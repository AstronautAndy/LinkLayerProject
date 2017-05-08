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
            //System.out.println("Command is: " + command);
            // PRINT - print the current node's distance vector, and the distance vectors received from the neighbors
            System.out.println();
            if("PRINT".equals(command)){
                r.printDistanceVector();
            }
            // MSG - <dst-ip> <dst-port> <msg> - send message msg to a destination with the specified address
            else if("MSG".equals(command)){
                String tmpIP = sc.next();
                String tmpPN = sc.next();
                String msg = sc.next();
                int port = Integer.parseInt(tmpPN);
                r.sendMessage(tmpIP,Integer.parseInt(tmpPN),msg); 
            }
            // CHANGE <dst-ip> <dst-port> <new-weight> - change the weight between the current node and the specified node to 
            // new-weight and update the specified node about the change
            else if("CHANGE".equals(command)){
                String tmpIP = sc.next();
                String tmpPN = sc.next();
                String tmpWT = sc.next();
                // change the distance vector here
                Key check = new Key(tmpIP,tmpPN);
                r.distanceVector.remove(check);
                r.distanceVector.put(check,Integer.parseInt(tmpWT));
            }
            else{
                System.out.println("Unknown Command: " + command);
            }
            System.out.println();
        }
    }
}
