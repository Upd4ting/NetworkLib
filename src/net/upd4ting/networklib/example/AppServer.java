package net.upd4ting.networklib.example;

import java.util.Scanner;

import net.upd4ting.networklib.Server;

public class AppServer {

	public static void main(String[] args) {
		Server server = new Server(8000);
		server.start();
		
		System.out.println("Enter something to close server..");
		
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		scanner.close();
		
		server.stop();
	}

}
