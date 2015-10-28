clear variables

basedir = '..\..\..\ASL_LOGS\summary_db_1_60_DB_CONNS_NO_DATA\level';
level = '2WIND\';
basedir = strcat(basedir, level);

throughputFile = strcat(basedir, 'tp_summary.log');
latencyFile = strcat(basedir, 'lat_summary.log');
indexFile = strcat(basedir, 'idx_summary.log');

tp = dlmread(throughputFile);
lat = dlmread(latencyFile);
idx = dlmread(indexFile);

boxplot(tp, idx);
figure()
boxplot(lat, idx);

%% plot data
%% DATA COLUMNS
%% #DBCONNECTIONS | TPS INC | TPS EXC | LATENCY
%clients = data(:, 1);
%tpsinc = data(:, 2);
%tpsexc = data(:, 3);
%latency = data(:, 4);

%plotyy(clients, tpsexc, clients, latency)