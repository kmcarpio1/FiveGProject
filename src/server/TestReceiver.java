package src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import src.config.Configurations;
import src.logger.MyLogger;

public class TestReceiver {
    
    /**
     * This method get a socket.
     * From this socket it will read a message, and depending of the message will have different behaviour.
     * It will either be a message to test the connectin for the first time (or after that the connection has been cut). Then the central machine will send a confirmation message and display that a new connection is up
     * It could also be a message to test a connection that already exists. In that case, the central machine will just send a confirmation message
     * In another case it doesn't do anything.
     * @param socket
     */
    public static void testHandler(Socket socket){
        try(socket;
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){
            String receivedMessage = in.readLine();
            //System.out.println(receivedMessage);
            if(receivedMessage != null){
                if(receivedMessage.equals(Configurations.getFirstTestMessage())){
                    System.out.println("Client connected");
                    MyLogger.writeLog("Client connected");
                    out.println(Configurations.getConnectionConfirmationMessage());
                }
                else if(receivedMessage.equals(Configurations.getTestMessage())){
                    out.println(Configurations.getConnectionConfirmationMessage());
                }
            }

        } 
        catch (SocketTimeoutException e) {
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}
