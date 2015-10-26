#!/bin/bash

# make sure ant is installed
#sudo yum install ant -y

ant -f antRunLoadGenerator.xml -DnumEntries=500000 -DcontentLenth=200
ant -f antRunLoadGenerator.xml -DnumEntries=1000000 -DcontentLenth=200
ant -f antRunLoadGenerator.xml -DnumEntries=500000 -DcontentLenth=2000
ant -f antRunLoadGenerator.xml -DnumEntries=1000000 -DcontentLenth=2000

chmod 777 messageData_500000_200.dat
chmod 777 messageData_1000000_200.dat
chmod 777 messageData_500000_2000.dat
chmod 777 messageData_1000000_2000.dat