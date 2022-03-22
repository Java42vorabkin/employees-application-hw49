package telran.net;
import java.io.*;
import java.net.*;

import telran.net.dto.*;
public class TcpClientServer implements Runnable { 
Socket socket;
ObjectInputStream reader;
ObjectOutputStream writer;
ApplProtocol protocol;
boolean shutdown = false;
int clientId;

public TcpClientServer(Socket socket, ApplProtocol protocol, int clientId) throws Exception{
	this.socket = socket;
	
	//socket.setSoTimeout(1000); //if socket is in the idle mode after 1 sec.
	//there will be SocketTimeoutException
	reader = new ObjectInputStream(socket.getInputStream());
	writer = new ObjectOutputStream(socket.getOutputStream());
	this.clientId = clientId;
	this.protocol = protocol;
	socket.setSoTimeout(1000);
}
	@Override
	public void run() {
		boolean cycle = true;
		while(cycle) {
			try {
				Request request = (Request) reader.readObject();
				Response response = protocol.getResponse(request);
				writer.writeObject(response);
			} catch(EOFException e) {
				closeSocket();
				cycle = false;
			} catch (SocketTimeoutException  e) {
				if(shutdown) {
					System.out.println("Shutdown & SocketTimeoutException");
					closeSocket();
					cycle = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				cycle = false;
			}			
		}
		System.out.println("connection with clientId="+clientId + " is closed");
	}
	private void closeSocket() {
		try {
			socket.close();
			System.out.println("Close socket for. clientId="+clientId);
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}
	public void shutdown() {
		shutdown = true;
	}
}
