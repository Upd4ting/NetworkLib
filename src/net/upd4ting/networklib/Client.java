package net.upd4ting.networklib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.upd4ting.networklib.logging.FichierLog;
import net.upd4ting.networklib.logging.Level;

public final class Client implements Runnable {
	
	private String ip;
	private int port;
	private Socket socket;
	private boolean running;
	private BufferedReader reader;
	private BufferedWriter writer;
	private Thread thread;
	
	private MessageListener listener;
	private List<String> receivers = new ArrayList<>();
	
	public Client(String ip, int port, MessageListener listener) {
		this.ip = ip;
		this.port = port;
		this.running = false;
		this.listener = listener;
		
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		try {
			this.socket = new Socket(ip, port);
		} catch (IOException e) {
			FichierLog.getInstance().writeLog(Level.SEVERE, "Failed to open socket!");
			return;
		}
		
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			FichierLog.getInstance().writeLog(Level.SEVERE, "Failed to create flux");
			return;
		}
		
		running = true;
		
		FichierLog.getInstance().writeLog(Level.INFO, "Client etablished!");
		
		while (running && !socket.isClosed()) {
			try {
				String message = reader.readLine();
				
				if (message != null) {
					String[] splitted = message.split("#");
					String receiver = splitted[0];
					String payload = splitted[1];
					
					FichierLog.getInstance().writeLog(Level.INFO, "Client received a message");
					
					if (receivers.contains(receiver))
						listener.onMessageReceive(receiver, payload);
				}
			} catch (IOException e) {
				FichierLog.getInstance().writeLog(Level.WARNING, "Failed to read line");
			}
		}
		
		try {
			if (this.reader != null)
				this.reader.close();
			if (this.writer != null)
				this.writer.close();
			if (this.socket != null)
				this.socket.close();
		} catch (IOException e) {
			FichierLog.getInstance().writeLog(Level.WARNING, "Failed to close flux");
		}
		
		FichierLog.getInstance().writeLog(Level.INFO, "Client disconnected!");
	}
	
	public void sendMessage(String receiver, String payload) {
		if (this.writer == null) {
			FichierLog.getInstance().writeLog(Level.SEVERE, "Cannot send a message because flux is null!");
			return;
		}
		
		try {
			String message = receiver + "#" + payload;
			this.writer.write(message);
			this.writer.newLine();
			this.writer.flush();
			FichierLog.getInstance().writeLog(Level.INFO, "Client sent a message: " + message);
		} catch (IOException e) {
			e.printStackTrace();
			FichierLog.getInstance().writeLog(Level.SEVERE, "Failed to write line");
		}
	}
	
	public void subscribe(String receiver) {
		this.receivers.add(receiver);
	}
	
	public void close() { // Will make throw an exception in Reader#readLine and the while loop will be breaked!
		if (!this.running) {
			FichierLog.getInstance().writeLog(Level.WARNING, "Cannot close client if client isn't open!");
			return;
		}
		
		running = false;
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
