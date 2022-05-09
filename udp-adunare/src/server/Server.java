package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server implements AutoCloseable {

	private DatagramSocket socket;
	
	public Server(int port) throws IOException {
		socket = new DatagramSocket(port);
		while (socket != null && !socket.isClosed()) {
			byte[] buffer = new byte[512];
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			
			socket.receive(request);
			
			InetAddress clientAddress = request.getAddress();
			int clientPort = request.getPort();
			
			String decoded = new String(buffer, 0, request.getLength());
			
			System.out.println(decoded);
			
			String string = processCommand(decoded);
			
			buffer = string.getBytes();
			
			DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
			socket.send(response);
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	public String processCommand(String input) {
		
		int suma = 0;
		
			String[] items = input.split("\\s");
			if (items.length < 2) {
				return "Command did not have enough params";
			} else {
				int i = 0;
				while(items.length != i) {
				if(items[i]!=null) {
					
					suma = suma + Integer.parseInt(items[i]);
					i++;
					
				
			}
		}
		
		String rez = String.valueOf(suma);
		return rez;
	}

}}
