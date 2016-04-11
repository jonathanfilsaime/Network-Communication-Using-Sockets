// By Greg Ozbirn, University of Texas at Dallas
// Adapted from example at Sun website:
// http://java.sun.com/developer/onlineTraining/Programming/BasicJava2/socket.html
// 11/07/07





import java.io.*;
import java.net.*;
import java.util.ArrayList;



class ClientWorker implements Runnable 
{
   private Socket client;
   
   ClientWorker(Socket client) 
   {
      this.client = client;
   }

   public void run()
   {
       
       
      String line;
      BufferedReader in = null;
      PrintWriter out = null;
      
       try
      {
          in = new BufferedReader(new InputStreamReader(client.getInputStream()));
          out = new PrintWriter(client.getOutputStream(), true);
      } 
      catch (IOException e) 
      {
          System.out.println("in or out failed");
          System.exit(-1);
      }

           try
           {
               while(true){
               // Receive text from client
               line = in.readLine();
	 
               // Send response back to client
                   if(!(isInteger(line)))
                   {
                       line = "Hi " + line;
                       out.println(line);
                   }
                   else if(Integer.parseInt(line) < 1 || Integer.parseInt(line) > 7)
                   {
                       out.println("please enter a number from 1 to 7");
                   }
                   else
                   {
                       //past a int here and call a case function
                       //************the meat will go here*******
                       out.println(SocketThrdServer.threadArray.size());
                   }
                
               }
 
           }
           catch (IOException e)
            {
                System.out.println("Read failed");
                System.exit(-1);
            }
       

      try
      {
          client.close();
      } 
      catch (IOException e) 
      {
          System.out.println("Close failed");
          System.exit(-1);
      }
    
   }
    
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

}

class SocketThrdServer
{
   ServerSocket server = null;

   public void listenSocket(int port) throws IOException
   {
      try
      {
          server = new ServerSocket(port);
          System.out.println("Server running on port " + port +
	                     "," + " use ctrl-C to end");
      } 
      catch (IOException e) 
      {
          System.out.println("Error creating socket");
          System.exit(-1);
      }
       
      while(true)
      {
         ClientWorker w;
          
         try
         {
                w = new ClientWorker(server.accept());
                Thread t = new Thread(w);
                threadArray.add(t);
                threadArray.get(count++).start();

            }
        catch (IOException e)
          
            {
                System.out.println("Accept failed");
                System.exit(-1);
            }
        }
    }

   protected void finalize()
   {
      try
      {
         server.close();
      } 
      catch (IOException e) 
      {
         System.out.println("Could not close socket");
         System.exit(-1);
      }
   }
    
    public static ArrayList<Thread> threadArray = new ArrayList<>();
    public static int count = 0;

   public static void main(String[] args) throws IOException
   {
       
       
      if (args.length != 1)
      {
          System.out.println("Usage: java SocketThrdServer port");
          System.exit(1);
      }

      SocketThrdServer server = new SocketThrdServer();
      int port = Integer.valueOf(args[0]);
      server.listenSocket(port);
   }
}

