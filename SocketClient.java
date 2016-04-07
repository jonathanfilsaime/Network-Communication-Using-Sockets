/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Scanner;
import java.net.*;
import java.io.*;
/**
 *
 * @author jonathan
 */
public class SocketClient {
    
    private String Name;
    private int i;
    public int x;
    String menu =   "1. Display the names of all known users.\n" + "2. Display the names of all currently connected users.\n" + "3. Send a text message to a particular user.\n" +"4. Send a text message to all currently connected users.\n" +"5. Send a text message to all known users.\n" +"6. Get my messages.\n" +"7. Exit.";
    
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String temp;
    
   public void communicate()
   {
      name();

      //Send data over socket
      out.println(getName());
      
      do{
        
          
          
          
            //Receive text from server
            try
                {
                String line = in.readLine();
                System.out.println("Text received: " + line);
                } 
            
            catch (IOException e)
                {
                System.out.println("Read failed");
                System.exit(1);
                }

            if(x > 7)
                {
                System.out.println("INVALID INPUT");
                }
          
          x = choice();
          temp = Integer.toString(x);
          out.println(temp);
      
        }while(x !=7);
      
        System.out.println("out");
   }
   
   public void listenSocket(String host, int port)
   {
      //Create socket connection
      try
      {
          socket = new Socket(host, port);
          out = new PrintWriter(socket.getOutputStream(), true);
          in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      } 
      catch (UnknownHostException e) 
      {
          System.out.println("Unknown host");
          System.exit(1);
      } 
      catch (IOException e) 
      {
          System.out.println("No I/O");
          System.exit(1);
      }
   }
    
    //set client name 
    public void name()
    {
        System.out.print("Enter your name: ");
        Scanner str = new Scanner(System.in);
        this.Name = str.nextLine();
    }
    
    //get client name 
    public String getName()
        {
            return Name;
        }
    
    //menu of action choice
    public int choice()
        {
            System.out.print(menu);
            System.out.println("Enter your choice: ");
            Scanner str = new Scanner(System.in);
            String userChoice = str.nextLine();
            this.i = Integer.parseInt(userChoice);
            return i;
        }
    
    
    public static void main(String[] args) 
    {
        
        if (args.length != 2)
        {
            System.out.println("Usage:  client hostname port");
            System.exit(1);
        }

        SocketClient client = new SocketClient();

        String host = args[0];
        int port = Integer.valueOf(args[1]);
        client.listenSocket(host, port);
        client.communicate();
        
    }
}
