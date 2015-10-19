#!/bin/bash

# Parse the ip of the aws machine
while getopts "a:" opt; do
  case $opt in
    a)
		IP=$OPTARG
		;;
  esac
done

scp -i ../../../frankfurt_key.pem setup_database.sh ec2-user@ec2-$IP.eu-central-1.compute.amazonaws.com:/home/ec2-user/
scp -i ../../../frankfurt_key.pem postgresql-9.4.4.tar.bz2 ec2-user@ec2-$IP.eu-central-1.compute.amazonaws.com:/home/ec2-user/
scp -i ../../../frankfurt_key.pem ../initDatabase.sql ec2-user@ec2-$IP.eu-central-1.compute.amazonaws.com:/home/ec2-user/
scp -i ../../../frankfurt_key.pem setup_mydb.sh ec2-user@ec2-$IP.eu-central-1.compute.amazonaws.com:/home/ec2-user/