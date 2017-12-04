import javax.swing.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.StringTokenizer;


/**
 * Created by mitchcout on 11/28/2017.
 */
public class FTPClient {
    private static final int port = 5568;
    private static JFrame frame;
    private static GameGUI panel;

    private static Socket ControlSocket, chatSocket;
    private static DataOutputStream toServer, chatToServer;
    private static BufferedReader chatFromServer;
    private static WaitForOpponentThread currentWaitThread;
    private static ReadChat chatReader;

    public static void main(String argv[]) {
        // Start GUI
        startGUI();
    }

    public static void connectToServer(String serverIP, String name) throws Exception {
        System.out.println("Connecting to " + serverIP + ":" + port);
        try {
            ControlSocket = new Socket(serverIP, port);
            chatSocket = new Socket(serverIP, port+10);
        } catch (IOException ioEx) {
            System.out.println("Unable to connect to " + serverIP + ":" + port);
            return;
        }
        System.out.println("Connected.");
        toServer = new DataOutputStream(ControlSocket.getOutputStream());
        chatToServer = new DataOutputStream(chatSocket.getOutputStream());
        toServer.writeBytes(name + '\n');
        chatReader = new ReadChat(panel, chatSocket);
        chatReader.start();
        // enable game board
        panel.enableBoard();
    }

    private static void startGUI() {
        frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new GameGUI(Player.USER);
        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
    }

    public static void sendChat(String text) {
        try {
            chatToServer.writeBytes(text + '\n');
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    public static void sendMoveToServer(Move move){
        int port1 = port + 2;
        try {
            // send move to server
            ServerSocket sendMoveData = new ServerSocket(port1);
            toServer.writeBytes(port1 + " " + move.isUserWon() + " " + move.getTileId() + " " + '\n');

            // get response from server for opponents move
            currentWaitThread = new WaitForOpponentThread(sendMoveData, panel);
            currentWaitThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() throws Exception {
        System.out.println("Closing Control Socket");
        toServer.writeBytes(0 + " close " + '\n');
        ControlSocket.close();
        toServer.close();
    }
}

class ReadChat extends Thread {
    private GameGUI panel;
    private JScrollBar vert;
    private Socket socket;
    private static BufferedReader chatFromServer;
    public ReadChat(GameGUI panel, Socket socket) {
        this.panel = panel;
        this.socket = socket;
    }
    public void run() {
        try {
            chatFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true) {
                String message = chatFromServer.readLine();
                if (message == null || message.equals("closeSocket")) {
                    break;
                }
                panel.chatTextArea.append(message + '\n');
                vert = panel.scrollPane.getVerticalScrollBar();
                vert.setValue(vert.getMaximum());
            }
            socket.close();
            chatFromServer.close();
        } catch (IOException ioEx) {
            System.out.println("Read Chat Error");
            ioEx.printStackTrace();
        }
    }
}

class WaitForOpponentThread extends Thread {
    private ServerSocket sendMoveData;
    private GameGUI panel;

    public WaitForOpponentThread(ServerSocket sendMoveData, GameGUI panel) {
        this.sendMoveData = sendMoveData;
        this.panel = panel;
    }

    public void run() {
        try {
            Socket dataSocket = sendMoveData.accept();
            BufferedReader inData = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            String response = inData.readLine();

            // add recieved data to Move and send to game
            StringTokenizer tokens = new StringTokenizer(response);
            boolean userWon = Boolean.parseBoolean(tokens.nextToken());
            int tileId = Integer.parseInt(tokens.nextToken());
            Move opponentMove = new Move(tileId, userWon);
            panel.updateOpponentsMove(opponentMove);

            // close sockets
            inData.close();
            dataSocket.close();
            sendMoveData.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
