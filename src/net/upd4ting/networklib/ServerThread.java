package net.upd4ting.networklib;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.upd4ting.networklib.logging.FichierLog;
import net.upd4ting.networklib.logging.Level;

public final class ServerThread implements Runnable {
	
	private int port;
	private boolean running;
	private Thread thread;
	private ServerSocket serverSocket;
	private List<Connection> connections = new ArrayList<>();
	
	public ServerThread(int port) {
		this.running = false;
		this.port = port;
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				FichierLog.getInstance().writeLog(Level.INFO, "New client connection...");
				
				connections.add(new Connection(this, socket));
			} catch (IOException e) {
				FichierLog.getInstance().writeLog(Level.WARNING, "ServerSocket#accept throw an exception, the server is closed ?");
			}
		}
		
		FichierLog.getInstance().writeLog(Level.INFO, "Closing connections...");
		
		for (Connection conn : connections) {
			conn.close();
		}
		
		FichierLog.getInstance().writeLog(Level.INFO, "Closing server...");
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			FichierLog.getInstance().writeLog(Level.SEVERE, "Failed to close server...");
		}
		
		FichierLog.getInstance().writeLog(Level.INFO, "Server closed");
	}
	
	public void start() {
		if (thread != null)
			return;
		
		try {
			serverSocket = new ServerSocket(port);
			FichierLog.getInstance().writeLog(Level.INFO, "Server listening on port " + port);
		} catch (IOException e) {
			FichierLog.getInstance().writeLog(Level.SEVERE, "Failed to start server on port " + port);
		}
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() { // Will cause to stop method ServerSocket#accept with a SocketException and the while loop will be breaked
		running = false;
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void sendMessage(Connection con, String message) {	
		for (Connection conn : new ArrayList<>(connections)) {
			if (conn.getSocket().isClosed()) {
				FichierLog.getInstance().writeLog(Level.WARNING, "A disconnected client has been found while sending a message, closing it...");
				connections.remove(conn);
				continue;
			}
			
			if (conn != con) {
				conn.sendMessage(message);
			}
		}
		
		FichierLog.getInstance().writeLog(Level.INFO, "Message sended: " + message);
	}

}
