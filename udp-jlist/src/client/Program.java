package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import server.ClientState;

public class Program {

	public static void main(String[] args) throws UnknownHostException {
		int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
		String host = ResourceBundle.getBundle("settings").getString("host");
		
		InetAddress address = InetAddress.getByName(host);
		try (DatagramSocket socket = new DatagramSocket()) {
			System.out.println("Ready to send");
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					String command = scanner.nextLine();
					byte[] buffer = new byte[512];
					
					buffer = command.getBytes();
					DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
					socket.send(request);
					
					buffer = new byte[512];
					DatagramPacket response = new DatagramPacket(buffer, buffer.length);
					socket.receive(response);
					
					String decoded = new String(buffer, 0, response.getLength());
					System.out.println(decoded);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.exit(0);
		}
	}
	
	
	
	

}
	
	




