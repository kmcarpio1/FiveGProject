Towards the developpent of a digital forensic readiness model for 5g nfv environments : DATA COLLECTION



## DESCRIPTION

This project is part of a larger project. It will later be attached to a simulated environment of 5G networks and artificial intelligence which will sort the data.
The goal of this program is to collect logs from different logFiles on several machines, encapsulate them, send them to a central machine which itself will store them in a MySQL database.




## Main Features

-Monitors log files in real time.
-Generates and stores information in the form of hashtables.
-Exports the collected data in JSON format.
-Allows files to be sent between machines for analysis.
-Convert the data in SQL command and send them in a Database.
-Checks the connection between the different machines and adopts appropriate behaviors to avoid loss of information
-Handling JSON files with multiple commands.
-Implementation of an automatic mode and a manual mode.


## Prerequisites

-coded with java 17.0.12
-MySQL on the center machine
-Creating a database on the center machine called "FiveGProjectDatabase", and update the username and password in DatabaseConnection.



## Utilisation 

## 1. Automatic mode

## 1.1 The center machine 

#launch the semi_Manual mode
make MainServer

The center machine is just listening on the ports 5000 and 6000, waiting either for JSON package to work with and send to the database, or connection checks.

## 1.2 The other machines

#launch the semi_Manual mode
make MainMachineAutomatic

The other machines, on another hand, will try at first to find their own IP address thanks to the interface used usually in openstack.
If the programm do not found it, it will be asked to the user to enter manually the IP adress of the machine.
Then, the programm will look in the file "config.ini" for the IP address of the center machine. If it's not working, or if it doesn't found any, it will again ask to the user to enter manually the center machine IP, and will modify "config.ini" if the IP is correct.

After all that, they will ask to the user the different logFIle/logDir they want the machine to watch.
When the user is done, simply type "done". 
If no correct logfile have been entered/found, the command "done" will be denied.

Finally, the code will start.
Multiple threads will work at the same time.
Some will constantly checks the logFiles to detect any new logs, and other will pack, save and send the collected data to the center machine every X seconds.
Before packing and sending the data, there is a connection test with the center machine.
If the connection has been cut, the behaviour changes, and the programm only collect the data without sending it, unless the connection has been restored automatically.

## 2. Semi Manual mode

#launch the semi_Manual mode
#on the center machine
make MainServer

#on the other machine
make MainMachineManual

It's more or less the same thing.
The central machine has the same behavior.
The other machines will have the same behavior of searching/requesting IP addresses, but the rest is done manually (see next part)

## 3. Manual mode

#launch the Manual program :
make Manual

The manual mode is not meant to be use with multiple machines, but with only one. It's more a debugging option, to understand how everything is working.

it is based on the use of commands in the terminal.

Here they are and here is their explanation :

- "exit"/"quit" : to exit the programm

- "watch" : to start "watching" a logFile, to collect and store the logs written inside into a "logHashTabl" object.
It's a 3 or 4 words command.
There are multiple options : 
   * "listed", then either "all" or the name of a listed logHashTable (it means a logHashTable that has been stored into the hashTable hashList), or the path to the only logFile that the logHashTable is linked and add it/them into HashWatched (meaning we watch to watch this/these logHashTable(s)). 
   * "saved", then either "all" or the name of a save logHashTable file, meaning we want to load it/them, add them into hashList and HashWtached.
   * "logFile" then either a path to a directory or a logFile, meaning we want te create a/multiple logHashTable each linked to one of the logFile, add them to hashList and to HashWatched (meaning we watch to watch this/these logFile(s)).
   If it's a directory, it will create as much logHashTable as the number of file in the directory, except if we add "mixed" after
"watch listed [all/$logHashTable_name]"
"watch saved [all/$saved_logHashTable_name]"
"watch logFile [$path_to_logFile$/path_to_logDir] [/mixed]"


- "save" : save is supposed to save a LogHashTable into a JSON file.
It's a 2 or 3 words command.
After "save ":
   * It's either "all", meaning we want to save all the logHashTable files listed in the hashList
   * Or either the path to the  LogFile, in that case we save the loghashtable watching it (if it exists an it is currently watched (in hashwatched))
   * Or even the name of a logHashTable, in that case we save it
   * If there is a third word, it should be "clean_on_save" and it will clean the logHashTables when they are saved.
"save [all/$path_to_logFile/$loghashTable_name] [/clean_on_save]"

- "load" : Load is supposed to load a saved LogHashTable
It's a 2 word command.
After "load " :    
   * It's either "all", meaning we want to load all the saved logHashTable files
   * Or either the path to the saved LogHashTable, in that case we load it
"load [$path_to_a_saved_logHashTable/all]"

- "write" : write is supposed to write a LogHashTable for the user to be able to read it (it's like it was written in the database, but in a text file)
It's a 3 word command.
After "write " :    * In the command we will have a string
   * It's either "listed ", and then either "all", the name of a logHashTable, or the path to a logFile (that is watched by a logHashTable)
   * Or either "saved ", followed by "all" or a savedLogHashTable name (in this case, it will load it to write it)
"write listed [all/$logHashTable_name/$path_to_watched_logfile]"
"write saved [all/$savedLogHashTable_name]"

- "mix" : mix is supposed to mix all the listed/saved LogHashTables together and store the reult in the HashList hashTable.
It's a 2 word command.
After "mix " :    
   * It's either "listed", a new logHashTable will be created by merging all the listed logHashTable (they are not deleted in the process)
   * Or either "saved", a new logHashTable will be created by loading and merging all the saved logHashTable.
"mix [listed/saved]"

- "pass" : pass is supposed to send a saved logHashTable to the center machine (or another machine in the manual mode), and delete it.
It's a 3 word command.
After "pass ", it is the IP of the center machine (in the program it is automatically the IP of the connected center machine, but it could be use in other cases with other IP addresses).
For the third word : 
   * It's either "all", and then all the saved logHashTable are sent to the center machine 
   * Or a saved logHashTable name, it is then sent
"pass $IP_ADDRESS [all/$savedLogHashTable_name]"

- "send" : send is supposed to convert to SQL and send the logHashTable on the Database.
It's a 2 word command.
After "send " :
   * It's either "all", and then all the listed logHashTable are converted and sent
   * Or a the name of a logHashTable, or its corresponding logPath, it is then converted and sent
"send [all/$logHashTable_name/$path_to_logPath]"


## Demo mode

#Run the demo1 class
make demo1

#Run the demo2 class
make demo2

#Run the demo3Server class (on the center machine)
make Demo3Server

#Run the demo3Machine class (on the other machines)
make Demo3Machine

## launch Tests
make test

## Cleanings

#clean database
make clean_database

#Clean port before starting
make clean_port

#Clean target
make clean

#Clean the logger
make clean_logger

#Clean everything (BE CAREFULL: the database will be cleaned)
make clean_all

## other makefile command

#Create the demo
make create_logfile

#setup the system
make all

## Dipsplay the database online
make display_database


## Configuration

You need to create a database on the center machine named "FiveGProjectDatabase", and change the username and password in "DatabaseConnection.java" according to your computer.

IF POSSIBLE, one day, change the database for an online database. Some part of the code will need to be change for that.

Be carefull that the computer has the reading permissions
if you don't have it 
sudo chmod +r $path_to_logfile 


## Credit

MORENO CARPIO Kenzo, intern at UP for 3 months in 2024.

have fun 
