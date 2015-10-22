#!/bin/bash

# make sure ant is installed
#sudo yum install ant -y

#mkdir -p logs

# Parse the number of clients to be launched
while getopts ":d:c:" opt; do
  case $opt in
    d)
		NUMDBCONNECTIONS=$OPTARG
		;;
	c)
		NUMCLIENTS=$OPTARG
		;;
  esac
done

printf "\nNUMBER OF DB CONNECTIONS: %d\n" "$NUMDBCONNECTIONS"
printf "\nNUMBER OF SENDING CLIENTS: %d\n" "$NUMCLIENTS"

ant -f antBuildDatabaseBaseline.xml -DnumDBConn=$NUMDBCONNECTIONS -DnumClients=$NUMCLIENTS &