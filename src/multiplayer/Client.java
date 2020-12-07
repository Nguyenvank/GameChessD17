package multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import gui.ChessBoard;

//Socket til klienten, som kobler seg til en server
public class Client implements ConnectionInterface, Runnable {
	private String ip;
	private int port;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ChessBoard board;
	
	public Client(ChessBoard board, String ip, int port) {
		this.board = board;
		this.ip = ip;
		this.port = port;
	}
	
	//Entry point for tr�den v�r
	@Override
	public void run() {
		
		try {
			socket = new Socket(ip, port);

			if(socket.isConnected()) {
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());
				handshake();
				notifyConnection();
			}
			
			while(socket.isConnected()) {
				waitForResponse();
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	//Si hei til hverandre
	private void handshake() {
		
		try {
			System.out.println("CLIENT: kết nối");
			Message sendTestMsg = new Message("client chờ kết nối...");
			output.writeObject(sendTestMsg);
			
			Message getTestMsg = (Message)input.readObject();
			System.out.println(getTestMsg.getMessage());	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	//Vent p� en melding fra den andre spilleren
	private void waitForResponse() {
		
		try {
			Message response = null;	
			System.out.println("client nhận phản hồi..");
			
			while(input.available() > 0) {
				Thread.sleep(100);
			}
			
			response = (Message)input.readObject();
			
			System.out.println("CLIENT: " + response.getMessage());
			updateChessBoard(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
	
	//Send en melding til den andre spilleren, sendes n�r en brikke blir flyttet
	public void sendResponse(Message response) {
		
		try {
			output.writeObject(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Kj�res n�r en melding har blitt mottatt, lar oss oppdatere brettet v�rt
	private void updateChessBoard(Message msg) {
		board.getUpdateFromSocket(msg);
	}
	
	//Si ifra til GUI at vi har blitt tilkoblet og starter sjakkspillet
	private void notifyConnection() {
		board.onPlayerConnected();
	}
	
}
