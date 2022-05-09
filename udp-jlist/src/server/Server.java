package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements AutoCloseable {
	
	private Map<String, List<String>> storage = Collections.synchronizedMap(new HashMap<>());

	private DatagramSocket socket;
	
	public Server(int port) throws IOException {
		socket = new DatagramSocket(port);
		while (socket != null && !socket.isClosed()) {
			byte[] buffer = new byte[512];
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			
			List<Socket> clients = Collections.synchronizedList(new ArrayList<Socket>());
			
			ClientState state = new ClientState();
			
			socket.receive(request);
			
			InetAddress clientAddress = request.getAddress();
			int clientPort = request.getPort();
			
			String decoded = new String(buffer, 0, request.getLength());
			
			System.out.println(decoded);
			
			String string = processComand(decoded,state);
			
			buffer = string.getBytes();
			
			DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
			socket.send(response);
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	private String processComand(String request, ClientState state) {
		String[] items = request.strip().split("\\s");
		if (true) {
			switch (items[0]) {
			case "put":
				if (items.length == 3) {
					List<String> existing;
					if (storage.containsKey(items[1])) {
						existing = storage.get(items[1]);
					} else {
						existing = new ArrayList<>();
					}
					existing.add(items[2]);
					storage.put(items[1], existing);
					return "added to the list";
				} else {
					return "wrong number of params";					
				}
			case "get":
				if (items.length == 2) {
					if (storage.containsKey(items[1])) {
						return storage.get(items[1]).stream().reduce("", (a, e) -> a + " " + e);
					} else {
						return "no such list";
					}
				} else {
					return "wrong number of params";					
				}
			case "delete":
				if (items.length == 2) {
					if (storage.containsKey(items[1])) {
						storage.remove(items[1]);
						return "list deleted";
					} else {
						return "no such list";
					}
				} else {
					return "wrong number of params";					
				}
			case "lists":
				if (items.length == 1) {
					return storage.keySet().stream().reduce("", (a, e) -> a + e);
				} else {
					return "wrong number of params";					
				}
			default:
				return "unknown command";
			}
		} else {
			if (items[0].equals("auth")) {
				if (items.length == 2) {
					if (items[1].equals("supersecret")) {
						state.isAuthenticated = true;
						return "welcome in";
					} else {
						return "you don't know the secret";
					}
				} else {
					return "wrong number of params";
				}
			} else {
				return "tell me a secret";
			}
		}
	}

}
