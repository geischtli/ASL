clear variables

basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\db_1M_test_data\';

clients = 1:5:60;
numClients = length(clients);

% store here all throughputs
tp_data = zeros(numClients, 1);
% here store all the response times
rt_data = zeros(numClients, 1);

% important note:
% all clients ran for exactly 120 seconds!

for i = 1:numClients
    currNumClients = clients(i);
    foldername = strcat('level_2_', num2str(currNumClients));
    
    % get all log file names in this directory
    folderpath = strcat(basedir, foldername);
    filenames = dir(folderpath);
    numfiles = length(filenames);
    
    % current response times
    curr_rt_sum = 0;
    
    % start at offset 3 because 1 = . and 2 = ..
    for j = 3:numfiles
        % read the log data
        log_data = dlmread(strcat(folderpath, ...
            strcat('\', filenames(j).name)));
        % since each transaction is logged on a new line
        % for throughput we can just count the number of lines
        curr_tp = size(log_data, 1);
        tp_data(i) = tp_data(i) + curr_tp;
        curr_rt_sum = curr_rt_sum + sum(log_data(:, 3));
    end
    % average over number of transactions seen
    rt_data(i) = curr_rt_sum / tp_data(i);
end

tp1 = tp_data;
rt1 = rt_data;

basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\db_1M_test_data_set_1\';

clients = 1:5:60;
numClients = length(clients);

% store here all throughputs
tp_data = zeros(numClients, 1);
% here store all the response times
rt_data = zeros(numClients, 1);

% important note:
% all clients ran for exactly 120 seconds!

for i = 1:numClients
    currNumClients = clients(i);
    foldername = strcat('level_2_', num2str(currNumClients));
    
    % get all log file names in this directory
    folderpath = strcat(basedir, foldername);
    filenames = dir(folderpath);
    numfiles = length(filenames);
    
    % current response times
    curr_rt_sum = 0;
    
    % start at offset 3 because 1 = . and 2 = ..
    for j = 3:numfiles
        % read the log data
        log_data = dlmread(strcat(folderpath, ...
            strcat('\', filenames(j).name)));
        % since each transaction is logged on a new line
        % for throughput we can just count the number of lines
        curr_tp = size(log_data, 1);
        tp_data(i) = tp_data(i) + curr_tp;
        curr_rt_sum = curr_rt_sum + sum(log_data(:, 3));
    end
    % average over number of transactions seen
    rt_data(i) = curr_rt_sum / tp_data(i);
end

tp2 = tp_data;
rt2 = rt_data;

basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\db_1M_test_data_set_2\';

clients = 1:5:60;
numClients = length(clients);

% store here all throughputs
tp_data = zeros(numClients, 1);
% here store all the response times
rt_data = zeros(numClients, 1);

% important note:
% all clients ran for exactly 120 seconds!

for i = 1:numClients
    currNumClients = clients(i);
    foldername = strcat('level_2_', num2str(currNumClients));
    
    % get all log file names in this directory
    folderpath = strcat(basedir, foldername);
    filenames = dir(folderpath);
    numfiles = length(filenames);
    
    % current response times
    curr_rt_sum = 0;
    
    % start at offset 3 because 1 = . and 2 = ..
    for j = 3:numfiles
        % read the log data
        log_data = dlmread(strcat(folderpath, ...
            strcat('\', filenames(j).name)));
        % since each transaction is logged on a new line
        % for throughput we can just count the number of lines
        curr_tp = size(log_data, 1);
        tp_data(i) = tp_data(i) + curr_tp;
        curr_rt_sum = curr_rt_sum + sum(log_data(:, 3));
    end
    % average over number of transactions seen
    rt_data(i) = curr_rt_sum / tp_data(i);
end

tp3 = tp_data;
rt3 = rt_data;

tot_tp = tp1 + tp2 + tp3;
tot_rt = rt1 + rt2 + rt3;

rl_rt_per_req = tot_rt./tot_tp;
il_rt_per_req = (clients'./tot_tp).*10^3;