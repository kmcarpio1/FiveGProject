package src.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.UUID;
import src.commands.CommandCutter;
import src.commands.CommandFactory;
import src.commands.ICommand;
import src.config.Configurations;
import src.config.PathToFile;
import src.logger.MyLogger;

public class FileReceiver {

    private static final String SAVE_DIR = PathToFile.getPathSave();


    public static String generateUniqueFilename(String directory) {
        String filename;
        File file;

        do {
            filename = UUID.randomUUID().toString() + ".ser";
            file = new File(directory, filename);
        } while (file.exists()); 

        return filename;
    }


    public static String convertObject(Socket socket) throws IOException,Exception{
        String filename = generateUniqueFilename(SAVE_DIR);
        try( //this is a try with ressourse, so the element can close on their own
            InputStream socketIn = socket.getInputStream();
            FileOutputStream fileOut = new FileOutputStream(SAVE_DIR + filename);
            BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut);
        ){

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = socketIn.read(buffer)) != -1) {
                bufferedOut.write(buffer, 0, bytesRead);
            }

            sendConfirmation(socket); //we are sending a confirmation message. To be sure that there will be no problem, this method can't create an exception. So, if there is a problem when sending the message, the rest of the code will still work normally

            //we don't need to flush, because at the end of the try, bufferOut will be closed, and so flushed too
            return filename;
        } 
    }

    /**
     * This method is meant to check if there is only one loaded file and catch his name
     * @param createdNames
     * @return
     * @throws Exception
     */
    public static String getOneCreatedName(List<String> createdNames)throws Exception{
        if(createdNames.size() != 1) throw new Exception("Wrong Number of loaded Files");
        else return createdNames.get(0);
    }

    public static String loadingObject(String saveFileName) throws Exception {
        try {

            CommandCutter commandCutted = new CommandCutter("load "+saveFileName); //we are cutting the command in differents parts to analyse it better
            ICommand command = CommandFactory.createCommand(commandCutted); //it gaves us the good type of command, we just need to execute it
            command.execute(); //now the logHashTable should be loaded

            return getOneCreatedName(command.getCreatedNames());

        } catch (Exception e) {
            //System.err.println("Error loading file: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public static void sendingObjectInDatabase(String logHashTableName) throws Exception{
        try {

            CommandCutter commandCutted = new CommandCutter("send "+logHashTableName); //we are cutting the command in differents parts to analyse it better
            ICommand command = CommandFactory.createCommand(commandCutted); //it gaves us the good type of command, we just need to execute it
            command.execute(); //now the logHashTable should have been converted and sent into the database
            //System.out.println("object sent on "+command.getCreatedNames());
            MyLogger.writeLog("object sent on "+command.getCreatedNames());
        } catch (Exception e) {
            //System.err.println("Error sending logHashTable into the Database: " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    private static void sendConfirmation(Socket socket){

        try( //this is a try with ressourse, so the element can close on their own
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true)
        ){
            socketOut.println(Configurations.getReceptionConfirmationMessage());
        } catch (Exception e) {
            System.err.println("Impossible to send the confirmation message: "+e.getMessage());
        }
    }

    public static void objectHandler(Socket socket){
        try {
            String savedFileName = convertObject(socket);
            String logHashTableName = loadingObject(savedFileName);
            sendingObjectInDatabase(logHashTableName);
        }  catch (IOException e) {
            System.err.println("IO error occurred in FileReceiver: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred in FileReceiver: " + e.getMessage());
        }finally { //to make sure that the socket has been closed
            try {
                if (!socket.isClosed()) {
                    socket.close(); // Close the socket in the finally block to ensure it's always closed
                }
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

}
