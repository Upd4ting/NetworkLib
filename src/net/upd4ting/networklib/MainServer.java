package net.upd4ting.networklib;

import java.util.Scanner;

public final class MainServer {
	
	// THIS IS JUST FOR TESTING PURPOSE
	
	public static void main(String[] args) {
		Server server = new Server(8000);
		server.start();
		
		System.out.println("Entrez un caractère pour fermer le serveur...");
		
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		scanner.close();
		
		server.stop();
	}
}
