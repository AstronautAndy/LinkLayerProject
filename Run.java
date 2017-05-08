/**
 * Starts up the program for each router. Creates a instance of each router, opens a terminal for it, and creates a new receive data thread.
 */
public class Run{
    public static void main(String [] args){
        Router r = new Router(args[0],true);
        CommandLine cl = new CommandLine(r); 
        ReceiveData rd = new ReceiveData(r);
        cl.start();
        rd.start();
    }
}
