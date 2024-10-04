# Makefile for compiling Java files and running the main class

# Compiler
JC =javac

#Flags
JFLAGS = -d dist -cp "lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar"

#Source directories
SRCDIRS = src \
					src/hashfunctions \
					src/commands \
					src/logger \
					src/config \
					src/sqlfunctions \
					src/threadfunctions \
					src/server

TSTDIRS = tst \
					tst/hashing \
					tst/commands \
					tst/sqlfunctions

ADMINER = adminer \


# Source files
SRCS = $(wildcard $(addsuffix /*.java,$(SRCDIRS)))
MCKS = $(wildcard $(addsuffix /*.java,$(TSTDIRS)))




# Phony targets
.PHONY: all clean run doc


# Default target
all: create_files create_config
	$(JC) $(JFLAGS) $(SRCS) $(MCKS) 

create_files:
	mkdir -p files/save files/write logfile

create_config:
	touch src/config/config.ini

# Run the demo1 class
demo1: create_logfile all 
	java -cp dist:lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar src.Demo1 $(ARGS)

# Run the demo2 class
demo2: create_logfile all
	java -cp dist:lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar src.Demo2 $(ARGS)

# Run the demo3Machine class
Demo3Machine: all
	java -cp dist:lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar src.Demo3Machine $(ARGS)

# Run the demo3Server class
Demo3Server: all
	java -cp dist:lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar src.Demo3Server $(ARGS)


# Run the MainMachineManual class
MainMachineManual: all
	java -cp dist:lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar src.MainMachineManual $(ARGS)


# Run the MainMachineAutomatic class
MainMachineAutomatic: all
	java -cp dist:lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar src.MainMachineAutomatic $(ARGS)


# Run the MainServer class
MainServer: all
	java -cp dist:lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar src.MainServer $(ARGS)


# Run the Manual class
Manual: all
	java -cp dist:lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar src.Manual $(ARGS)



# Launches tests
test: all
	java -ea -cp dist:lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar tst.Test $(ARGS)
	
# clean database
clean_database: all
	java -ea -cp dist:lib/commons-cli-1.6.0.jar:/usr/share/java/mysql-connector-java-9.0.0.jar src.Clean $(ARGS)
	


# Clean port before starting
clean_port:
	./close_port.sh



# Clean target
clean: 
	$(RM) $(shell find ./dist -name "*.class")
	$(RM) ./files/save/*
	$(RM) ./files/write/*
	$(RM) ./logfile/logfile.log 
	$(RM) ./logfile/logfile_other.log 

# Clean the logger
clean_logger:
	$(RM) ./src/logger/log.log 

# Clean everything
clean_all: clean_database clean_logger clean_port clean

#create the demo logfiles
create_logfile:
	touch logfile/logfile.log
	touch logfile/logfile_other.log

#to launch a server to watch the database
display_database:
	./display_database.sh