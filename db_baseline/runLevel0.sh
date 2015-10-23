#!/bin/bash

START_DB_CONNECTIONS=1;
END_DB_CONNECTIONS=$1;
INCREMENT_DB_CONNECTIONS=1;

CURR_DB_CONNECTIONS=$START_DB_CONNECTIONS;

TIME_PER_RUN=$2

printf "Experiment Setup: %d runs, each one of %d seconds\n\n" $END_DB_CONNECTIONS $TIME_PER_RUN

cd D:/PostgreSQL/9.4.4/bin/

while [  $CURR_DB_CONNECTIONS -le $END_DB_CONNECTIONS ]; do

	# clear everything 
	./psql -U postgres -d mydb -f C:/Users/Sandro/Documents/eclipse/ASL/db_setup/clearDatabase.sql
	# resetup the bgbench base environment/tables
	./pgbench -h 127.0.0.1 -U postgres -i mydb
	# register all needed functions in this level
	./psql -U postgres -d mydb -f C:/Users/Sandro/Documents/eclipse/ASL/db_setup/experiments/initLevel0.sql

	printf "Run script with %d concurrent database connection for %d seconds\n" $CURR_DB_CONNECTIONS $TIME_PER_RUN
	CURR_WORKER_THREADS=$CURR_DB_CONNECTIONS
	./pgbench -h 127.0.0.1 -U postgres --no-vacuum -T $TIME_PER_RUN \
		-f C:/Users/Sandro/Documents/eclipse/ASL/db_baseline/benchScripts/benchLevel0.sql \
		-c $CURR_DB_CONNECTIONS -j $CURR_DB_CONNECTIONS mydb \
		>> C:/Users/Sandro/Documents/eclipse/ASL/db_baseline/logs/level0.log
	CURR_DB_CONNECTIONS=`expr $CURR_DB_CONNECTIONS + $INCREMENT_DB_CONNECTIONS`
done
