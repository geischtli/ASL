#!/bin/bash

# make sure ant is installed
sudo yum install ant -y

mkdir -p logs

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

# we want to run only 1 middleware per machine anyway
# so we can run it synchronously without any problems.
ant -f antBuildMiddleware.xml

# old impl...
#COUNTER=0
#while [  $COUNTER -lt $NUMMIDDLEWARES ]; do
#	ant -f antBuildMiddleware.xml &
#	COUNTER=`expr $COUNTER + 1`
#done