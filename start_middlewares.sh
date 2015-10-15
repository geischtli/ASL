#!/bin/bash

# Parse the number of clients to be launched
while getopts "n:" opt; do
  case $opt in
    n)
		NUMMIDDLEWARES=$OPTARG
		;;
  esac
done

printf "\n"
echo Number of Middlewares: $NUMMIDDLEWARES
printf "\n"

COUNTER=0
while [  $COUNTER -lt $NUMMIDDLEWARES ]; do
	#ant -f antBuildMiddleware.xml &
	echo ant got called
	COUNTER=`expr $COUNTER + 1`
done
