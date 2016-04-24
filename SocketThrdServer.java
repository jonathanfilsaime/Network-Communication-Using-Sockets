// By Greg Ozbirn, University of Texas at Dallas
// Adapted from example at Sun website:
// http://java.sun.com/developer/onlineTraining/Programming/BasicJava2/socket.html
// 11/07/07
//Jonathan Fils-Aime
//4/22/2016



import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.text.SimpleDateFormat;
import java.util.Date;


class ClientWorker implements Runnable 
{
    public static Sem s = new Sem(1);
    private Socket client;
    public int myID;
    public BufferedReader in = null;
    public PrintWriter out = null;
    public String line;
    public String sentBack = "";
    public String receiver;
    public String message;
    public ArrayList<String> ms = new ArrayList<>();
    public int lenghtString;
    public int me;
   
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
		   while(true)
                {
               //reas in the input form the client
		       line = in.readLine();
               
               //if the client already exist
               if(match(line))
                    {
                        didMatch(line);
                    }
               //if the client does not exist
               else
                    {
                        didNotMatch(line);
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
    
    //check if the input is an integer
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
    
    //check if the client already exist
    public void didMatch(String line)
    {
        if(!(isInteger(line)))
        {
            s.stop();
            for(int i = 0; i <= SocketThrdServer.id; i++)
            {
                if(SocketThrdServer.name[i] == line);
                {
                    me = i;
                }
            }
            myID = me;
            SocketThrdServer.name[myID] = line;
            System.out.println(myID);
            SocketThrdServer.online[myID] = 1;
            
            
            if(line != null)
            {
                System.out.println(time() +" " + "Connection by known user " + line + ".");
                out.println(line);
                SocketThrdServer.id++;
                s.go();
            }
            else
            {
                System.out.println("thread exited");
            }
        }
        else if(Integer.parseInt(line) < 1 || Integer.parseInt(line) > 7)
        {
            out.println("please enter a number from 1 to 7");
        }
        else
        {
            s.stop();
            exe(Integer.parseInt(line));
            s.go();
        }
        
    }
    
    //if client does not
    public void didNotMatch(String line){
        if(!(isInteger(line)))
        {
            s.stop();
            myID = SocketThrdServer.id;
            SocketThrdServer.name[myID] = line;
            System.out.println(myID);
            SocketThrdServer.online[myID] = 1;
            
            if(line != null)
            {
                System.out.println(time() +" " + "Connection by known user " + line + ".");
                SocketThrdServer.table.put(SocketThrdServer.name[SocketThrdServer.id], ms);
                out.println(line);
                SocketThrdServer.id++;
                s.go();
            }
            else
            {
                System.out.println("thread exited");
            }
        }
        else if(Integer.parseInt(line) < 1 || Integer.parseInt(line) > 7)
        {
            out.println("please enter a number from 1 to 7");
        }
        else
        {
            s.stop();
            exe(Integer.parseInt(line));
            s.go();
        }
    }

    //memu choice
    public void exe(int x){
        try{
        switch (x) {

                //view all know user
                case 1:
                for(int i = 0; i < SocketThrdServer.id; i++)
                    {
                            sentBack += (SocketThrdServer.name[i*2] + " ");
                    }
                    out.println(sentBack);
                    sentBack = "";
                    System.out.println(time() +" "+ SocketThrdServer.name[myID] + " displays all known users.");
                break;
                
                //view all connected users
                case 2:
                for(int i = 0; i < SocketThrdServer.id; i++ )
                {
                    if(SocketThrdServer.online[i] == 1)
                    {
                        sentBack += (SocketThrdServer.name[i] + " ");
                    }
                }
                out.println(sentBack);
                sentBack = "";
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + " displays all connected users.");
                break;

                //send a message a specific person
                case 3:
                out.println("name of the receiver ");
                line = in.readLine();
                receiver = line;

                out.println("message: ");
                line = in.readLine() ;
                message = "From " + SocketThrdServer.name[myID] + ", " +time() + ", " + line;
                
                if(SocketThrdServer.table.containsKey(receiver))
                {
                    SocketThrdServer.table.get(receiver).add(message);
                    System.out.println(time() +" "+ SocketThrdServer.name[myID] + " posts a message for " + receiver + ".");
                }
                else
                {
                    ArrayList<String> ms1 = new ArrayList<>();
                    SocketThrdServer.id++;
                    SocketThrdServer.name[SocketThrdServer.id] = receiver;
                    SocketThrdServer.table.put((receiver), ms1);
                    SocketThrdServer.table.get(receiver).add(message);
                    System.out.println(time() +" "+ SocketThrdServer.name[myID] + " posts a message for unknow user " + receiver + ".");
                }
            
                out.println("Message posted to " + receiver);
                break;

                //sned a message to all connected users
                case 4:
                out.println("message all currently connected users: ");
                line = in.readLine();
                message = "From " + SocketThrdServer.name[myID] + ", " +time() + ", " +line;

                for(int i = 0 ; i <= SocketThrdServer.id ; i++)
                {
                    if(SocketThrdServer.online[i] == 1)
                    {
                        SocketThrdServer.table.get(SocketThrdServer.name[i]).add(message);
                    }
                }
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + " posts a message for all currently connected users.");
                out.println("Message posted to all currently connected users." );
                break;

                //send a message to all known users
                case 5:
                out.println("email all users: ");
                line = in.readLine();
                message = "From " + SocketThrdServer.name[myID] + ", " +time() + ", " + line;
    
                for(int i = 0 ; i <= SocketThrdServer.id ; i++)
                {
                    if(SocketThrdServer.name[i] != null)
                    {
                        SocketThrdServer.table.get(SocketThrdServer.name[i]).add(message);
                    }
                }
                out.println("Message posted to all users. " );
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + " posts a message for all known users.");
                break;

                //get your messages
                case 6:
                out.println("my messages: ");
                
                for(int i = 0 ; i < SocketThrdServer.table.get(SocketThrdServer.name[myID]).size(); i++)
                    {
                        lenghtString = SocketThrdServer.table.get(SocketThrdServer.name[myID]).size();
                        sentBack += (SocketThrdServer.table.get(SocketThrdServer.name[myID]).get(i) + "\n");
                    }
                
                out.println(lenghtString);
                out.println(sentBack );
                sentBack="";
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + " gets messages.");
                SocketThrdServer.table.get(SocketThrdServer.name[myID]).clear();
                break;

                //exit
                case 7:
                SocketThrdServer.online[myID] = 0;
                System.out.println(time() +" "+ SocketThrdServer.name[myID] + " exits.");
                break;
            }
        }
        catch (IOException e)
        {
                System.out.println("crash");
        }
    }

    //compute the time
    public String time(){
                SimpleDateFormat localDateFormat = new SimpleDateFormat("MM/dd/yy h:mm:ss a");
                String time = localDateFormat.format(new Date());
                return time;
                }
    
    //if the name match
    public boolean match(String line){
        return SocketThrdServer.table.containsKey(line);
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
