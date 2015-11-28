clear variables

% constants
num_clients_per_machine = 60;

% read global response time of client perspective, thinktime and
% individual throughput
basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\stability_2\data_client1\';
files = dir(basedir);
numFiles = length(files);

total_rt_per_request = zeros(120, 1);
total_thinktime_per_request = zeros(120, 1);
total_tp = zeros(120, 1);

for i = 3:3:numFiles
    curr_rt = dlmread(strcat(basedir, files(i).name));
    curr_thinktime = dlmread(strcat(basedir, files(i+1).name));
    curr_tp = dlmread(strcat(basedir, files(i+2).name));
    
    curr_rt = curr_rt(1:120);
    curr_thinktime = curr_thinktime(1:120);
    curr_tp = curr_tp(1:120);
    
    total_rt_per_request = total_rt_per_request + curr_rt./curr_tp;
    total_thinktime_per_request = total_thinktime_per_request + ...
        curr_thinktime./curr_tp;
    total_tp = total_tp + curr_tp;
end

total_rt_per_request = total_rt_per_request/num_clients_per_machine;
total_thinktime_per_request = ...
    total_thinktime_per_request/num_clients_per_machine;
% convert thinktime into milliseconds from nanoseconds
total_thinktime_per_request = total_thinktime_per_request*10^-6;
total_rt_per_request = total_rt_per_request*10^-6;

hold on
plot(total_rt_per_request)
plot(total_thinktime_per_request)

hold off
figure()
plot(total_tp);
title('full throughput')

% now dig into the middleware logs
basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\stability_2\data_mw1\';
db_plust_network_rt = dlmread(strcat(basedir, 'db_plus_network_rt.log'));
mw_rt = dlmread(strcat(basedir, 'rtt.log'));
thread_count = dlmread(strcat(basedir, 'threadCount.log'));
mw_tp = dlmread(strcat(basedir, 'throughput.log'));
wait_for_db_conn = dlmread(strcat(basedir, 'waitForDbConn.log'));

db_plust_network_rt_per_request = db_plust_network_rt./mw_tp*10^-6;
mw_rt_per_request = mw_rt./mw_tp*10^-6;
thread_count_per_request = thread_count./mw_tp;
wait_for_db_conn_per_request = wait_for_db_conn./mw_tp*10^-6;

db_plust_network_rt_per_request = db_plust_network_rt_per_request(20:60);
mw_rt_per_request = mw_rt_per_request(20:60);
thread_count_per_request = thread_count_per_request(20:60);
wait_for_db_conn_per_request = wait_for_db_conn_per_request(20:60);

hold off
figure();
hold on
plot(mw_rt_per_request)
plot(db_plust_network_rt_per_request)
plot(wait_for_db_conn_per_request)
%plot(total_rt_per_request)

legend('mw rt', 'db plust network', 'wait for db conn')