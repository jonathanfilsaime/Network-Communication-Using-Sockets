/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Scanner;
import java.net.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author jonathan
 */
public class SocketClient {
    
    private String Name;
    private int i;
    public int x;
    String menu =   "1. Display the names of all known users.\n" + "2. Display the names of all currently connected users.\n" + "3. Send a text message to a particular user.\n" +"4. Send a text message to all currently connected users.\n" +"5. Send a text message to all known users.\n" +"6. Get my messages.\n" +"7. Exit. \n";
    
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String temp;
    String line;
    int count = 0;
    int num = 1;
    String pattern;
    Pattern r;
    Matcher m;
    
    
    public void communicate()
    {
	name();

	//Send data over socket
	out.println(getName());
      
	do{
            //Receive text from server
            try
                {
                    if(count == 0)
                    {
                    line = in.readLine();
                    System.out.println("Hi " + line);
                    System.out.print(menu);
                    x = choice();
                    exe(x);
//                    temp = Integer.toString(x);
//                    out.println(temp);
                    }
                    
                    if(count > 0){
                        
                        x = choice();
                        exe(x);
                        
                    }
                    
                    if((isInteger(line)) && (x > 7 || x < 1))
                    {
                        System.out.println("INVALID INPUT");
                    }
                    
                } 
            
            catch (IOException e)
                {
                    System.out.println("Read failed");
                    System.exit(1);
                }

        
        
        count++;
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
//	System.out.print(menu);
	System.out.println("Enter your choice: ");
	Scanner str = new Scanner(System.in);
	String userChoice = str.nextLine();
            
	if(!(isInteger(userChoice)) || Integer.parseInt(userChoice) < 1 || Integer.parseInt(userChoice) > 7)
            {
                return 0;
            }
            
	this.i = Integer.parseInt(userChoice);
	return i;
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
    
    public void exe(int p){
        try{
            switch (p) {
                    
                    case 1:
                    num = 1;
                    temp = Integer.toString(x);
                    out.println(temp);
                    line = in.readLine();
                    
                    pattern = "\\S+";
                    r = Pattern.compile(pattern);
                    m = r.matcher(line);
                    
                    System.out.println("Know users: ");
                    
                    while(m.find( )){
                        System.out.println("\t" + num + ". " + m.group(0) );
                        num++;
                    }
                    
                    break;
                    
                    case 2:
                    num = 1;
                    temp = Integer.toString(x);
                    out.println(temp);
                    line = in.readLine();
                    
                    pattern = "\\S+";
                    r = Pattern.compile(pattern);
                    m = r.matcher(line);
                    
                    System.out.println("Know connected users: ");
                    
                    while(m.find( )){
                        System.out.println("\t" + num + ". " + m.group(0) );
                        num++;
                    }
                    
                    break;
                    
                    case 3:
                    num = 1;
                    temp = Integer.toString(x);
                    out.println(temp);
                    
                    line = in.readLine();
                    System.out.print(line);
                    
                    Scanner nstr = new Scanner(System.in);
                    String strname = nstr.nextLine();
                    out.println(strname);
                    
                    line = in.readLine();
                    System.out.print(line);
                    String strmessage = nstr.nextLine();
                    out.println(strmessage);
                    
                    line = in.readLine();
                    System.out.println(line);
                    
                    break;
                    
                    case 4:
                    num = 1;
                    temp = Integer.toString(x);
                    out.println(temp);
                    
                    line = in.readLine();
                    System.out.print(line);
                    
                    Scanner mstr = new Scanner(System.in);
                    String strmess = mstr.nextLine();
                    out.println(strmess);
                    
                    line = in.readLine();
                    System.out.println(line);
                    
                    break;
                    
                    case 5:
                    num = 1;
                    temp = Integer.toString(x);
                    out.println(temp);
                    
                    line = in.readLine();
                    System.out.print(line);
                    
                    Scanner mstr1 = new Scanner(System.in);
                    String strmess1 = mstr1.nextLine();
                    out.println(strmess1);
                    
                    line = in.readLine();
                    System.out.println(line);
                    break;
                    
                    case 6:
                    break;
                    
                    case 7:
                    System.exit(0);
                    break;
            }
        }
        catch (IOException e)
        {
            System.out.println("crash");
            
        }
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