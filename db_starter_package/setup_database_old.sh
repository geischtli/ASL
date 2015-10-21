#!/bin/bash

sudo yum install gcc -y
sudo yum install readline-devel -y
sudo yum install zlib-devel -y

tar xjf postgresql-9.4.4.tar.bz2

cd postgresql-9.4.4
#./configure --prefix="/mnt/local/sanhuber/postgres"
./configure --prefix="/home/ec2-user/postgres"

make

mkdir /home/ec2-user/postgres

make install

#LD_LIBRARY_PATH=/mnt/local/sanhuber/postgres/lib export LD_LIBRARY_PATH
LD_LIBRARY_PATH=/home/ec2-user/postgres/lib export LD_LIBRARY_PATH

#/mnt/local/sanhuber/postgres/bin/initdb -D /mnt/local/sanhuber/postgres/db/
/home/ec2-user/postgres/bin/initdb -D /home/ec2-user/postgres/db/

#/mnt/local/sanhuber/postgres/bin/pg_ctl -D /mnt/local/sanhuber/postgres/db/ start
/home/ec2-user/postgres/bin/pg_ctl -D /home/ec2-user/postgres/db/ start