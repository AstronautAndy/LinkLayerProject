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
    Socket connectionSocket;
	int portStartVal = 5000; //Is the starting port number for the range of ports a router can use. 
    InputStream inputStream;
    DataOutputStream outputStream;

	Map<InetAddress,Integer> distanceVector; //Distance vector containing the set of routers in the network and the calculated distances to each
	Map<InetAddress,Connection> neighbors; //Contains a map of IP addresses neighboring the localhost and their associated connection

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
			//Initialize the socket located on this host
            connectionSocket = new Socket("localhost",thisRouterPort); //Create a new socket belonging to the router
			inputStream = connectionSocket.getInputStream(); //Initialize input stream
			outputStream = new DataOutputStream( connectionSocket.getOutputStream() ); //Initialize output stream
            while( sc.hasNextLine() ){ //For every line of the router.txt file, create a new socket port
                
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    
}
