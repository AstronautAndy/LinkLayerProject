import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
/**
 * This class will contain all the data that the router needs access to, including the forwarding table and
 * all the necessary code that makes the sockets work. Contains a distance vector and a forwarding table, as well as
 * the sockets that are able to send messages to other routers.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Router
{
    ServerSocket serversocket;
    Socket connectionSocket;
    InputStream inputStream;
    DataOutputStream outputStream;

    Map<Connection,Integer> distanceVector; //Distance vector containing the set of routers in the network and the calculated distances to each
    //Map<Connection,Connection> neighbors; //Contains a map of IP addresses neighboring the localhost and their associated connection

    /**
     * Constructor for objects of class Router. Needs to read the file with the name given as a parameter 
     */
    public Router(String fileName, Boolean router)
    {
        try{
            FileReader fr = new FileReader(fileName);
            Scanner sc = new Scanner(fr);
            sc.next(); //Skip the first value of the file (the one that just reads "localhost")
            int thisRouterPort =  sc.nextInt(); //Read the int on the first line of the router text file
            //Initialize the server socket located on this host
            serversocket = new ServerSocket(thisRouterPort); //Create a new server socket belonging to the router
            connectionSocket = serversocket.accept(); //Remember that the .accept() method causes the program to wait until a client has connected to the server 
            inputStream = connectionSocket.getInputStream(); //Initialize input stream
            outputStream = new DataOutputStream( connectionSocket.getOutputStream() ); //Initialize output stream
            
            while( sc.hasNextLine() ){ //For every line of the router.txt file after the first line, create a new socket port
                Connection newConnection;
                Object check = sc.next();
                if(check == "localhost"){ //Ask sadovnik if the way I'm checking the first IP address is correct
                    newConnection = new Connection();
                    newConnection.setSocket( Integer.parseInt( sc.next() )  );
                    newConnection.createLocalSocket();
                    newConnection.setWeight( Integer.parseInt(sc.next() ) ); 
                }
                else{ //Cast check to a InetAddress
                    InetAddress ip = (InetAddress) check;
                    newConnection = new Connection();
                    newConnection.setSocket( Integer.parseInt( sc.next() )  );
                    newConnection.createForeignSocket(ip);
                    newConnection.setWeight( Integer.parseInt(sc.next() ) ); 
                }
                distanceVector.put( newConnection,newConnection.getWeight() );
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    
}
