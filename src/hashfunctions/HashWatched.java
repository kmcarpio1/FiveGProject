

package src.hashfunctions;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * HashWatched is meant to be a static class containing all known logHashTable that are currently watching a logFile and the path of the logFile in a static HashTable 
 * If a logHashTable watch multiple 
 */
public class HashWatched{
    private static  Hashtable<String,List<LogHashTable>> _hashWatch= new Hashtable<>(); //with the path of the logFile and the list of the LogHashTable watching it

    /**
     * Method to add a new logHashTable into _hashWatch
     * @param path : the path to the logFile
     * @param logHashTable : the logHashTable watching the logFile
     */
    public static void addLogHashTable(String path, LogHashTable logHashTable){
        List<LogHashTable> logHashTableList  = _hashWatch.getOrDefault(path, new ArrayList<>());
        logHashTableList.add(logHashTable);
        _hashWatch.put(path, logHashTableList);
    }
    
    public static boolean containsKey(String logPath){
        return _hashWatch.containsKey(logPath);
    }

    /**
     * Method to get the hashWatched
     * @return _hashWatch
     */
    public static  Hashtable<String,List<LogHashTable>> getHashWatched(){
        return _hashWatch;
    }


    /**
     * Method to remove a logHashTable with the name of a logHashTable, from the hashList
     * @param name: name of the logHashTable
     */
    public static void removeLogHashTable(String path){
        _hashWatch.remove(path);
    }

    /**
     * Method to get a logHashTable with the path of the logFile, from the hashWatched
     * @param name : path of the log
     * @return : the corresponding logHashTable, or null
     */
    public static List<LogHashTable> getLogHashTable(String path){
        return _hashWatch.get(path);
    }

    /**
     * This method cheks if the hashWatched is correctly initialized (should always be), and if it's not empty
     * @return true if not empty, false if it is
     */    
    public static boolean isHashWatchedInitializedAndNotEmpty() {
        return _hashWatch != null && !_hashWatch.isEmpty();
    }

    /**
     * This method erase the hashWatch, and initialize it again, so it's empty
     */
    public static void clean(){
        _hashWatch = new Hashtable<>();
    }

}
