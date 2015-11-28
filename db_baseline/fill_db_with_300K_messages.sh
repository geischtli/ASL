#!/bin/bash

# create a directory for our logs
mkdir -p logs

# clear everything 
#./psql -U postgres -d mydb -f C:/Users/Sandro/Documents/eclipse/ASL/db_setup/clearDatabase.sql -q
/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/clearDatabase.sql -q

/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/initPreFilledDatabase.sql -q

# register a single client
/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM register_client(1);' >/dev/null
/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM create_queue(1);' >/dev/null

# resetup the bgbench base environment/tables
/home/ec2-user/postgres/bin/pgbench -U postgres -i -q mydb

# clear the pgbech_accounts table, becuase we dont need it ant its quite big
/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/experiments/clearAccountsTable.sql -q

# fill db with 750*10*40=300'000 entries
/home/ec2-user/postgres/bin/pgbench -r -l -U postgres --no-vacuum -t 750 \
	-f ./benchScripts/benchLevel3_insert.sql \
	-c 40 -j 40 mydb \
	>> ./logs/level$1_$2_$3.log