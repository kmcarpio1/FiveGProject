package src.hashfunctions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import src.config.PathToFile;

/**
 * The save, load and convert/write to CSV class
 * It's a static class that will allow us to save, load and write the logHashTable
 * Thanks to it, we will not loose the data betwwen the differents uses of the program
 */
public class SaveLogHashTable{

    private static final String FILES_DIR = PathToFile.getPathFile(); //path to the file directory
    private static final String FILES_WRITE_DIR = PathToFile.getPathWrite(); //path to the write file directory
    private static final String FILES_SAVE_DIR = PathToFile.getPathSave(); //path to the saved file directory

    /**
     * this is to make sure the directories are existing
     */
    static {
        // Ensure the directory exists
        try {
            Files.createDirectories(Paths.get(FILES_DIR));
            Files.createDirectories(Paths.get(FILES_WRITE_DIR));
            Files.createDirectories(Paths.get(FILES_SAVE_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to get the savedFile name frome the logHashTableName
     * @param logHashTableName
     * @return
     */
    public static String getSavedNameOfALogHashTable(String logHashTableName){
        return "save" + logHashTableName;
    }


    /**
     * This methods checks if a savedFileName starts with "save", and resturn the rest of the string if it is. It should be a logHashTable name.
     * @param savedFileName
     * @return
     * @throws Exception
     */
    public static String getLogHashTableNameOfASavedFile(String savedFileName) throws Exception{
        if (savedFileName.startsWith("save")) return savedFileName.substring(4);
        else throw new Exception("The savedFile name isn't regulatory. It should start with 'save'.");
        }


    /**
     * static method to save a logHashTable
     * @param table : the logHashTable to save
     * @return the instance of the savedFile
     */
    public static File save(LogHashTable table) throws IOException,Exception {
        String filename = getSavedNameOfALogHashTable(table.getLogHashTableName());
        String filepath = FILES_SAVE_DIR + filename; //we give it a name
        try (FileOutputStream fileOut = new FileOutputStream(filepath); //creating a new file with the name filename, if it already exist, it erase it before recreating it 
            ObjectOutputStream out = new ObjectOutputStream(fileOut)) { // creates an ObjectOutputStream object that is associated with the previously created FileOutputStream. ObjectOutputStream allows you to serialize Java objects
            out.writeObject(table); //write table in the ObjectOutPutStream
            File save = new File(filepath);
            return new File(filepath); // Return the file object directly
        }catch (IOException e) {
            System.err.println("Error saving file: " + filepath);
            throw e; 
        }
    }

    /**
     * Static method to load a LogHashTable
     * @param filename : the filename of the saved LogHashTable
     * @return the loaded LogHashTable object
     * @throws IOException if an I/O error occurs while loading the file
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    public static LogHashTable load(String filename) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(FILES_SAVE_DIR + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (LogHashTable) in.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            throw e; // Re-throw exception after logging
        } catch (IOException e) {
            System.err.println("Error loading file: " + filename);
            throw e; // Re-throw exception after logging
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found while loading file: " + filename);
            throw e; // Re-throw exception after logging
        } catch (ClassCastException e) {
            System.err.println("Class cast error: " + e.getMessage());
            throw e; // Relance l'exception après avoir logué
        }
    }

    /**
     * static method to write a logHashTable to CSV
     * @param filename : the logHashTable to write to CSV
     * @return the name of the loaded LogHashTable
     */
    public static File writeToCSV(LogHashTable table) throws IOException {
        String filename = "tab_" + table.getLogHashTableName();
        String filepath = FILES_WRITE_DIR + filename;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath,true))) {
            // Write the table header
            writer.write("RawData,Hash,Date,IPAddress,logPath");
            writer.newLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Écrire les lignes du tableau
            for (HashTableElement element : table.getLogTable().values()) { //we are writting each element of the LogHashTable in a new line
                writer.write(String.format("%s,%s,%s,%s",
                        element.getRawData(),
                        element.getHash(),
                        element.getTimeReceived().format(formatter),
                        element.getIP(),
                        element.getLogPath()));
                writer.newLine();
            }
            return new File(filepath);
        }catch (IOException e) {
            System.err.println("Error writing file to CSV: " + filepath);
            throw e;
        }
    }

    public static String getFileDir(){
        return FILES_DIR;
    }


}