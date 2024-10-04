package src.commands;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import src.hashfunctions.HashList;
import src.hashfunctions.LogHashTable;
import src.sqlfunctions.SqlTable;

/**
 * The "send" command name class
 * Send is supposed to convert a logHashTable into sql and send it into the database, then clean it (we don't want to send the same informations twice).
 * The logHashTable are listed into the HasList.
 */
public class Send extends  ICommand {
        /*
     * In the command we will have a string
     * It's either all (and it will convert and send all the logHashTable in HashList and send them) or the name of a logHashTable or the logPath
    */

    private String _name; //the third word of the command (either "all", or the path to a logFile, or the name of the LogHashTable or to a saved LogHashTable)
    private LogHashTable _logHashTable = null; //the stored logHashTable, if the second argument is saved, or if the third is all or if it's an incorrect command it stays null

    /**
     * Constructor of the write command instance.
     * It checks if there are the right amount of element, and if they are valid (if the second is either "listed" or "saved", and if the third is accepted)
     * You can find the acceptance at the beginning of the class
     * @param command
     * @throws Exception
     */
    Send(CommandCutter command) throws Exception{
        if(command.size() != 2) throw new Exception("Wrong command: wrong number of arguments");

        _name = command.getArg(1);

        if(!_name.equals("all")){ //if it's listed and not "all", the name is either a logHashTable name listed in the hashTableList or a logFile path
            Hashtable<String,LogHashTable> hashList = HashList.getHashList();
            if(!hashList.containsKey(_name)){ //we are looking if it's a logHashTable name listed in hashList, if not it could be a logFile path
                String name = HashList.findNameWithLogPath(_name); //we check if there is a logHashTable with the coresponding logFile path
                if(name == null){ //if not, wrong argument
                    throw new Exception("Wrong command: wrong argument");
                }
                else{ //else, good argument, we change _name with the logHashTable name, and store the logHashTable
                    _name = name;
                    _logHashTable = hashList.get(_name);
                }
            }
            else{ //else, good argument, we change _name with the logHashTable name, and store the logHashTable
                _logHashTable = hashList.get(_name);
            }
        }
    }

    /**
     * @return the name of the second word of the command
     */    
    @Override
    public String getArgument() {
        return _name;
    }



    private void sendAFile(LogHashTable logHashTable) throws Exception{
        if(!logHashTable.isEmpty()){
            SqlTable.ConvertAndSendLogHashTableIntoSql(logHashTable);      //convert into sql and send it to the database
            addCreatedName(SqlTable.getSqlLogHashTableName(logHashTable.getLogHashTableName()));
            logHashTable.clean(); //and we clean it
        }
    }

    /**
     * Execute the command by converting the logHashTable(s) into sql and sending the conversion(s) into the database.
     * If it's not possible (for example if there is a connexion problem with the database), it throws an error
     */
    @Override
    public void execute() throws Exception{
        try{
            if(_name.equals("all")){ //if it's all we just have to go threw all the hashList elements
                Hashtable<String,LogHashTable> hashList = HashList.getHashList();
                Enumeration<LogHashTable> values = hashList.elements();
                while (values.hasMoreElements()){ //while there are still elements in HashList
                    LogHashTable logHashTable =  values.nextElement(); 
                    sendAFile(logHashTable);
                }

            }

            else{ //if it's not "all", it's the name of a logHashTable
                if (_logHashTable!=null) { 
                    sendAFile(_logHashTable);
                } else {
                    throw new Exception("Wrong command: logHashTable doesn't exist");
                }
            }
        }catch(SQLException e){
            throw new Exception("SQL error : "+ e.getLocalizedMessage() );
        }
    }
}



