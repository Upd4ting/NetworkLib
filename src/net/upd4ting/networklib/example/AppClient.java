package net.upd4ting.networklib.example;

import java.util.Scanner;

public class AppClient {
	
	public static void main(String[] args) {
		MainClient client = new MainClient();
		client.start();
		client.subsribe("client1");
		
		System.out.println("Enter something to send message..");
		
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		scanner.close();
		
		client.sendMessage("client2", "bonjour");
		client.close();
	}
}
