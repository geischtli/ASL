#!/bin/bash

#ARGUMENTS
# $1 describes number of entries: {500K, 1M}
# $2 describes length of content in each entry: {200, 2000}

# Fill the message table with a prepared load of entries, such we are able
# to benchmark the performance under a certain amount of existing data.
#/home/ec2-user/postgres/bin/psql -U postgres -d mydb -c 'SELECT * FROM register_client('"$CLIENT"');'
#/home/ec2-user/postgres/bin/psql -U postgres -d mydb -c 'COPY MESSAGE FROM "messageData_$1_$2.dat"'
#MYFILE=''\''messageData_'"$1"'_'"$2"'.dat'\'
#echo $MYFILE
cd D:\\PostgreSQL\\9.4.4\\bin
#./psql -U postgres -d mydb -c 'COPY MESSAGE FROM '"$MYFILE"';'
#./psql -U postgres -d mydb -c 'COPY MESSAGE FROM '\''C:\Users\Sandro\Documents\eclipse\ASL\db_baseline\initalMessageLoad\messageData_500K_200.dat'\'
./psql -U postgres -d mydb -f $1