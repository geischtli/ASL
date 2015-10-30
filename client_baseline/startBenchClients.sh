#!/bin/bash

# make sure ant is installed
sudo yum install ant -y

# Parse the number of clients to be launched
while getopts "n:" opt; do
  case $opt in
    n)
		CLIENTS=$OPTARG
		;;
  esac
done

ant -f antRunBenchClient.xml -DnumClients=$CLIENTS