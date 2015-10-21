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
#rsync -avze 'ssh -i ../../../frankfurt_key.pem' pg_hba.conf ec2-user@ec2-$IP.eu-central-1.compute.amazonaws.com:/home/ec2-user/postgres/db
cp pg_hba.conf /home/ec2-user/postgres/db

#scp postgresql.conf sanhuber@dryad12.ethz.ch:/mnt/local/sanhuber/postgres/db
#rsync -avze 'ssh -i ../../../frankfurt_key.pem' postgresql.conf ec2-user@ec2-$IP.eu-central-1.compute.amazonaws.com:/home/ec2-user/postgres/db
cp postgresql.conf /home/ec2-user/postgres/db