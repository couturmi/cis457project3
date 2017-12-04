import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by mitchcout on 11/28/2017.
 */
public final class FTPServer {

    private static ServerSocket welcomeSocket;

    public static void main(String argv[]) throws Exception {
        try {
            welcomeSocket = new ServerSocket(5568);
        } catch (IOException ioEx) {
            System.out.println("Unable to set up port!");
            System.exit(1);
        }
        System.out.println("[Server] Server running on port: 5568");
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("[Client] Connected to " + connectionSocket.getRemoteSocketAddress().toString());

            // Create ClientHandler thread to handle client
            ClientHandler handler = new ClientHandler(connectionSocket);
            handler.start();
        }
    }
}

class ClientHandler extends Thread {

    private Socket clientSocket;
    private Scanner input;

    public ClientHandler(Socket socket) {
        //Set up reference to associated socket
        clientSocket = socket;

        try
        {
            input = new Scanner(clientSocket.getInputStream());
        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        }
    }

    public void run() {
        String fromClient;
        String clientCommand;
        String frstln;
        String name = input.nextLine();
        try {
            ChatHandler c = new ChatHandler(name, clientSocket);
            c.start();
        } catch(IOException ioEx) {
            ioEx.printStackTrace();
        }
        do {
            // read in initial command line from client
            fromClient = input.nextLine();

            StringTokenizer tokens = new StringTokenizer(fromClient);

            frstln = tokens.nextToken();
            int port = Integer.parseInt(frstln);
            clientCommand = tokens.nextToken();

            //if the command is "close", end this thread
            if(clientCommand.equals("close")){
                endConnection();
                return;
            }

            // TODO: handle client moves here

        } while (true);
    }

    /**
     * Closes the thread
     */
    private void endConnection() {
        System.out.println("[Quit] Disconnecting from client "+clientSocket.getRemoteSocketAddress().toString());
        input.close();
        try {
            clientSocket.close();
        } catch(IOException ioEx) {
            System.out.println("Unable to disconnect!");
        }
        System.out.println("[Quit] Disconnected from client");
    }
}
