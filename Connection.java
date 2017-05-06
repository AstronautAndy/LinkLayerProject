import java.util.*;
import java.io.*;
import java.net.*;

/**
 * Connection will be used to store the port number used to connect a router to a neighbor
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Connection{
    private InetAddress ipAddress;
    private Integer portNum;
    private int weight;
    private DatagramSocket socket;
    
    /**
     * Constructor for objects of class Connection
     */
    public Connection(int portNum, InetAddress ipAddress){
        this.portNum = portNum;
        this.ipAddress = ipAddress;
    }
    
    /**
     * sends a packet when called
     */
    public void send(DatagramPacket payload){
        try
        {
            //send bytes out to socket
            socket.send(payload);
        }
        catch(Exception ex){ex.printStackTrace(); }
    }
    
    /**
     * 
     */
    public DatagramPacket receive(){
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
        try
        {
            socket.receive(receivePacket);
        } catch (IOException e) {}
        return receivePacket;
    }
    
    //Getter and setter methods
    public void setWeight(int i){
        weight = i;
    }
    
    public int getWeight(){
        return weight;
    }
    
    public InetAddress getIPAddress(){
        return ipAddress;
    }
    
    public Integer getPortNum(){
        return portNum;
    }
    
    public DatagramSocket getSocket(){
        return socket;
    }
}
