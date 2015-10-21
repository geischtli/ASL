#!/bin/bash

# since we are operating on port 9090 in the whole ASL project
# we expect it to be open in the AWS security rules.
nc -v -l 9090 >/dev/null