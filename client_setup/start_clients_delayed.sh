#!/bin/bash

# Parse the number of clients to be launched
while getopts "s:e:m:i:" opt; do
  case $opt in
    s)
		STARTCLIENTS=$OPTARG
		;;
	e)
		ENDCLIENTS=$OPTARG
		;;
	m)
		MSGSIZE=$OPTARG
		;;
	i)
		IP=$OPTARG
		;;
  esac
done

printf "\n"
echo Starting number of Clients: $STARTCLIENTS
echo Ending number of Clients: $ENDCLIENTS
echo Sleep time: 20 seconds
echo Increment per sleep time: 5 Clients
echo Message Size: $MSGSIZE
echo IP of MW: $IP
printf "\n"

ant -f antBuildClient.xml clean jar

COUNTER=0
UPPERBOUNDCLIENTS=$STARTCLIENTS
while [  $COUNTER -lt $ENDCLIENTS ]; do
	while [ $COUNTER -lt $UPPERBOUNDCLIENTS ]; do
		ant -f antBuildClient.xml run -Dmsgsize=$MSGSIZE -Dip=$IP &
		COUNTER=`expr $COUNTER + 1`
		echo $COUNTER
	done
	UPPERBOUNDCLIENTS=`expr $UPPERBOUNDCLIENTS + 5`
	sleep 20
done
echo done