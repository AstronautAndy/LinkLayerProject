
/**
 * Write a description of class Run here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Run
{
   
    public static void main(String [] args){
        Router r = new Router("test",true);
        CommandLine cl = new CommandLine(r); 
        cl.start();
    }

   
}
