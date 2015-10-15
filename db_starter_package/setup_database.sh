#!/bin/bash

tar xjf postgresql-9.4.4.tar.bz2

cd postgresql-9.4.4
./configure --prefix="/mnt/local/sanhuber/postgres"

make

make install

LD_LIBRARY_PATH=/mnt/local/sanhuber/postgres/lib export LD_LIBRARY_PATH

/mnt/local/sanhuber/postgres/bin/initdb -D /mnt/local/sanhuber/postgres/db/

/mnt/local/sanhuber/postgres/bin/pg_ctl -D /mnt/local/sanhuber/postgres/db/ start