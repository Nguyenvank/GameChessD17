package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//Tar seg av "backend" delen av sjakkmenyen p� starten. 
public class InitializeChess {
	
	public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
	
	String clientIP;
	int clientPort;
	int hostPort;
	JLabel localPlay = new JLabel();
	JLabel join = new JLabel();
	JLabel host = new JLabel();
	JLabel analyze = new JLabel();
	JTextField hostTextField = new JTextField();
	JTextField joinIPTextField = new JTextField();
	JTextField joinPortTextField = new JTextField();
	String localhost = "localhost";
	int port = 21337;
	boolean client = true;
	boolean server = false;
	boolean multiplayer = true;
	
	public InitializeChess() {
		ChessMenu menu = new ChessMenu();
		localPlay = menu.singleplayerLabel;
		join = menu.joinLabel;
		host = menu.hostLabel;
		analyze = menu.analyzeLabel;
		hostTextField = menu.hostPortTextfield;
		joinIPTextField = menu.clientIPTextfield;
		joinPortTextField = menu.clientPortTextfield;
		host.setFocusable(true);
		
		//Moustelistener n�r join labelen blir trykket inn. Den fjerner focus fra begge join feltene s�nn at de kj�rer sin focusLost. 
		join.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	joinIPTextField.setFocusable(false);
            	joinIPTextField.setFocusable(true);
            	joinPortTextField.setFocusable(false);
            	joinPortTextField.setFocusable(true);
            }

        });
		
		//Mouselistener n�r host knappen blir trykket inn. Gj�r det samme som den over. 
		host.addMouseListener(new MouseAdapter() {
			
            @Override
            public void mousePressed(MouseEvent e) {
            	//n�r du trykker her s� skal alle felt miste fokus
            	hostTextField.setFocusable(false);
            	hostTextField.setFocusable(true);
            }
            
        });
		
		//N�r slipper knappen du trykker inn, s� vil feltene miste fokus og vi f�r da hentet verdiene. Den bruker verdiene fra tekstfeltene til � starte en ny instans av ChessBoard med �nsket verdier. 
		host.addMouseListener(new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e) {
		    	hostPort = menu.hostPort;
		    	System.out.println(hostPort);
		    	
				if (hostPort == 0) {
					InitializeChess.infoBox("Vennligst skriv inn en port", "Feilmelding");
				} else {
					new ChessBoard(multiplayer, server, "localhost", hostPort, "Sjakk"); 
				}
				
			}
			
		});
		
		//Gj�r det samme som den over, bare for join knappen istedet. 
		join.addMouseListener(new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e) {
				clientIP = menu.clientIP;
				clientPort = menu.clientPort;
				
				if (clientIP == null && clientPort == 0) {
					InitializeChess.infoBox("Vennligst skriv inn en IP adresse og en port", "Feilmelding");
				} else {
					System.out.println(clientPort + clientIP);
					new ChessBoard(multiplayer, client, clientIP, clientPort, "Sjakk");
				}
				
			}
			
		});
    
		//N�r du trykker p� analyze labelen vil ChessBoardAnalyze instansen kj�res. 
		analyze.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
            	new ChessBoardAnalyze();
            }

        });
		
		//N�r du trykker p� lokal flerspiller labelen vil ChessBoard instansieres to ganger med localhost verdier for lokal flerspiller. 
		localPlay.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
            	new ChessBoard(multiplayer, server, localhost, port, "Sjakk"); 
            	new ChessBoard(multiplayer, client, localhost, port, "Sjakk");
            	
            }

        });
		
	}
	
}
