
/**
 * Write a description of class Key here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Key implements java.io.Serializable
{
    private final String IP;
    private final String Port;

    /**
     * Constructor for objects of class Key
     */
    public Key(String i, String p)
    {
        IP = i;
        Port = p;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Key)) {
            return false;
        }
        
        Key otherKey = (Key) object;
        if(otherKey.getIP().equals(this.IP) && otherKey.getPort().equals(this.Port) ){
         return true;   
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        int result = 17; // any prime number
        result = 31 * result + Boolean.valueOf(this.IP).hashCode();
        result = 31 * result + Boolean.valueOf(this.Port).hashCode();
        return result;
    }
    
    public String getIP(){
        return IP;
    }
    
    public String getPort(){
        return Port;
    }
}
