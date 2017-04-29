import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
/**
 * Connection will be used to store the port number used to connect a router to a neighbor
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Connection
{
    private InetAddress ipAddress;
    private int SocketNum;
    private Socket connectionSocket;
    private InputStream inputStream;
    private DataOutputStream outputStream;
    
    /**
     * Constructor for objects of class Connection
     */
    public Connection(int SocketNum)
    {
        this.SocketNum = SocketNum;
    }

    public void createForeignSocket(InetAddress i){
        try{
            connectionSocket = new Socket(i,SocketNum); //Create a new socket belonging to the router
    		inputStream = connectionSocket.getInputStream(); //Initialize input stream
    		outputStream = new DataOutputStream( connectionSocket.getOutputStream() ); //Initialize output stream
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void createLocalSocket(){
        try{
            connectionSocket = new Socket("localhost",SocketNum); //Create a new socket belonging to the router
    		inputStream = connectionSocket.getInputStream(); //Initialize input stream
    		outputStream = new DataOutputStream( connectionSocket.getOutputStream() ); //Initialize output stream
        }catch(Exception ex){
            ex.printStackTrace();
        }
    		
    }
    
}
