/**
 * Sends the Distance vector to each neighbor every 30 seconds.
 */
public class SendData extends Thread{
    // this is the router the thread spawns from
    private Router senderRouter;
    // this is the Connection that the sender has. There is one of these SendData threads per connection the sender has.
    private Connection receiverConnection;
    /**
     * Constructor for objects of class SendData
     */
    public SendData(){

    }

    /**
     * The constructor takes in a router and connection to set as the sender and receiver.
     */
    public SendData(Router r, Connection c){
        senderRouter = r;
        receiverConnection = c;
    }

    /**
     * Simply send the routers DV to the other guy at the other end of the connection, then wait 30 seconds to do it again.
     */
    public void run(){
        //System.out.print("Sending data");
        try{
            // wait 30000 ms or 30 s to send the DV
            Thread.sleep(1000);
            // send the DV
            senderRouter.sendDistanceVector(receiverConnection);
        }
        catch (InterruptedException interruptedException){
            System.out.println( "Second Thread is interrupted when it is sleeping" +interruptedException);
        }        
    }
}
