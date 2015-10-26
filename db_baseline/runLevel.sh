#!/bin/bash


# ARGUMENTS
# $1 - The level: {0, 1, 2}
# $2 - The maximal number of concurrent database connections (runs from 1 to $2): INTEGER
# $3 - Time per run: INTEGER
# $4 - Preload the database with messages: {0, 1}

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
	
	# register all needed functions/data in this level
	if [ $1 -lt 2 ]
		then
			# level 0 and 1
			/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/experiments/initLevel$1.sql -q
		else
			# level 2
			if [ $4 == 0 ]
				then
					/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/initDatabase.sql -q
				else
					/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/initPreFilledDatabase.sql -q
			fi			
			if [ $4 == 0 ]
				then
					#Prefill the database with the number of clients running in the system
					#this allows to ignore the handshake/creation part of the system, which
					#is not important for benchmarking
					CLIENT=1
					while [ $CLIENT -le $CURR_DB_CONNECTIONS ]; do
						/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM register_client(1);' >/dev/null
						/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM create_queue('"$CLIENT"');' >/dev/null
						CLIENT=`expr $CLIENT + 1`
					done
				else
					# Fill the database with a lot of data to benchmark this effect, too.
					# Prefill the database with 100 clients and queues, because the prepared
					# data expects these to be present
					CLIENT=1
					while [ $CLIENT -le 100 ]; do
						/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM register_client(1);' >/dev/null
						/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM create_queue('"$CLIENT"');' >/dev/null
						CLIENT=`expr $CLIENT + 1`
					done
					
					printf 'Start filling message table...\n'
					#now load the data specified by the file into the message table
					sh /home/ec2-user/ASL/db_baseline/initialMessageLoad/addMessageData.sh \
						/home/ec2-user/ASL/db_baseline/initialMessageLoad/messageData_500000_200.dat
			fi
	fi
	
	# resetup the bgbench base environment/tables
	/home/ec2-user/postgres/bin/pgbench -U postgres -i --no-vacuum -q mydb
	
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
