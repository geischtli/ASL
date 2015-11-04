#!/bin/bash

# Parse the number of clients to be launched
while getopts "n:m:" opt; do
  case $opt in
    n)
		NUMCLIENTS=$OPTARG
		;;
	m)
		MSGSIZE=$OPTARG
		;;
  esac
done

printf "\n"
echo Number of Clients: $NUMCLIENTS
echo Message Size: $MSGSIZE
printf "\n"

ant -f antBuildClient.xml clean jar

COUNTER=0
while [  $COUNTER -lt $NUMCLIENTS ]; do
	ant -f antBuildClient.xml run -Dmsgsize=$MSGSIZE &
	COUNTER=`expr $COUNTER + 1`
done
