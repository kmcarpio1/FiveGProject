package src.threadfunctions;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class threadManager {
    private static ThreadPoolExecutor executor;

    public static void initialize(){
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    }

    public static void executeTask(Runnable task){
        try{
            if(executor != null){
                executor.execute(task);
           }
           else {
                initialize();
                //throw new Exception("The thread management hasn't been initialized");
           }        
        }catch(Exception e){
            System.err.println("An error occurred when trying to give a task to a new thread: " + e.getMessage());
        }  
    }


}
