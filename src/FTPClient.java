import javax.swing.*;
import java.net.*;
import java.io.*;
import java.lang.*;


/**
 * Created by mitchcout on 11/28/2017.
 */
public class FTPClient {
    private static JFrame frame;
    private static GameGUI panel;

    private static Socket ControlSocket;
    private static DataOutputStream toServer;
    private static DataInputStream fromServer;

    public static void main(String argv[]) {
        // Start GUI
        startGUI();
    }

    public static void connectToServer(String serverIP) throws Exception {
        int port = 5568;
        System.out.println("Connecting to " + serverIP + ":" + port);
        try {
            ControlSocket = new Socket(serverIP, port);
        } catch (IOException ioEx) {
            System.out.println("Unable to connect to " + serverIP + ":" + port);
            return;
        }
        toServer = new DataOutputStream(ControlSocket.getOutputStream());
        fromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));
    }

    private static void startGUI() {
        frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new GameGUI(Player.USER);
        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
    }

    public static void sendMoveToServer(Move move){

    }
}
