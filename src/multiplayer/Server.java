package multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import gui.ChessBoard;

//Socket til serveren, som lar klienten koble seg til
public class Server implements ConnectionInterface, Runnable {
	private int port;
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ChessBoard board;
	
	public Server(ChessBoard board, int port) {
		this.board = board;
		this.port = port;
	}
	
	//tạo socket cho server
	@Override
	public void run() {
		
		try {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
			
			if(socket.isConnected()) {
				input = new ObjectInputStream(socket.getInputStream());
				output = new ObjectOutputStream(socket.getOutputStream());
				handshake();
				notifyConnection();// thong bao ket noi hay chua
			}
			
			while(socket.isConnected()) {
				waitForResponse();// cho phan hoi
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
	
	//
	private void handshake() {
		
		try {			
			System.out.println("SERVER: kết nối");
			Message sendTestMsg = new Message("xin chào");
			Message getTestMsg = (Message)input.readObject();
			
			if(getTestMsg.getMessage() != null) {
				System.out.println(getTestMsg.getMessage());
				output.writeObject(sendTestMsg);
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	//chờ phản hồi từ người chơi khác
	private void waitForResponse() {
		
		try {
			Message response = null;	
			System.out.println("server chờ phản hồi..");
			
			while(input.available() > 0) {
				Thread.sleep(100);
			}
			
			response = (Message)input.readObject();
			
			System.out.println("SERVER: " + response.getMessage());
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
	
	//gửi tin nhắn cho người chơi khác
	public void sendResponse(Message response) {
		
		try {
			output.writeObject(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//nhận kết nối và cập nhật bảng 
	private void updateChessBoard(Message msg) {
		board.getUpdateFromSocket(msg);
	}
	
	//thông báo với GUI là bắt đầu trò chơi 
	private void notifyConnection() {
		board.onPlayerConnected();
	}

}
