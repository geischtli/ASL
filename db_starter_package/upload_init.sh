#!/bin/bash

scp setup_database.sh sanhuber@dryad12.ethz.ch:/mnt/local/sanhuber
scp postgresql-9.4.4.tar.bz2 sanhuber@dryad12.ethz.ch:/mnt/local/sanhuber
scp ../initDatabase.sql sanhuber@dryad12.ethz.ch:/mnt/local/sanhuber
scp setup_mydb.sh sanhuber@dryad12.ethz.ch:/mnt/local/sanhuber