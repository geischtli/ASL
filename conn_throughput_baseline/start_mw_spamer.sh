#!/bin/bash

# since we are operating on port 9090 in the whole ASL project
# we expect it to be open in the AWS security rules.
# we send exactly 500*1024*1024 bytes.

# $1 = IP of target machine
dd if=/dev/zero bs=5000M count=1| nc -v $1 9090