#!/bin/bash

# SET THE PARAMETERS FOR THE MAIN SCRIPT
NUM_MIDDLEWARES=1
NUM_CLIENTS=10

printf "\n\n"
printf "*** ASL MAIN SCRIPT ***\n"
printf "Start the middleware instances\n"

#for (( i = 0; i < 1; i++ )) 
#do
	ant -f antBuildMiddleware.xml &
#done

sleep 10

printf "\n\nStart client instances\n"
#for (( i = 0; i < 1; i++ )) 
#do
	ant -f antBuildClient.xml &
#done

printf "\n\n"