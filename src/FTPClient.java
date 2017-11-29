import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;


/**
 * Created by mitchcout on 11/28/2017.
 */
public class FTPClient {
    private static Socket ControlSocket;

    public static void main(String argv[]) throws Exception {
        String input;
        System.out.println("Type connect <ip> <port>");

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("FTPClient >> ");
        input = inFromUser.readLine();
        StringTokenizer tokens = new StringTokenizer(input);

        if (input.startsWith("connect")) {
            tokens.nextToken(); // connect
            String serverName = tokens.nextToken(); // serverIP
            int port = Integer.parseInt(tokens.nextToken()); // port
            System.out.println("Connecting to " + serverName + ":" + port);
            try {
                ControlSocket = new Socket(serverName, port);
            } catch (IOException ioEx) {
                System.out.println("Unable to connect to " + serverName + ":" + port);
                System.exit(1);
            }
            while (true) {
                DataOutputStream toServer = new DataOutputStream(ControlSocket.getOutputStream());
                DataInputStream fromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));

            }

        } else {
            System.out.println("You must connect to a server");
            System.exit(1);
        }

    }
}
