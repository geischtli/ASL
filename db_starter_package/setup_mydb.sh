#!/bin/bash

#/mnt/local/sanhuber/postgres/bin/createuser -s postgres
/home/ec2-user/postgres/bin/createuser -s postgres

#/mnt/local/sanhuber/postgres/bin/createdb mydb -U postgres -w
/home/ec2-user/postgres/bin/createdb mydb -U postgres -w

#/mnt/local/sanhuber/postgres/bin/psql -U postgres -d mydb -f initDatabase.sql
/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f initDatabase.sql

#/mnt/local/sanhuber/postgres/bin/pg_ctl -D /mnt/local/sanhuber/postgres/db/ restart
/home/ec2-user/postgres/bin/pg_ctl -D /home/ec2-user/postgres/db/ restart