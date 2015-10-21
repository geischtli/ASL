#!/bin/bash

sudo yum install gcc -y
sudo yum install readline-devel -y
sudo yum install zlib-devel -y

tar xjf postgresql-9.4.4.tar.bz2

cd postgresql-9.4.4
./configure --prefix="/home/ec2-user/postgres"

make

mkdir /home/ec2-user/postgres

make install

LD_LIBRARY_PATH=/home/ec2-user/postgres/lib export LD_LIBRARY_PATH

/home/ec2-user/postgres/bin/initdb -D /home/ec2-user/postgres/db/

/home/ec2-user/postgres/bin/pg_ctl -D /home/ec2-user/postgres/db/ start

# wait until the startup loggint text is shown then return to command line
sleep 5
printf "\nDATABASE CLUSTER OK."
printf "SETUP mydb...\n"

/home/ec2-user/postgres/bin/createuser -s postgres

/home/ec2-user/postgres/bin/createdb mydb -U postgres -w

/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f /home/ec2-user/ASL/db_setup/initDatabase.sql

/home/ec2-user/postgres/bin/pg_ctl -D /home/ec2-user/postgres/db/ restart

sleep 5
printf "\nmydb OK"
printf "COPY CONFIG FILES...\n"
cp /home/ec2-user/ASL/db_setup/pg_hba.conf /home/ec2-user/postgres/db
cp /home/ec2-user/ASL/db_setup/postgresql.conf /home/ec2-user/postgres/db

/home/ec2-user/postgres/bin/pg_ctl -D /home/ec2-user/postgres/db/ restart

sleep 5
printf "\nCOPY CONFIG FILES DONE"
printf "FULL DATABASE SETUP COMPLETED. WE'RE READY TO GO!\n"