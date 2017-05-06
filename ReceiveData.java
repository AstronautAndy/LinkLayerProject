import java.net.*;
import java.io.*;
/**
 * This class will be used to continuously accept any data sent to the given router at any given time. Should
 * use some kind of socket based "listen" command embedded in the Router class.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ReceiveData extends Thread
{
    // instance variables - replace the example below with your own
    private Router r;

    /**
     * Constructor for objects of class ReceiveData
     */
    public ReceiveData(Router r)
    {
        this.r = r;
    }

    public void run(){
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
        try
        {
            r.routerSocket.receive(receivePacket); //Causing a null pointer exception
        } catch (Exception e) {e.printStackTrace();}
    }
}
