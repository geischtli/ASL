#!/bin/bash

# Parse the ip of the aws machine
while getopts "a:" opt; do
  case $opt in
    a)
		IP=$OPTARG
		;;
  esac
done

#scp pg_hba.conf sanhuber@dryad12.ethz.ch:/mnt/local/sanhuber/postgres/db
#scp -r -i ../../frankfurt_key.pem . ec2-user@ec2-$IP.eu-central-1.compute.amazonaws.com:/home/ec2-user/
rsync -avze 'ssh -i ../../frankfurt_key.pem' --exclude=.git --exclude=db_starter_package --exclude=logs . ec2-user@ec2-$IP.eu-central-1.compute.amazonaws.com:/home/ec2-user/