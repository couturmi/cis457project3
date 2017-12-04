import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by mitchcout on 11/28/2017.
 */
public final class FTPServer {

    private static final int chatPort = 5578;
    private static ServerSocket welcomeSocket, chatSocket;

    public static void main(String argv[]) throws Exception {
        try {
            welcomeSocket = new ServerSocket(5568);
            chatSocket = new ServerSocket(chatPort);
        } catch (IOException ioEx) {
            System.out.println("Unable to set up port!");
            System.exit(1);
        }
        System.out.println("[Server] Server running on port: 5568");
        while (true) {
            Socket connectionSocket1 = welcomeSocket.accept();
            System.out.println("[Client 1] Connected to " + connectionSocket1.getRemoteSocketAddress().toString());
            Socket client1 = chatSocket.accept();

            Socket connectionSocket2 = welcomeSocket.accept();
            System.out.println("[Client 2] Connected to " + connectionSocket2.getRemoteSocketAddress().toString());
            Socket client2 = chatSocket.accept();

            // Create ClientHandler thread to handle client
            ClientHandler handler = new ClientHandler(connectionSocket1, connectionSocket2, client1, client2);
            handler.start();
        }
    }
}

class ClientHandler extends Thread {

    private final int dataPort = 5570;

    private Socket clientSocket1, clientSocket2, chatClient1, chatClient2;
    private Scanner input1, input2;

    public ClientHandler(Socket socket1, Socket socket2, Socket socket3, Socket socket4) {
        //Set up reference to associated socket
        clientSocket1 = socket1;
        clientSocket2 = socket2;
        chatClient1 = socket3;
        chatClient2 = socket4;
        try
        {
            input1 = new Scanner(clientSocket1.getInputStream());
            input2 = new Scanner(clientSocket2.getInputStream());
        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        }
    }

    public void run() {
        String fromClient;
        String clientCommand;
        String firstItem, secondItem, thirdItem;
        boolean client1Turn = true;
        String name1 = input1.nextLine();
        String name2 = input2.nextLine();
        try {
            ChatHandler c1 = new ChatHandler(name1, chatClient1);
            ChatHandler c2 = new ChatHandler(name2, chatClient2);
            c1.start();
            c2.start();
        } catch(IOException ioEx) {
            ioEx.printStackTrace();
        }
        do {
            // read in initial command line from whichever clients turn it is
            if(client1Turn) {
                fromClient = input1.nextLine();
            } else {
                fromClient = input2.nextLine();
            }

            StringTokenizer tokens = new StringTokenizer(fromClient);

            firstItem = tokens.nextToken();
            secondItem = tokens.nextToken();

            //if the command is "close", end this thread
            if(secondItem.equals("close")){
                endConnection();
                return;
            } else {
                // if not close, get third item
                thirdItem = tokens.nextToken();
            }

            try {
                Socket dataSocket;
                if (client1Turn) {
                    dataSocket = new Socket(clientSocket2.getInetAddress(), dataPort);
                } else {
                    dataSocket = new Socket(clientSocket1.getInetAddress(), dataPort);
                }
                DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());
                dataOutToClient.writeBytes(secondItem + " " + thirdItem + " " + '\n');
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // change turn
            client1Turn = !client1Turn;

        } while (true);
    }

    /**
     * Closes the thread
     */
    private void endConnection() {
        System.out.println("[Quit] Disconnecting from client "+clientSocket1.getRemoteSocketAddress().toString());
        System.out.println("[Quit] Disconnecting from client "+clientSocket2.getRemoteSocketAddress().toString());
        input1.close();
        input2.close();
        try {
            clientSocket1.close();
            clientSocket2.close();
        } catch(IOException ioEx) {
            System.out.println("Unable to disconnect!");
        }
        System.out.println("[Quit] Disconnected from clients");
    }
}
