import java.net.*;
import java.io.*;
import java.util.*;

public class ChatHandler extends Thread {
    
   Socket socket;
   DataInputStream in;
   DataOutputStream out;
   String name;
   protected static Vector handlers = new Vector ();
    
   public ChatHandler (String name, Socket socket) throws IOException {
   this.name = name;
   this.socket = socket;
   in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
   out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
   }
    
   public void run () {

   try {
      broadcast(name+" entered");
      handlers.addElement (this);

      while (true) {
      String message = in.readUTF();
      broadcast(name + ": " + message);
      }

   } catch (IOException ex) {
      System.out.println("-- Connection to user lost.");
   } finally {
      handlers.removeElement (this);
      broadcast(name+" has left");
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
            handler.out.writeUTF(message);
            handler.out.flush();
         } catch (IOException ex) {
            handler.stop ();
         }
      }
   }
   }
}