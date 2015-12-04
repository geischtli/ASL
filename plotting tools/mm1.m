% Do the log analysis for milestone 2.1, i.e. the m/m/1 modeling. This is
% done based on the data of the stability_2 data with 20 db conns per
% middleware. This has been chosen, because the database operates best
% with 40 concurrent connections.

% clear workspace
clear variables

% set basedir of experiment data
basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\stability_2\db_conn_per_mw_20_20\';

% some constants
total_num_clients = 120;
num_clients_per_mw = 60;

% result vectors for the middleware data
% only take seconds 21:120
mw_total_tp = zeros(100, 1);
mw_total_wait_time_for_db_conn_per_request = zeros(2, 1);
mw_total_db_plus_network_rt = zeros(100, 1);
mw_total_rt = zeros(100, 1);
mw_total_rt_per_request = zeros(2, 1);
mw_mean_db_work_per_request = zeros(2, 1);
mw_uncut_tp_sum = zeros(135, 1);
mw_uncut_rt = zeros(135, 2);

% read the data of both middlewares
for i = 1:2
    mw_i_dir = strcat(basedir, ...
        strcat('data_mw', ...
        strcat(num2str(i), '\')));
    files = dir(mw_i_dir);
    % read also tp here, because it might be slightly different to the
    % one logged on the client side, because the timers don't clean up,
    % i.e. unlogged data at 120 seconds was just discarded.
    curr_tp = dlmread(strcat(mw_i_dir, 'throughput.log'));
    mw_uncut_tp_sum = mw_uncut_tp_sum + curr_tp;
    curr_wait_time_for_db_conn = ...
        dlmread(strcat(mw_i_dir, 'waitForDbConn.log'));
    curr_rt = dlmread(strcat(mw_i_dir, 'rtt.log'));
    mw_uncut_rt(:, i) = curr_rt./curr_tp;
    mw_total_rt_per_request(i) = mean(curr_rt(21:120)./curr_tp(21:120));
    % sum up total tp in system
    mw_total_tp = mw_total_tp + curr_tp(21:120);
    % sum up wait time, too
    mw_total_wait_time_for_db_conn_per_request(i) = ...
        mean(curr_wait_time_for_db_conn(21:120)./curr_tp(21:120));
end

% wait time was logged in nanoseconds, so scale to seconds
mw_total_rt_per_request = mw_total_rt_per_request * 10^-9;
mw_total_wait_time_for_db_conn_per_request = ...
    mw_total_wait_time_for_db_conn_per_request * 10^-9;

% average over both middlewares
mw_total_rt_per_request_mean = mean(mw_total_rt_per_request);
mw_total_wait_time_for_db_conn_per_request = ...
    mean(mw_total_wait_time_for_db_conn_per_request);

% average over all seconds the tp was measured
mw_total_tp_mean = mean(mw_total_tp);

%% Apply M/M/1 model

% System built is closed, so arrival rate = tp
arrival_rate = mw_total_tp_mean;

% service time = rt - wait time
service_time = mw_total_rt_per_request_mean ...
    - mw_total_wait_time_for_db_conn_per_request - 0.002;

% service rate = (service time)^-1
service_rate = 1/service_time;

% calculate other M/M/1 specific numbers
traffic_intensity = arrival_rate/service_rate;

% what if we had am M/M/40?
traffic_intensity_mm40 = traffic_intensity/40;