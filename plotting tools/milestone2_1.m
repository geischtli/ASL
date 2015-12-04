clear variables
% Model the system as M/M/1
% This model requires two parameters:
% a) The arrival rate, which is equal to the throughput
% b) The service rate, which is equal to 1/(response time)
% Since we take the measurements from the stability experiment
% we know both parameters.

% constants
num_clients_per_machine = 60;

% read global response time of client perspective, thinktime and
% individual throughput
basedir_base = 'C:\Users\Sandro\Documents\ASL_LOGS\stability_2\';
db_conns_per_mw = 10:10:70;
num_db_conns_per_mw = length(db_conns_per_mw);

curr_num_db_conns_per_mw = 20;
numstr = num2str(curr_num_db_conns_per_mw);
postfix = strcat(numstr, strcat('_', numstr));
basedir = strcat(...
    basedir_base, strcat('db_conn_per_mw_', ...
    strcat(postfix, '\')));

% client data vars
total_rt_per_request = zeros(120, 1);
total_thinktime_per_request = zeros(120, 1);
total_tp = zeros(120, 1);

for k = 1:2
    client_basedir = strcat(basedir, ...
        strcat('data_client', ...
        strcat(num2str(k), '\')));
    files = dir(client_basedir);
    numFiles = length(files);

    for i = 3:3:numFiles
        curr_rt = dlmread(strcat(client_basedir, files(i).name));
        curr_thinktime = dlmread(strcat(client_basedir, files(i+1).name));
        curr_tp = dlmread(strcat(client_basedir, files(i+2).name));

        curr_rt = curr_rt(1:120);
        curr_thinktime = curr_thinktime(1:120);
        curr_tp = curr_tp(1:120);

        total_rt_per_request = total_rt_per_request + curr_rt./curr_tp;
        total_thinktime_per_request = total_thinktime_per_request + ...
            curr_thinktime./curr_tp;
        total_tp = total_tp + curr_tp;
    end
end

total_rt_per_request = total_rt_per_request/(2*num_clients_per_machine);
total_thinktime_per_request = ...
    total_thinktime_per_request/(2*num_clients_per_machine);
% convert thinktime into milliseconds from nanoseconds
total_thinktime_per_request = total_thinktime_per_request*10^-6;
total_rt_per_request = total_rt_per_request*10^-6;

%     hold on
%     plot(total_rt_per_request)
%     plot(total_thinktime_per_request)
% 
%     hold off
%     figure()
%     plot(total_tp);
%     title('full throughput')

% now dig into the middleware logs
db_plust_network_rt_per_request = zeros(81, 1);
mw_rt_per_request = zeros(81, 1);
thread_count = zeros(81, 1);
wait_for_db_conn_per_request = zeros(81, 1);
db_conn_queue_length_per_request = zeros(81, 1);
for k = 1:2
    mw_basedir = strcat(basedir, ...
        strcat('data_mw', ...
        strcat(num2str(k), '\')));
    db_plus_network_rt = dlmread(strcat(mw_basedir, 'db_plus_network_rt.log'));
    mw_rt = dlmread(strcat(mw_basedir, 'rtt.log'));
    curr_thread_count = dlmread(strcat(mw_basedir, 'threadCount.log'));
    mw_tp = dlmread(strcat(mw_basedir, 'throughput.log'));
    wait_for_db_conn = dlmread(strcat(mw_basedir, 'waitForDbConn.log'));
    db_conn_queue_length = dlmread(strcat(mw_basedir, 'db_conn_queue_length.log'));

    curr_db_plust_network_rt_per_request = db_plus_network_rt(20:100)./(total_tp(20:100)/2)*10^-6;
    curr_mw_rt_per_request = mw_rt(20:100)./(total_tp(20:100)/2)*10^-6;
    curr_wait_for_db_conn_per_request = wait_for_db_conn(20:100)./(total_tp(20:100)/2)*10^-6;
    curr_db_conn_queue_length_per_request = db_conn_queue_length(20:100)./(total_tp(20:100)/2);

    db_plust_network_rt_per_request = ...
        db_plust_network_rt_per_request + curr_db_plust_network_rt_per_request;
    mw_rt_per_request = ...
        mw_rt_per_request + curr_mw_rt_per_request;
    thread_count = ...
        thread_count + curr_thread_count(20:100);
    wait_for_db_conn_per_request = ...
        wait_for_db_conn_per_request + curr_wait_for_db_conn_per_request;
    db_conn_queue_length_per_request = ...
        db_conn_queue_length_per_request + curr_db_conn_queue_length_per_request;
    if k == 1
        mw1_tp = mw_tp(20:100);
    else
        mw2_tp = mw_tp(20:100);
    end
end

service_time = mean(total_rt_per_request(~isnan(total_rt_per_request))) ...
    - mean(wait_for_db_conn_per_request) ...
    - mean(total_thinktime_per_request(~isnan(total_thinktime_per_request)));
service_rate = 1/(service_time*10^-3);
arrival_rate = total_tp(20:100);
factor = mean(arrival_rate)/service_rate;