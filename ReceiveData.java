import java.util.*;
import java.net.*;
import java.io.*;
/**
 * This class will be used to continuously accept any data sent to the given router at any given time. Should
 * use some kind of socket based "listen" command embedded in the Router class.
 */
public class ReceiveData extends Thread{
    private Router r;

    /**
     * Constructor for objects of class ReceiveData
     */
    public ReceiveData(Router r){
        this.r = r;
        
    }

    public void run(){
        while(true){
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
            try{
                r.routerSocket.receive(receivePacket);
                System.out.println("Obtained packet");
                // out of the receivePacket, must get the DV hashmap and the hasmap length
                // deserializeDistanceVectorBytes(receivePacket)
                
                // iterate through the receiced DV hashmap, and place the updated distances of the neighbor to other nodes in its own DV
                
                // based on new neighbor values, update its own DV
                // if the DV is different, send the new one to all its neighbors
            } catch (IOException e) {e.printStackTrace();}
        }
    }
    
    /**
     * Should be used in the ReceiveData thread to pass received data to the Router for the router. Returns a 
     * HashMap<Connection,Integer> that the thread has deserialized from the bytes
     * ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
     * ObjectInputStream in = new ObjectInputStream(byteIn);
     * Map<Integer, String> data2 = (Map<Integer, String>) in.readObject();
     * System.out.println(data2.toString());
     */
    public HashMap<Connection,Integer> deserializeDistanceVectorBytes(byte[] inputBytes){
        HashMap<Connection,Integer> newDistanceVector = null;
        try{
            ByteArrayInputStream byteIn = new ByteArrayInputStream(inputBytes);
            ObjectInputStream in = new ObjectInputStream(byteIn);
            newDistanceVector = (HashMap<Connection,Integer>) in.readObject();
        }catch(Exception ex){ ex.printStackTrace(); }
        r.addNeighborsValues(newDistanceVector);
        return newDistanceVector;
    }    
}
