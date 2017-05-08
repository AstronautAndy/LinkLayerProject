import java.util.*;
import java.io.*;
import java.net.*;

/**
 * This class will contain all the data that the router needs access to, including the forwarding table and
 * all the necessary code that makes the sockets work. Contains a distance vector and a forwarding table, as well as
 * the sockets that are able to send messages to other routers.
 */
public class Router{
    // new router socket for the class
    DatagramSocket routerSocket;

    // String for the IP the router last received from
    String lastReceivedIP;

    // String for the Port the router last received from
    String lastReceivedPort;

    // The IP for this roouter
    String thisRoutersIP;

    // A HashMap of Key and Integer values. The key applies to a specific IP/port number combination
    HashMap<Key/*destination*/,Integer/*cost to dest.*/> distanceVector;

    // An ArrayList that stores all the direct neighbors of the router
    ArrayList<Key> neighbors;

    // An ArrayList that stores all nodes that this router can theortically reach
    ArrayList<Key> reachableRouters;

    // ArrayList that stores all the sender threads for each of the router's connections 
    ArrayList<SendData> threadList = new ArrayList<SendData>();

    // a table to used to calculate the routers DV the 
    int tableDV[][];

    // a key for this router
    Key thisRouterEntry;

    /**
     * Constructor for objects of class Router. Needs to read the file with the name given as a parameter 
     */
    public Router(String fileName, Boolean router){
        distanceVector = new HashMap<Key,Integer>();
        neighbors = new ArrayList<Key>();
        reachableRouters = new ArrayList<Key>();
        try{
            FileReader fr = new FileReader(fileName); Scanner sc = new Scanner(fr); //Initialize scanner
            InetAddress thisIp = InetAddress.getByName( sc.next() ); //Skip the first value of the file (the one that just reads "localhost")
            int thisRouterPort =  sc.nextInt(); //Read the int on the first line of the router text file
            routerSocket = new DatagramSocket(thisRouterPort,thisIp); // create a new router socket for this IP and port
            thisRoutersIP = thisIp.toString(); // set the value of the IP to a string variable
            thisRouterEntry = new Key(thisIp.toString(),Integer.toString(thisRouterPort)); // create a new Key entry for this router
            neighbors.add(thisRouterEntry); // adds itself to its list of neighbors
            reachableRouters.add(thisRouterEntry); // adds itself to its list of reachable routers
            distanceVector.put(thisRouterEntry,0); // lastly, it puts itself into its own distance vector
            while( sc.hasNextLine() ){ //For every line of the router.txt file after the first line, create a new socket port
                Connection newConnection; // creates a new connection for each line of the file
                String ipString = sc.next(); // gets the next string as the IP as a local variable
                InetAddress ip = InetAddress.getByName(ipString); // Convert the string to an InetAddress
                String portNum = sc.next(); // // gets the next string as the port number as a local variable
                newConnection = new Connection(Integer.parseInt(portNum),ip); // creates a new connection with the local port and IP
                newConnection.setWeight(Integer.parseInt(sc.next())); // sets a weight to the connection
                Key newKey = new Key(ipString,portNum); // creates a new Key based on the same IP and Port #

                //String[] newPair = {ipString,portNum}; // 
                // Prints out the IP and port for the purpose of debugging
                System.out.println("This IP is: " + ipString); 
                System.out.println("This portNum is: " + portNum);     

                distanceVector.put(newKey,newConnection.getWeight()); //Add new neighbor to basic distance vector
                neighbors.add(newKey); // Add new neighbor to set of neighbors  
                reachableRouters.add(newKey); // Add new neighbors to set of reachable routers. Seems redundant, but allows them to be used separately
                SendData sd = new SendData(this,newConnection); // Opens a new SendData thread for this new connection
                threadList.add(sd); // adds the thread to the list of threads 
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        //after adding all SendData threads to the arrayList, iterate through the list and start each at the same time
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
            //System.out.println("Send DV payload size: " + payload.length);
            DatagramPacket send = new DatagramPacket(payload,payload.length,c.getIPAddress(),c.getPortNum());
            routerSocket.send(send);
            out.close();
            byteOut.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Called when the MSG command is used to talk between routers
     */
    public void sendMessage(String Ip, int port, String message){
        try{
            InetAddress destIp = InetAddress.getByName(Ip);
            DatagramPacket sendPacket = new DatagramPacket(message.getBytes(),message.getBytes().length,destIp,port);
            routerSocket.send(sendPacket);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }  

    /**
     * used to update the distance vector when a new DV is received from another router
     */
    public void updateDistanceVector(HashMap<Key,Integer> dv, Key receivedFrom){
        int tmpTable[][];
        // print out if distance vector is empty
        if(dv == null){
            System.out.println("Null distance vector input");
        }
        // check for new reachable destinations from the input dv
        else{
            Iterator it = dv.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry nextEntry = (Map.Entry) it.next();
                Key check = (Key) nextEntry.getKey();
                if(distanceVector.containsKey(check) == false ){ //If the router's distance vector doesn't have the current key
                    //System.out.println("Found unknown key: " + check.getIP() + " " + check.getPort());
                    Integer j = (Integer) nextEntry.getValue();
                    distanceVector.put(check,j);
                    reachableRouters.add(check);
                }
            }
        }

        // set the values of the table to infinity, unless it is the intersection of a node and itself, where you set to zero
        tmpTable = new int[neighbors.size()][reachableRouters.size()];
        for(int i = 0; i < reachableRouters.size(); i++){
            for(int j = 0; j < neighbors.size(); j++){
                if(reachableRouters.get(i).equals(neighbors.get(j))){
                    tmpTable[j][i] = 0;
                }
                else{
                    tmpTable[j][i] = 1000000;
                }
            }
        }

        // fill the rest of the table with this routers distances and the input routers distances
        for(int k = 0; k < reachableRouters.size(); k++){
            for(int l = 0; l < neighbors.size(); l++){
                // set the routers list of neighbor weights into the table
                if(neighbors.get(l).equals(thisRouterEntry)){
                    if(reachableRouters.get(k).equals(neighbors.get(l))){
                        tmpTable[l][k] = distanceVector.get(neighbors.get(k));
                    }
                }
                // set the input dv's weights into the table
                if(neighbors.get(l).equals(receivedFrom)){
                    if(reachableRouters.get(k).equals(neighbors.get(l))){
                        tmpTable[l][k] = dv.get(neighbors.get(l));
                    }
                }                
            }
        } 
        
        for(int n = 0; n < neighbors.size(); n++){
            int min = 1000000;
            int minIndex = 0;
            for(int m = 0; m < reachableRouters.size(); m++){
                if(distanceVector.get(neighbors.get(n)) + tmpTable[n][m] < min){
                    min = distanceVector.get(neighbors.get(n)) + tmpTable[n][m];
                    minIndex = m;
                }
            }
            distanceVector.remove(reachableRouters.get(minIndex));
            distanceVector.put(reachableRouters.get(minIndex),min);
        }
    } 

    /**
     * Prints the table used to calculate the DV
     */
    public void printTable(int table[][]){
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }    
    }    

    /**
     * Prints the routers current distance vector
     */
    public void printDistanceVector(){
        Iterator it = distanceVector.entrySet().iterator();        
        while(it.hasNext()){
            Map.Entry nextEntry = (Map.Entry) it.next();
            Key check = (Key) nextEntry.getKey();
            System.out.println(check.getIP() + " " + check.getPort() + " " + nextEntry.getValue());
        }
    }
}
