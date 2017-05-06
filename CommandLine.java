import java.util.*;
/**
 * This class will be used to accept commands from the user's commandline
 */
public class CommandLine extends Thread{
    private Router r;
    
    /**
     * Constructor for objects of class CommandLine. Takes in a router as a parameter
     */
    public CommandLine(Router r){
        this.r = r;
    }
    
    /**
     * Method needs to handle multiple cases. Either prints out the current distance, sends a message to another router, or changes the weight of a connection
     */
    public void run(){
        Scanner sc = new Scanner(System.in);
        String command;
        while(sc.hasNextLine() ){ //Read in code one line at a time from the console
            command = sc.next();
            switch(command) {
                // PRINT - print the current node's distance vector, and the distance vectors received from the neighbors
                case "PRINT": System.out.println("Printing"); break;
                // MSG - <dst-ip> <dst-port> <msg> - send message msg to a destination with the specified address
                case "MSG": int port = Integer.parseInt(sc.next());
                            r.sendDistanceVector(r.neighbors.get(port));
                            break;
                // CHANGE <dst-ip> <dst-port> <new-weight> - change the weight between the current node and the specified node to 
                // new-weight and update the specified node about the change
                case "CHANGE": break;
                default: System.out.println("Command not recognized");
            }
        }
    }
}
