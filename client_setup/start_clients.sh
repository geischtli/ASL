#!/bin/bash

# first delete everything in the experiment log
rm -r /home/ec2-user/ASL/experiment_log/*

# Parse the number of clients to be launched
while getopts "n:m:i:" opt; do
  case $opt in
    n)
		NUMCLIENTS=$OPTARG
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
echo Number of Clients: $NUMCLIENTS
echo Message Size: $MSGSIZE
echo IP of MW: $IP
printf "\n"

ant -f antBuildClient.xml clean jar

COUNTER=0
while [  $COUNTER -lt $NUMCLIENTS ]; do
	ant -f antBuildClient.xml run -Dmsgsize=$MSGSIZE -Dip=$IP &
	COUNTER=`expr $COUNTER + 1`
done
