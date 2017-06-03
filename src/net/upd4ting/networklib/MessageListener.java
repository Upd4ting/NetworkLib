package net.upd4ting.networklib;

public interface MessageListener {
	public void onMessageReceive(String receiver, String payload);
}
