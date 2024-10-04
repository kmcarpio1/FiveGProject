package src;

import src.sqlfunctions.SqlTable;

public class Clean{
    
    public static void main(String[] args){
        try{

        //DO THE LOOP HERE

        System.out.println("Welcome into the Cleaning function.");
        System.out.println("The database will be cleaned");

        SqlTable.cleanDatabase();
  



  

        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }



}