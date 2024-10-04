package src.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import src.config.Configurations;
import src.threadfunctions.threadManager;

public class TestServer {
    
    /**
     * this method is retrieving the sockets frome the other machines to test the connection.
     * It will attribute them each to a thread to take care of it.
     */
    public static void start(){ //we have to give this method to a thread
        int test_port = Configurations.getTestPort();
        try (ServerSocket serverSocket = new ServerSocket(test_port)) { //we create a server socket
            System.out.println("test Server is listening on port "+test_port);
            while (true) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(Configurations.getTimeOut()); // we are adding a timeout
                //System.out.println("A test socket has been found");

                // Submit the task to the thread manager
                threadManager.executeTask(() -> TestReceiver.testHandler(socket)); //a thrread will run the behaviour for the socket on this port
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
