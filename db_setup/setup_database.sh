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
printf "\n\nDATABASE CLUSTER OK.\n"
printf "SETUP mydb...\n\n"

/home/ec2-user/postgres/bin/createuser -s postgres

/home/ec2-user/postgres/bin/createdb mydb -U postgres -w

/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f /home/ec2-user/ASL/db_setup/initDatabase.sql

/home/ec2-user/postgres/bin/pg_ctl -D /home/ec2-user/postgres/db/ restart

sleep 5
printf "\n\nmydb OK\n"
printf "COPY CONFIG FILES...\n\n"
cp /home/ec2-user/ASL/db_setup/pg_hba.conf /home/ec2-user/postgres/db
cp /home/ec2-user/ASL/db_setup/postgresql.conf /home/ec2-user/postgres/db

/home/ec2-user/postgres/bin/pg_ctl -D /home/ec2-user/postgres/db/ restart

sleep 5
printf "\n\nCOPY CONFIG FILES DONE"

# make sure our homebrew pg_ctl is executable with ./pg_ctl start/stop/restart/status
chmod +x /home/ec2-user/ASL/pg_ctl.sh

printf "FULL DATABASE SETUP COMPLETED. WE'RE READY TO GO!\n\n"