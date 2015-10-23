clear variables

basedir = '..\db_baseline\logs\';
level = '0';
levelstr = strcat('level', level);
filename = strcat(levelstr, 'log_plotReady.log');
logfile = dir(strcat(basedir, filename));

% read data into matrix
data = dlmread(strcat(basedir, logfile.name));

%% plot data
%% DATA COLUMNS
%% #DBCONNECTIONS | TPS INC | TPS EXC | LATENCY
plot(data(:, 1), data(:, 3))
figure()
plot(data(:, 1), data(:, 4))