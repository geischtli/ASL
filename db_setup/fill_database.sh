#!/bin/bash

# fill database for stability experiment with 300K entries

# clear everything 
#./psql -U postgres -d mydb -f C:/Users/Sandro/Documents/eclipse/ASL/db_setup/clearDatabase.sql -q
/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/clearDatabase.sql -q

# first init db with normal functionalities for sending and removing
/home/ec2-user/postgres/bin/psql -U postgres -d mydb -f ../db_setup/initDatabase.sql -q

/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM register_client(1);' >/dev/null
/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM create_queue(1);' >/dev/null

#MESSAGE=1
#while [ $MESSAGE -le 300000 ]; do
#	/home/ec2-user/postgres/bin/psql -U postgres -d mydb -q -c 'SELECT * FROM send_message(1, 1, 1, '"'"'01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789'"'"');' >/dev/null
#	MESSAGE=`expr $MESSAGE + 1`
#done
/home/ec2-user/postgres/bin/pgbench -U postgres --no-vacuum -t 1000 \
	-f ../db_baseline/benchScripts/benchLevel3_insert.sql \
	-c 30 -j 30 mydb \
	>> ../db_baseline/logs/level$1_$2_$3.log

echo done