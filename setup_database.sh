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
sleep 2
echo "database successfullly set up, continue to setup mydb"

/home/ec2-user/postgres/bin/createuser -s postgres

/home/ec2-user/postgres/bin/createdb mydb -U postgres -w

/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f initDatabase.sql

/home/ec2-user/postgres/bin/pg_ctl -D /home/ec2-user/postgres/db/ restart