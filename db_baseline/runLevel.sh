#!/bin/bash

START_DB_CONNECTIONS=1;
END_DB_CONNECTIONS=$2;
INCREMENT_DB_CONNECTIONS=1;

CURR_DB_CONNECTIONS=$START_DB_CONNECTIONS;

TIME_PER_RUN=$3;

# create a directory for our logs
mkdir -p logs

printf "Level %d setup:\n%d runs\neach one of %d seconds\n\n" $1 $END_DB_CONNECTIONS $TIME_PER_RUN

#cd D:/PostgreSQL/9.4.4/bin/

while [  $CURR_DB_CONNECTIONS -le $END_DB_CONNECTIONS ]; do

	# clear everything 
	#./psql -U postgres -d mydb -f C:/Users/Sandro/Documents/eclipse/ASL/db_setup/clearDatabase.sql -q
	/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/clearDatabase.sql -q
	
	# register all needed functions in this level
	if [ $1 -lt 2 ]
		then
			/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/experiments/initLevel$1.sql -q
		else
			/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/initDatabase.sql -q
			
			#Prefill the database with the number of clients running in the system
			#this allows to ignore the handshake/creation part of the system, which
			#is not important for benchmarking
			CLIENT=1
			while [ $CLIENT -le $CURR_DB_CONNECTIONS ]; do
				/home/ec2-user/postgres/bin/psql -U postgres -d mydb -c 'SELECT * FROM register_client(1);'
				/home/ec2-user/postgres/bin/psql -U postgres -d mydb -c 'SELECT * FROM create_queue('"$CLIENT"');'
				CLIENT=`expr $CLIENT + 1`
			done
	fi
	
	# resetup the bgbench base environment/tables
	/home/ec2-user/postgres/bin/pgbench -U postgres -i -q mydb
	
	# clear the pgbech_accounts table, becuase we dont need it ant its quite big
	/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/experiments/clearAccountsTable.sql -q	

	printf "Run script with %d concurrent database connection for %d seconds\n" $CURR_DB_CONNECTIONS $TIME_PER_RUN
	CURR_WORKER_THREADS=$CURR_DB_CONNECTIONS
	/home/ec2-user/postgres/bin/pgbench -r -l --aggregate-interval=1 -U postgres --no-vacuum -T $TIME_PER_RUN \
		-f ./benchScripts/benchLevel$1.sql \
		-c $CURR_DB_CONNECTIONS -j $CURR_DB_CONNECTIONS \
		-s $CURR_DB_CONNECTIONS mydb \
		>> ./logs/level$1_$2_$3.log
		
	CURR_DB_CONNECTIONS=`expr $CURR_DB_CONNECTIONS + $INCREMENT_DB_CONNECTIONS`
done
