#!/bin/bash

# make sure ant is installed
sudo yum install ant -y

# renew logs folder
rm -rf /home/ec2-user/ASL/logs
mkdir /home/ec2-user/ASL/logs