#!/bin/bash

# Parse the number of clients to be launched
while getopts "n:" opt; do
  case $opt in
    n)
		NUMCLIENTS=$OPTARG
		;;
  esac
done

printf "\n"
echo Number of Clients: $NUMCLIENTS
printf "\n"

COUNTER=0
while [  $COUNTER -lt $NUMCLIENTS ]; do
	ant -f antBuildClient.xml 
	let COUNTER=COUNTER+1
done
