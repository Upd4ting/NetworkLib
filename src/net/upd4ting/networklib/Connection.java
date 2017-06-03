package net.upd4ting.networklib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import net.upd4ting.networklib.logging.FichierLog;
import net.upd4ting.networklib.logging.Level;

public final class Connection implements Runnable {
	
	private ServerThread ss;
	private Socket socket;
	private boolean running;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public Connection(ServerThread ss, Socket socket) {
		this.socket = socket;
		this.running = false;
		this.ss = ss;
		
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			FichierLog.getInstance().writeLog(Level.SEVERE, "Failed to create flux");
			return;
		}
		
		running = true;
		
		FichierLog.getInstance().writeLog(Level.INFO, "Connection etablished");
		
		while (running && !socket.isClosed()) {
			try {
				String message = reader.readLine();
				
				if (message != null)
					ss.sendMessage(this, message);
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
			FichierLog.getInstance().writeLog(Level.WARNING, "Failed to close flux, maybe it was already closed");
		}
		
		FichierLog.getInstance().writeLog(Level.INFO, "Connection ended");
	}
	
	public void sendMessage(String message) {
		try {
			this.writer.write(message);
			this.writer.newLine();
			this.writer.flush();
		} catch (IOException e) {
			FichierLog.getInstance().writeLog(Level.WARNING, "Failed to write line, client/connection is disconnecting ?");
		}
	}
	
	public void close() {
		running = false;
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
}
