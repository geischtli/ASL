#!/bin/bash

myfile=$1

substring=`echo $myfile| cut -d '/' -f 2`
subsubstring=`echo $substring| cut -d '.' -f 1`

sed '/number\|tps\|latency/!d' $myfile >> logs/${subsubstring}_filtered.log