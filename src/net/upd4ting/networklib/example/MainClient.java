package net.upd4ting.networklib.example;

import net.upd4ting.networklib.Client;
import net.upd4ting.networklib.MessageListener;

public class MainClient implements MessageListener {
	
	private Client client;
	
	public MainClient() {
	}
	
	public void start() {
		client = new Client("localhost", 8000, this);
	}
	
	public void close() {
		client.close();
	}
	
	public void subsribe(String receiver) {
		if (client == null) return;
		client.subscribe(receiver);
	}
	
	public void sendMessage(String receiver, String message) {
		if (client == null) return;
		client.sendMessage(receiver, message);
	}

	@Override
	public void onMessageReceive(String receiver, String payload) {
		System.out.println("I have received a message: " + payload);
	}
}
