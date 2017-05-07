import java.util.*;
import java.io.*;
import java.net.*;

/**
 * This class will contain all the data that the router needs access to, including the forwarding table and
 * all the necessary code that makes the sockets work. Contains a distance vector and a forwarding table, as well as
 * the sockets that are able to send messages to other routers.
 */
public class Router{
    DatagramSocket routerSocket;

    HashMap<Key,Integer> distanceVector;
    // Distance vector containing the set of routers in the network and the calculated distances to each

    HashMap<Connection,Integer> updateDV;
    
    ArrayList<Connection> neighbors;
    ArrayList<SendData> threadList = new ArrayList<SendData>();

    /**
     * Constructor for objects of class Router. Needs to read the file with the name given as a parameter 
     */
    public Router(String fileName, Boolean router){
        distanceVector = new HashMap<Key,Integer>();
        neighbors = new ArrayList<Connection>();
        try{
            FileReader fr = new FileReader(fileName); Scanner sc = new Scanner(fr); //Initialize scanner
            InetAddress thisIp = InetAddress.getByName( sc.next() ); //Skip the first value of the file (the one that just reads "localhost")
            int thisRouterPort =  sc.nextInt(); //Read the int on the first line of the router text file
            routerSocket = new DatagramSocket(thisRouterPort,thisIp);
            //Add connection to itself to DistanceVector
            Key thisRouterEntry = new Key(thisIp.toString(),Integer.toString(thisRouterPort));
            distanceVector.put(thisRouterEntry,0);
            while( sc.hasNextLine() ){ //For every line of the router.txt file after the first line, create a new socket port
                Connection newConnection;
                String ipString = sc.next();
                InetAddress ip = InetAddress.getByName(ipString); //Convert the string to an InetAddress
                String portNum = sc.next();
                newConnection = new Connection(Integer.parseInt(portNum),ip);
                newConnection.setWeight(Integer.parseInt(sc.next()));
                neighbors.add(newConnection); //Add new neighbor to set of neighbors
                Key newKey = new Key(ipString,portNum);
                String[] newPair = {ipString,portNum};
                distanceVector.put(newKey,newConnection.getWeight()); //Add new neighbor to basic distance vector
                SendData sd = new SendData(this,newConnection);
                threadList.add(sd);
            }
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        //after adding all SendData threads to the arrayList, iterate through the list and run each
        for(int i=0; i<threadList.size(); i++){
            threadList.get(i).start();
        }
    }
    
    /**
     * This method will be used to bundle the distanceVector into a datagram to be sent to another router
     * Remember the constructor for DatagramPacket is (byte[] buf, int length, InetAddress address, int port)
     * Starts by converting the distanceVector to a byte array (serialization). This byte set needs to be deserialized
     * on the other router.
     */
    public void sendDistanceVector(Connection c){
        System.out.println("Sending Vector");
        byte[] payload;
        try{
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(distanceVector);
            payload = byteOut.toByteArray();
            System.out.println("Send DV payload size: " + payload.length);
            DatagramPacket send = new DatagramPacket(payload,payload.length,c.getIPAddress(),c.getPortNum());
            routerSocket.send(send);
            out.close();
            byteOut.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
    }
    
    /**
     * This method will be used to determine if the distance vector obtained from ReceiveData contains shorter (or greater)
     * path lengths
     */
    public void compareDistanceVector(HashMap<Key,Integer> newDistanceVector){
        // compares new and old DV's 
        // if the new one is different, it will immediately be sent to all neighbors. Else, nothing will happen and the old DV will be resent to its neighbors 
        // at the regular time intervals
    }
    
    /**
     * 
     */
    public void updateDistanceVector(HashMap<Key,Integer> dv){
        if(dv == null){
            System.out.println("Null distance vector input");
        }
        else{
            Iterator it = dv.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry nextEntry = (Map.Entry) it.next();
                Key check = (Key) nextEntry.getKey();
                if(distanceVector.containsKey(check) == false ){ //If the router's distance vector doesn't have the current key
                    System.out.println("Found unknown key: " + check.getIP() + " " + check.getPort());
                    Integer j = (Integer) nextEntry.getValue();
                    distanceVector.put(check,j);
                }
            }
        }
        // update the actual distance vector
        // call compareDistanceVector to compare new and old DV's
    }
    
    /**
     * Looks at the values of a 
     */
    public void addNeighborsValues(HashMap<Key,Integer> newDistanceVector){
        
        // add the values form dv to the routers actual distance vector
        // call updateDistanceVector to update the actual distance vector based on the new values
    }    
}
