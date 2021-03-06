#!/bin/bash

# make sure ant is installed
sudo yum install ant -y

# Parse the number of clients to be launched
while getopts ":n:m:" opt; do
  case $opt in
    n)
		CLIENTS=$OPTARG
		;;
	m)
		NUMREQUESTS=$OPTARG
		;;
  esac
done

ant -f antRunBenchClient.xml -DnumClients=$CLIENTS -DnumRequests=$NUMREQUESTS