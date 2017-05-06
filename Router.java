import java.util.*;
import java.io.*;
import java.net.*;

/**
 * This class will contain all the data that the router needs access to, including the forwarding table and
 * all the necessary code that makes the sockets work. Contains a distance vector and a forwarding table, as well as
 * the sockets that are able to send messages to other routers.
 */
public class Router{
    ServerSocket serversocket;
    Socket connectionSocket;
    InputStream inputStream;
    DataOutputStream outputStream;
    DatagramSocket routerSocket;

    Map<Connection,Integer> distanceVector; //Distance vector containing the set of routers in the network and the calculated distances to each
    Map<Integer, Connection> neighbors;

    /**
     * Constructor for objects of class Router. Needs to read the file with the name given as a parameter 
     */
    public Router(String fileName, Boolean router){
        try{
            FileReader fr = new FileReader(fileName);
            Scanner sc = new Scanner(fr);
            InetAddress thisIp = InetAddress.getByName( sc.next() ); //Skip the first value of the file (the one that just reads "localhost")
            int thisRouterPort =  sc.nextInt(); //Read the int on the first line of the router text file
            routerSocket = new DatagramSocket(thisRouterPort,thisIp);
            
            while( sc.hasNextLine() ){ //For every line of the router.txt file after the first line, create a new socket port
                Connection newConnection;
                String ipString = sc.next();
                InetAddress ip = InetAddress.getByName(ipString); //Convert the string to an InetAddress
                newConnection = new Connection(Integer.parseInt( sc.next()),ip);
                newConnection.setWeight(Integer.parseInt(sc.next()));
                neighbors.put(newConnection.getPortNum(),newConnection);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
    
    /**
     * This method will be used to bundle the distanceVector into a datagram to be sent to another router
     * Remember the constructor for DatagramPacket is (byte[] buf, int length, InetAddress address, int port)
     * Starts by converting the distanceVector to a byte array (serialization). This byte set needs to be deserialized
     * on the other router.
     */
    public void sendDistanceVector(Connection c){
        byte[] payload;
        try{
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(distanceVector);
            payload = byteOut.toByteArray();
            DatagramPacket send = new DatagramPacket(payload,payload.length,c.getIPAddress(),c.getPortNum());
            routerSocket.send(send);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * This method will be used to determine if the distance vector obtained from ReceiveData contains shorter (or greater)
     * path lengths
     */
    public void updateDistanceVector(HashMap<Connection,Integer> newDistanceVector){
        
    }
}
