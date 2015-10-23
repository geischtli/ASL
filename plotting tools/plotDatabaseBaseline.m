clear variables

basedir = '..\db_baseline\logs\';
level = '0';
levelstr = strcat('level', level);
filename = strcat(levelstr, '_plotReady.log');
logfile = dir(strcat(basedir, filename));

% read data into matrix
data = dlmread(strcat(basedir, logfile.name));

%% plot data
%% DATA COLUMNS
%% #DBCONNECTIONS | TPS INC | TPS EXC | LATENCY
clients = data(:, 1);
tpsinc = data(:, 2);
tpsexc = data(:, 3);
latency = data(:, 4);

plotyy(clients, tpsexc, clients, latency)