package net.upd4ting.networklib;

public final class Server {
	
	private ServerThread sThread;
	
	public Server(int port) {
		this.sThread = new ServerThread(port);
	}
	
	public void start() {
		sThread.start();
	}
	
	public void stop() {
		sThread.stop();
	}
}
