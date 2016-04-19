// By Greg Ozbirn, University of Texas at Dallas
// Adapted from example at Sun website:
// http://java.sun.com/developer/onlineTraining/Programming/BasicJava2/socket.html
// 11/07/07





import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.text.SimpleDateFormat;
import java.util.Date;


class ClientWorker implements Runnable 
{
    private Socket client;
    public int myID;
    public BufferedReader in = null;
    public PrintWriter out = null;
    public String line;
    public String sentBack = "";
    public String receiver;
    public String message;
    public ArrayList<String> ms = new ArrayList<>();
   
    ClientWorker(Socket client) 
    {
	this.client = client;
    }

    public void run()
    {
       
       
	
    
    
      
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
                   myID = SocketThrdServer.id;
                   SocketThrdServer.name[myID] = line;
                   SocketThrdServer.online[myID] = 1;
                   
                   System.out.println(time() +" " + "Connection by known user " + line + ".");
                   
                   
			       line = "Hi " + line;
                   ms.add(line);
                   
                   SocketThrdServer.table.put(SocketThrdServer.name[SocketThrdServer.id], ms);
                   
			       out.println(line);
                   SocketThrdServer.id++;
			   }
		       else if(Integer.parseInt(line) < 1 || Integer.parseInt(line) > 7)
			   {
			       out.println("please enter a number from 1 to 7");
			   }
		       else
			   {
			       //past a int here and call a case function
//			       //************the meat will go here*******
//			       //out.println(SocketThrdServer.threadArray.size());
//                   for(int h = 0 ; h < SocketThrdServer.id ; h++){
//                       System.out.println(SocketThrdServer.table.get(SocketThrdServer.name[h]));
                   exe(Integer.parseInt(line));
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

    public void exe(int x){
        try{
        switch (x) {

                case 1:
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + " displays all known users.");

                for(int i = 0; i < SocketThrdServer.id; i++){
                    sentBack += (SocketThrdServer.name[i] + " ");
                    
                    }
                    out.println(sentBack);
                    sentBack = "";
                break;

                case 2:
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + " displays all connected users.");

                for(int i = 0; i < SocketThrdServer.id; i++ ){
                    if(SocketThrdServer.online[i] == 1){
                    sentBack += (SocketThrdServer.name[i] + " ");
                        }
                    }
                out.println(sentBack);
                sentBack = "";
                break;

                case 3:
                
                out.println("name of the receiver ");
                line = in.readLine();
                receiver = line;

                out.println("message: ");
                line = in.readLine();
                message = line;
                
                if(SocketThrdServer.table.containsKey(receiver)){
                    SocketThrdServer.table.get(receiver).add(message);
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + " posts a message for " + receiver + ".");
                }
                else{
                    ArrayList<String> ms1 = new ArrayList<>();
                    SocketThrdServer.id++;
                    SocketThrdServer.name[SocketThrdServer.id] = receiver;
                    ms1.add(message);
                    SocketThrdServer.table.put(SocketThrdServer.name[SocketThrdServer.id], ms1);
                    System.out.println(time() +" "+ SocketThrdServer.name[myID] + " posts a message for unknow user " + receiver + ".");
                }
                
                out.println("Message posted to " + receiver);
                    
                break;

                case 4:
                out.println("message all currently connected users: ");
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + " posts a message for all currently connected users.");
                line = in.readLine();
                message = line;

                for(int i = 0 ; i < SocketThrdServer.id ; i++){
                    if(SocketThrdServer.online[i] == 1){
                        SocketThrdServer.table.get(SocketThrdServer.name[i]).add(message);
                    }
                }
                
                out.println("Message posted to all currently connected users." );
                break;

            case 5:
                out.println("email all users: ");
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + " posts a message for all known users.");

                line = in.readLine();
                message = line;

                for(int i = 0 ; i < SocketThrdServer.id ; i++){
                    SocketThrdServer.table.get(SocketThrdServer.name[i]).add(message);
                    }
                
                out.println("Message posted to all users. " );
                break;

                case 6:
                out.println("my messages: ");
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + "gets messages.");

                for(int i = 0 ; i < SocketThrdServer.table.get(SocketThrdServer.name[myID]).size(); i++){
                    System.out.println(SocketThrdServer.table.get(SocketThrdServer.name[myID]).get(i));
                }
                SocketThrdServer.table.get(SocketThrdServer.name[myID]).removeAll(ms);
                break;

                case 7:
                SocketThrdServer.online[myID] = 0;
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + "exits.");

//                for(int i = 0 ; i < name.size(); i++){
//                    System.out.println(t.get(name.get(i)));
//                }
                System.exit(0);
                break;
            }
        }
        catch (IOException e)
        {
                System.out.println("crash");
            
        }
    
}

        public String time(){
                SimpleDateFormat localDateFormat = new SimpleDateFormat("MM/dd/yy h:mm:ss a");
                String time = localDateFormat.format(new Date());
                return time;
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
    
    public static TreeMap<String, ArrayList> table = new TreeMap<>();
    public static ArrayList<Thread> threadArray = new ArrayList<>();
    public static int count = 0;
    public static int id = 0;
    public static String[] name = new String[100];
    public static int[] online = new int[100];

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
