package src.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import src.config.Configurations;
import src.logger.MyLogger;
import src.threadfunctions.threadManager;

public class FileServer {
    

    public static void start(){ //we have to give this method to a thread
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is listening on port 5000");
            while (true) {
                Socket socket = serverSocket.accept(); // Accept new connection
                socket.setSoTimeout(Configurations.getTimeOut()); // we are adding a timeout
                //System.out.println("A socket has been found");
                MyLogger.writeLog("A socket has been found");
                
                // Submit the task to the thread manager
                threadManager.executeTask(() -> FileReceiver.objectHandler(socket));             // Pass the socket to a new thread for handling
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
