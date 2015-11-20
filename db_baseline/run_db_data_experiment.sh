#!/bin/bash

START_DB_CONNECTIONS=1;
END_DB_CONNECTIONS=60;
INCREMENT_DB_CONNECTIONS=5;
CURR_DB_CONNECTIONS=$START_DB_CONNECTIONS;

TIME_PER_RUN=$1;

# create a directory for our logs
mkdir logs

printf "Level %d setup:\n%d runs\neach one of %d seconds\n\n" 2 $END_DB_CONNECTIONS $TIME_PER_RUN

while [  $CURR_DB_CONNECTIONS -le $END_DB_CONNECTIONS ]; do
	
	# init with prefilled database
	/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/initPreFilledDatabase.sql -q
	
	# resetup the bgbench base environment/tables
	/home/ec2-user/postgres/bin/pgbench -U postgres -i -q mydb
	
	# clear the pgbech_accounts table, becuase we dont need it ant its quite big
	/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/experiments/clearAccountsTable.sql -q
	
	printf "Start filling database\n"
	
	# add a client and queue
	/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM register_client(1);' >/dev/null
	/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM create_queue(1);' >/dev/null
	
	# pre fill the message table with 10^9 messages
	# 40*10*2500=10^9
	/home/ec2-user/postgres/bin/pgbench -r -l -U postgres --no-vacuum -t 2500 \
				-f ./benchScripts/benchLevel3_insert.sql \
				-c 40 -j 40 -s 1 mydb \
				>>/dev/null
				
	printf "Complete\n"
	
	printf "Start with actual benchmark"
	
	/home/ec2-user/postgres/bin/pgbench -r -l -U postgres --no-vacuum -T 60 \
				-f ./benchScripts/benchLevel2.sql \
				-c $CURR_DB_CONNECTIONS -j $CURR_DB_CONNECTIONS \
				-s $CURR_DB_CONNECTIONS mydb \
				>> ./logs/level_2_$CURR_DB_CONNECTIONS.log
	
	printf "Complete\n"
	
	
	CURR_DB_CONNECTIONS=`expr $CURR_DB_CONNECTIONS + $INCREMENT_DB_CONNECTIONS`
done
