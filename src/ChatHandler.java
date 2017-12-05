import java.net.*;
import java.io.*;
import java.util.*;

public class ChatHandler extends Thread {
    
   Socket socket;
   BufferedReader in;
   DataOutputStream out;
   String name;
   protected static Vector handlers = new Vector ();
    
   public ChatHandler (String name, Socket socket) throws IOException {
   this.name = name;
   this.socket = socket;
   in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
   out = new DataOutputStream(socket.getOutputStream());
   }
    
   public void run () {

   try {
      broadcast(name+" has entered" + '\n');
      handlers.addElement (this);

      while (true) {
         String message = in.readLine();
         if (message == null || message.equals("closeSocket")) {
            break;
         }
         broadcast(name + ": " + message + '\n');
      }
   } catch (IOException ex) {
      System.out.println("-- Connection to user lost.");
   } finally {
      handlers.removeElement (this);
      broadcast("closeSocket" + '\n');
      try {
         socket.close();
      } catch (IOException ex) {
         System.out.println("-- Socket to user already closed ?");
      }  
   }
   }

   protected static void broadcast (String message) {
   synchronized (handlers) {
      Enumeration e = handlers.elements ();
      while (e.hasMoreElements()) {
      ChatHandler handler = (ChatHandler) e.nextElement();
         try {
            handler.out.writeBytes(message);
            handler.out.flush();
         } catch (IOException ex) {
            handler.stop();
         }
      }
   }
   }
}
