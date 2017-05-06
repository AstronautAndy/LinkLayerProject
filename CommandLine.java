import java.util.*;
/**
 * This class will be used to accept commands from the user's commandline
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CommandLine extends Thread
{
    private Router r;
    
    /**
     * Method needs to handle multiple cases.
     */
    public void run(){
        Scanner sc = new Scanner(System.in);
        String command;
        while(sc.hasNextLine() ){ //Read in code one line at a time from the console
            command = sc.next();
            switch(command) {
                case "PRINT": System.out.println("Printing"); break;
                case "MSG": int port = Integer.parseInt(sc.next());
                            r.sendDistanceVector(r.neighbors.get(port));
                            break;
                case "CHANGE": break;
                default: System.out.println("COmmand not recognized");
            }
        }
    }
    
    /**
     * Constructor for objects of class CommandLine
     */
    public CommandLine(Router r)
    {
        this.r = r;
    }

}
