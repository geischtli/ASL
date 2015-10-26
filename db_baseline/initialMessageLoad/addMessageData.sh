#!/bin/bash

#ARGUMENTS
# $1 links to the datafile which will be loaded. Full path expected. (/home/ec2-user/ASL/db_baseline/initialMessageLoad/xxx.dat)

# Fill the message table with a prepared load of entries, such we are able
# to benchmark the performance under a certain amount of existing data.
/home/ec2-user/postgres/bin/psql -U postgres -d mydb -c 'COPY MESSAGE FROM '\'"$1"\'';'