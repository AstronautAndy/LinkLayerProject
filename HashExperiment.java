import java.util.*;
/**
 * Write a description of class HashExperiment here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HashExperiment
{
    public static void main(String [] args){
        HashMap<String[],Integer> testMap = new HashMap<String[],Integer>();
        String[] firstKey = {"one","two"};
        String[] falseKey = {"three","four"};
        testMap.put(firstKey,5);
        System.out.println(testMap.containsKey(firstKey));
        System.out.println(testMap.containsKey(falseKey));
    }
}
