package src.sqlfunctions;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import src.config.DatabaseConnection;
import src.hashfunctions.HashTableElement;
import src.hashfunctions.LogHashTable;

public class SqlTable {

    //private static List<String> Sqltables = new ArrayList<>();
    
    
    public static PreparedStatement prepareInsertStatement(Connection conn, String tableName) throws SQLException {
        String sql = "INSERT INTO " + getSqlLogHashTableName(tableName) +" "
                   + "(hashKey, rawData, ipAddress, datetime, logFilePath) "
                   + "VALUES (?, ?, ?, ?, ?)";  //it will be the canva for each "line" of our command
        return conn.prepareStatement(sql);
    }



    public static void addElementToThePstmt(HashTableElement hashTableElement, PreparedStatement pstmt) throws  SQLException{
        pstmt.setString(1, hashTableElement.getHash()); 
        pstmt.setString(2, hashTableElement.getRawData()); 
        pstmt.setString(3, hashTableElement.getIP()); 
        pstmt.setString(4, hashTableElement.getTimeReceivedString()); 
        pstmt.setString(5, hashTableElement.getLogPath());
    }

    public static void addPstmtToTheBatch(PreparedStatement pstmt) throws  SQLException{
          pstmt.addBatch();
    }

    public static void addElementToTheBatch(HashTableElement hashTableElement, PreparedStatement pstmt) throws  SQLException{
        addElementToThePstmt(hashTableElement, pstmt); 
        addPstmtToTheBatch(pstmt);       
    }
    
    public static String getSqlLogHashTableName(String logHashTableName){
        String sanitizedTableName = logHashTableName.replaceAll("[^a-zA-Z0-9_]", "_"); //all special characters are prohibed in sql table names
        return sanitizedTableName + "_Entries";   
    }

    public static PreparedStatement createSqlCommand(HashTableElement hashTableElement, String logHashTableName, Connection conn) throws SQLException{
        conn.setAutoCommit(false); //we don't want an auto commit, we will want to check that the entire command is good before commiting and executing it

        try{
            PreparedStatement pstmt = prepareInsertStatement(conn, logHashTableName); //PreparedCommand help us create a secure command effectivly

            addElementToTheBatch(hashTableElement, pstmt);
        
            return pstmt;
        }catch(SQLException e){
            conn.rollback();
            throw e;
        }
    }


    public static PreparedStatement createSqlCommand(LogHashTable logHashTable, Connection conn) throws SQLException{
        String logHashTableName = logHashTable.getLogHashTableName();
        conn.setAutoCommit(false); //we don't want an auto commit, we will want to check that the entire command is good before commiting and executing it

        try{
            PreparedStatement pstmt = prepareInsertStatement(conn, logHashTableName); //PreparedCommand help us create a secure command effectivly

            for(HashTableElement hashTableElement : logHashTable.getLogTable().values()){
                addElementToTheBatch(hashTableElement, pstmt);
            }

            return pstmt;
        }catch(SQLException e){
            conn.rollback();
            throw e;
        }
    }

    public static void executeBatch(PreparedStatement pstmt) throws SQLException {
        pstmt.executeBatch(); // Exécute the batch after adding all the lines
    
    }

    public static void setupDatabase(Connection conn, String logHashTableName) throws SQLException {
        String SqlTableName = getSqlLogHashTableName(logHashTableName);
        String createTableSQL = "CREATE TABLE IF NOT EXISTS "+SqlTableName+" ("
                               + "hashKey VARCHAR(255), "
                               + "rawData TEXT, "
                               + "ipAddress VARCHAR(255), "
                               + "datetime VARCHAR(30), "
                               + "logFilePath VARCHAR(255))";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            //if(!Sqltables.contains(SqlTableName)) Sqltables.add(SqlTableName);
        }catch(SQLException e){
            throw e;
        }
    }


    public static void ConvertAndSendLogHashTableIntoSql(LogHashTable logHashTable) throws SQLException{
        try(Connection conn = DatabaseConnection.getTestConnection()){
            conn.setAutoCommit(false); //we don't want an auto commit, we will want to check that the entire command is good before commiting and executing it

            try{
                setupDatabase(conn, logHashTable.getLogHashTableName());
                PreparedStatement pstmt = createSqlCommand(logHashTable, conn);
                executeBatch(pstmt);

                conn.commit();  // validate the modifications


            }catch(SQLException e){
                conn.rollback();
                throw e;
            }
            //the connexion is automatically closed here, thanks to the try-with-ressourse
        }catch (SQLException e) {
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            throw e;
        }
    }


    public static void ConvertAndSendHashTableElementIntoSql(HashTableElement hashTableElement, String logHashTableName) throws SQLException{
        try(Connection conn = DatabaseConnection.getTestConnection()){
            conn.setAutoCommit(false); //we don't want an auto commit, we will want to check that the entire command is good before commiting and executing it

            try{
                setupDatabase(conn, logHashTableName);
                PreparedStatement pstmt = createSqlCommand(hashTableElement, logHashTableName, conn);
                executeBatch(pstmt);

                conn.commit();  // validate the modifications


            }catch(SQLException e){
                conn.rollback();
                throw e;
            }
            //the connexion is automatically closed here, thanks to the try-with-ressourse
        }catch (SQLException e) {
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            throw e;
        }
    }

    public static List<String> listTables(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        List<String> Names = new ArrayList<>();

        try (ResultSet tables = metaData.getTables(null, null, "%", new String[] {"TABLE"})) {
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                Names.add(tableName);
            }
            return Names;
        }
    }

    public static void cleanDatabase(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            // Récupérer les tables
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    String sql = "DROP TABLE IF EXISTS " + tableName;
                    stmt.executeUpdate(sql);
                    System.out.println("Table " + tableName + " dropped.");
                }
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void cleanDatabase() throws SQLException {
        try(Connection conn = DatabaseConnection.getTestConnection()){
            cleanDatabase(conn);
            //the connexion is automatically closed here, thanks to the try-with-ressourse
        }catch (SQLException e) {
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            throw e;
        }
    }


}

