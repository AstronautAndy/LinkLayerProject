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

    public void run() throws NullPointerException{
        while(true){
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
            try{
                r.routerSocket.receive(receivePacket);
                //System.out.println("Obtained packet");
                // out of the receivePacket, must get the DV hashmap and the hasmap length
                try{
                    HashMap<Key,Integer> newDV = deserializeDistanceVectorBytes(receivePacket.getData());
                    r.updateDistanceVector(newDV);
                }catch(Exception ex){ex.printStackTrace(); }
                // iterate through the receiced DV hashmap, and place the updated distances of the neighbor to other nodes in its own DV
                // based on new neighbor values, update its own DV
                // if the DV is different, send the new one to all its neighbors
            } catch (IOException e) {e.printStackTrace();}
        }
    }
    
    /**
     * Should be used in the ReceiveData thread to pass received data to the Router for the router. Returns a 
     * HashMap<Connection,Integer> that the thread has deserialized from the bytes
     */
    public HashMap<Key,Integer> deserializeDistanceVectorBytes(byte[] inputBytes) throws EOFException{
        HashMap<Key,Integer> newDistanceVector = null;
        try{
            ByteArrayInputStream byteIn = new ByteArrayInputStream(inputBytes);
            ObjectInputStream in = new ObjectInputStream(byteIn);
            newDistanceVector = (HashMap<Key,Integer>) in.readObject(); //Causing EOF error
            in.close();
            byteIn.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        r.addNeighborsValues(newDistanceVector);
        return newDistanceVector;
    }    
}
