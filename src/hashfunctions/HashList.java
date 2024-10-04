package src.hashfunctions;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * HashList is meant to be a static class containing all known logHashTable and their name in a static HashTable (it lists all the logHashTable used during the run)
 */
public class HashList {
    private static Hashtable<String,LogHashTable> _hashList = new Hashtable<>(); //with the name of the logHashTable and the LogHashTable

    /**
     * Method to add a new logHashTable into _hashList
     * @param name : the name of the logHashTable
     * @param logHashTable : the logHashTable
     */
    public static void addLogHashTable(String name, LogHashTable logHashTable){
        _hashList.put(name, logHashTable);
    }

    /**
     * Method to get the hashList
     * @return _hashList
     */
    public static  Hashtable<String,LogHashTable> getHashList(){
        return _hashList;
    }

    /**
     * Method to get a logHashTable with the name of a logHashTable, from the hashList
     * @param name : name of the logHashTable
     * @return : the corresponding logHashTable, or null
     */
    public static LogHashTable getLogHashTable(String name){
        return _hashList.get(name);
    }

    /**
     * Method to remove a logHashTable with the name of a logHashTable, from the hashList
     * @param name: name of the logHashTable
     */
    public static void removeLogHashTable(String name){
        _hashList.remove(name);
    }

    /**
     * This method cheks if the hashList is correctly initialized (should always be), and if it's not empty
     * @return true if not empty, false if it is
     */
    public static boolean isHashListInitializedAndNotEmpty() {
        return _hashList != null && !_hashList.isEmpty();
    }

    /**
     * This method finds in hashList the logHashTable that correspond to the logPath (if it exists). Usually, it should be only one corresponding, 
     * but in the case where there are multiple, it returns the first one.
     * If there is none, it returns null
     * @param logPath : the logFile path
     * @return the logHashTable associated to only logPath, or null
     */
    public static String findNameWithLogPath(String logPath){
        Enumeration<LogHashTable> values = _hashList.elements();

        while (values.hasMoreElements()){

            LogHashTable logHashTable =  values.nextElement();
            List<String> list = logHashTable.getLogPaths();
            if(list.size() == 1){ //this method only works with logHashTable containing one logPath
                if(list.get(0).equals(logPath)){
                    return logHashTable.getLogHashTableName();
                }
            }
        }
        return null;
    }
    /**
     * This method finds in hashList the logHashTable that correspond to the logPath in logPaths (if it exists). Usually, it should be only one corresponding, 
     * but in the case where there are multiple, it returns the first one.
     * If there is none, it returns null
     * @param logPaths : the list of logFile paths
     * @return the logHashTable associated to all the logPath in logPaths, and only them, or null
     */
    public static String findNameWithLogPaths(List<String> logPaths){
        Enumeration<LogHashTable> values = _hashList.elements();
        while (values.hasMoreElements()){
            LogHashTable logHashTable =  values.nextElement();
            List<String> list = logHashTable.getLogPaths();
            Set<String> set1 = new HashSet<>(list); //we transform the two list in set, to be abble to compare them without caring about the order of the elements, or the repetitions
            Set<String> set2 = new HashSet<>(logPaths);
            if(set1.equals(set2)){
                return logHashTable.getLogHashTableName();
            }
        }
        return null;

    }

    /**
     * This method erase the hashList, and initialize it again, so it's empty
     */
    public static void clean(){
        _hashList = new Hashtable<>();
    }

}
