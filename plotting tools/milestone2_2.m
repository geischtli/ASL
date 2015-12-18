% The scalability data was gather for milestone 2 for exercise 2. The goal
% of this script is to give an overview about the data, and to allow
% in a next step to plot additional M/M/m lines either veri- or 
% falsify the corresponding models.
clear variables
close all

% set the base directory where the logs are stored
basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\scalability\';

% predefine all possible configurations
mws = [1 2];
clients = 5:5:60;
num_mws = length(mws);
num_clients = length(clients);

% define how many seconds of the original measurements should be treated
% as valid data, when ignoring the first 20 seconds
int_start = 21;
int_end = 60;

% result matrices for single and double mw configuration throughput
tp_res_single = zeros((int_end - int_start + 1) * num_clients, 1);
tp_res_double = zeros((int_end - int_start + 1) * num_clients, 1);

% result matrices for single and double mw configuration resposne time
rt_res_single = zeros((int_end - int_start + 1) * num_clients, 1);
rt_res_double = zeros((int_end - int_start + 1) * num_clients, 1);

% wait time res matrices
wait_res_single = zeros((int_end - int_start + 1) * num_clients, 1);
wait_res_double = zeros((int_end - int_start + 1) * num_clients, 1);

% q len matrices
qlen_res_single = zeros((int_end - int_start + 1) * num_clients, 1);
qlen_res_double = zeros((int_end - int_start + 1) * num_clients, 1);

% number of threads in each middleware
threads_res_single = zeros((int_end - int_start + 1) * num_clients, 1);
threads_res_single_res_double = zeros((int_end - int_start + 1) * num_clients, 1);

% response time of db plus network
db_plus_network_res_single = zeros((int_end - int_start + 1) * num_clients, 1);
db_plus_network_res_double = zeros((int_end - int_start + 1) * num_clients, 1);

% indices for the boxplots
idx_single = zeros((int_end - int_start + 1) * num_clients, 1);
idx_double = zeros((int_end - int_start + 1) * num_clients, 1);

% run through all the logs of the middlewares only, since we still
% treat the system as a blackbox around the middlewares and the
% database, so we are only interested in the measurements at the start
% and end of this box
for i = 1:num_mws
    for j = 1:num_clients
        curr_mws = mws(i);
        curr_clients = clients(j);
        curr_folder = strcat(strcat(strcat(num2str(curr_mws), 'mw_'), ...
            strcat(num2str(curr_clients), '_')), ...
            strcat(num2str(curr_clients), '_'));
        if i == 1
            curr_folder = strcat(curr_folder, '40\');
        else
            curr_folder = strcat(curr_folder, '20_20\');
        end
        
        % sum up the current inter-results
        total_mw_tp = zeros(int_end - int_start + 1, 1);
        total_mw_rt_per_request = zeros(int_end - int_start + 1, 1);
        total_mw_rt = zeros(int_end - int_start + 1, 1);
        total_mw_wait = zeros(int_end - int_start + 1, 1);
        total_q_len = zeros(int_end - int_start + 1, 1);
        total_threads = zeros(int_end - int_start + 1, 1);
        total_db_plus_network = zeros(int_end - int_start + 1, 1);
        
        % run through the log files of the middleware(s)
        mw_log_dirs = char('data_mw1\', 'data_mw2\');
        for k = 1:i
            % concat all path strings
            log_folder = strcat(basedir, ...
                strcat(curr_folder, mw_log_dirs(k, :)));
            
            % read the logs
            curr_tp = dlmread(strcat(log_folder, 'throughput.log'));
            curr_rt = dlmread(strcat(log_folder, 'rtt.log'));
            curr_wait = dlmread(strcat(log_folder, 'waitForDbConn.log'));
            curr_q_len = dlmread(strcat(log_folder, 'db_conn_queue_length.log'));
            curr_threads = dlmread(strcat(log_folder, 'threadCount.log'));
            curr_db_plus_network = dlmread(strcat(log_folder, 'db_plus_network_rt.log'));
            
            % sum them up
            total_mw_tp = total_mw_tp + curr_tp(int_start:int_end);
            total_mw_rt_per_request = total_mw_rt_per_request + ...
                curr_rt(int_start:int_end)./curr_tp(int_start:int_end);
            total_mw_rt = total_mw_rt + curr_rt(int_start:int_end);
            total_mw_wait = total_mw_wait + curr_wait(int_start:int_end);
            total_q_len = total_q_len + curr_q_len(int_start:int_end);
            total_threads = total_threads + curr_threads(int_start:int_end);
            total_db_plus_network = total_db_plus_network ...
                + curr_db_plus_network(int_start:int_end);
        end
        
        % calculate result matrix indices
        idx_lo = (j-1)*40 + 1;
        idx_hi = j*40;
        
        % store the result
        if i == 1
            tp_res_single(idx_lo:idx_hi) = total_mw_tp;
            %rt_res_single(idx_lo:idx_hi) = total_mw_rt_per_request;
            rt_res_single(idx_lo:idx_hi) = total_mw_rt./total_mw_tp;
            wait_res_single(idx_lo:idx_hi) = total_mw_wait./total_mw_tp;
            qlen_res_single(idx_lo:idx_hi) = total_q_len./total_mw_tp;
            threads_res_single(idx_lo:idx_hi) = total_threads;
            db_plus_network_res_single(idx_lo:idx_hi) = ...
                total_db_plus_network./total_mw_tp;
            idx_single(idx_lo:idx_hi) = 2*curr_clients;
        else
            tp_res_double(idx_lo:idx_hi) = total_mw_tp;
            %rt_res_double(idx_lo:idx_hi) = total_mw_rt_per_request;
            rt_res_double(idx_lo:idx_hi) = total_mw_rt./total_mw_tp;
            wait_res_double(idx_lo:idx_hi) = total_mw_wait./total_mw_tp;
            qlen_res_double(idx_lo:idx_hi) = total_q_len./total_mw_tp;
            threads_res_double(idx_lo:idx_hi) = total_threads;
            db_plus_network_res_double(idx_lo:idx_hi) = ...
                total_db_plus_network./total_mw_tp;
            idx_double(idx_lo:idx_hi) = 2*curr_clients;
        end
    end
end

% scale the response time to millisseconds
rt_res_single = rt_res_single*10^-6;
rt_res_double = rt_res_double*10^-6;
wait_res_single = wait_res_single*10^-6;
wait_res_double = wait_res_double*10^-6;
db_plus_network_res_single = db_plus_network_res_single*10^-6;
db_plus_network_res_double = db_plus_network_res_double*10^-6;

%% GENERATE PLOTS

figure()
hold on

boxplot(tp_res_single, idx_single)

medians = findobj(gca,'tag','Median');
numMedians = length(medians);
colors = ['b'];
for i = 1:1
    currMedians = medians(((i-1)*numMedians + 1):(i*numMedians));
    xs = zeros(numMedians, 1);
    ys = zeros(numMedians, 1);
    for j = 1:numMedians
        currMedian = currMedians(j);
        xt = currMedian.XData;
        xs(j) = mean(xt);
        yt = currMedian.YData;
        ys(j) = mean(yt);
    end
    single_tp_means = ys;
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
legend('1 Middleware', 'location', 'northwest')
xlabel('Number of Clients in the System')
ylabel('Requests per Second')
title('Throughput of the whole System')

hold off
figure()
hold on

boxplot(rt_res_single, idx_single)
boxplot(wait_res_single, idx_single)
boxplot(rt_res_single - wait_res_single, idx_single)
boxplot(db_plus_network_res_single, idx_single)

medians = findobj(gca,'tag','Median');
numMedians = length(medians)/4;
colors = ['k', 'g', 'r', 'b'];
for i = 1:4
    currMedians = medians(((i-1)*numMedians + 1):(i*numMedians));
    xs = zeros(numMedians, 1);
    ys = zeros(numMedians, 1);
    for j = 1:numMedians
        currMedian = currMedians(j);
        xt = currMedian.XData;
        xs(j) = mean(xt);
        yt = currMedian.YData;
        ys(j) = mean(yt);
    end
    if i == 1
        single_db_plus_network_means = flipud(ys);
    elseif i == 2
        single_service_means = flipud(ys);
    elseif i == 3
        single_wait_means = flipud(ys);
    else
        single_rt_means = flipud(ys);
    end
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end

legend('Database+Network Response Time', 'Service Time', ...
    'Wait Time', 'Response Time', 'location', 'northwest')
xlabel('Number of Clients in the System')
ylabel('Milliseconds')
title('Timings of the whole System')
set(gca, 'YLim', [0, 9])

% calculate other model-related numbers
m = 40;
%traffic intensity
mu = 1./(single_service_means.*10^-3);
rho = single_tp_means./(m.*(mu));
% probability of 0 jobs in the system
rho0 = calculate_rho0(rho, m);
% probability of having n requests in the system
rho_n = calc_req_prob(rho, rho0, m, 60);
% probability of queueing
l = ((m.*rho).^m./(factorial(m)*(1-rho))).*rho0;
% mean number of requests in the system
En = m.*rho + (rho.*l)./(1-rho);
% mean number of requests in the queue
Enq = (rho.*l)./(1-rho);
% mean waiting time
Ew = l./(m.*mu.*(1 - rho));
% mean response time
Er = 1./mu.*(1 + (l)./(m.*(1-rho)));
Er.*10^3


% normalize and plot rho_n
hold off
figure()
hold on
%store the maximum of each row/num of clients
theoretical_max_q_len = zeros(size(rho_n, 1), 1);
for i = 1:12
    rho_nn(i, :) = rho_n(i, :)./sum(rho_n(i, :));
    [~, theoretical_max_q_len(i)] = max(rho_nn(i, :));
    plot(rho_nn(i, :))
end
legend('120', '110', '100', '90', '80', '70', '60', ...
    '50', '40', '30', '20', '10')

hold off
figure()
hold on
boxplot(qlen_res_single, idx_single)
boxplot(qlen_res_double, idx_single)
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['r', 'b'];
for i = 1:2
    currMedians = medians(((i-1)*numMedians + 1):(i*numMedians));
    xs = zeros(numMedians, 1);
    ys = zeros(numMedians, 1);
    for j = 1:numMedians
        currMedian = currMedians(j);
        xt = currMedian.XData;
        xs(j) = mean(xt);
        yt = currMedian.YData;
        ys(j) = mean(yt);
    end
    if i == 1
        double_qlen_means = ys;
    else
        single_qlen_means = ys;
    end
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
title('Measured Queue Length of DB Connection Pool')
xlabel('Number of Clients in the System')
ylabel('Queue Length')
set(gca, 'YLim', [0 120])
legend('2 MW', '1 MW')

hold off
figure()
hold on
plot(ans)
plot(single_rt_means)

% plot number of threads for all clients
hold off
figure()
hold on

% 4 threads run on each middleware when no requests have to be served
boxplot(threads_res_single, idx_single, 'symbol', '')
boxplot(threads_res_double, idx_double, 'symbol', '')

medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['r', 'b'];
plot(1:12, 10:10:120, 'color', 'g', 'linewidth', 2)
for i = 1:2
    currMedians = medians(((i-1)*numMedians + 1):(i*numMedians));
    xs = zeros(numMedians, 1);
    ys = zeros(numMedians, 1);
    for j = 1:numMedians
        currMedian = currMedians(j);
        xt = currMedian.XData;
        xs(j) = mean(xt);
        yt = currMedian.YData;
        ys(j) = mean(yt);
    end
    if i == 1
        double_threads_means = ys;
    else
        single_threads_means = ys;
    end
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
legend('Expectation', '#Threads with 2 Middlewares', ...
    '#Threads with 1 Middleware', 'location', 'southeast')
xlabel('Number of Clients')
ylabel('Total number of Threads')
title('Number of Threads on all Middlewares')

% plot effective number of requests in mw and db
hold off
figure()
hold on

% loc_single_qlen_means = single_qlen_means;
% loc_single_threads_means = single_threads_means./2;
% qlen_idx = loc_single_qlen_means <= 1;
% db_num_req = 40*ones(length(loc_single_qlen_means), 1);
% db_num_req(qlen_idx) = db_num_req(qlen_idx).*loc_single_qlen_means(qlen_idx);
% single_total_req = flipud(db_num_req + loc_single_qlen_means + loc_single_threads_means);
% 
% loc_double_qlen_means = double_qlen_means.*2;
% loc_double_threads_means = double_threads_means./2;
% double_qlen_idx = loc_double_qlen_means <= 1;
% double_db_num_req = 40*ones(length(loc_double_qlen_means), 1);
% double_db_num_req(double_qlen_idx) = ...
%     double_db_num_req(double_qlen_idx).*loc_double_qlen_means(double_qlen_idx);
% double_total_req = flipud(double_db_num_req + loc_double_qlen_means + ...
%     loc_double_threads_means);

% plot(1:12, 10:10:120, 'color', 'green', 'linewidth', 2)
% plot(single_total_req, 'color', 'blue', 'linewidth', 2)
% plot(double_total_req, 'color', 'red', 'linewidth', 2)
% legend('Expectation', 'Estimation with 1 Middleware', ...
%     'Estimation with 2 Middlewares', 'location', 'northwest')

loc_single_qlen_means = qlen_res_single;
loc_single_threads_means = (threads_res_single - 4)./2;
qlen_idx = loc_single_qlen_means <= 1;
db_num_req = 40*ones(length(loc_single_qlen_means), 1);
db_num_req(qlen_idx) = db_num_req(qlen_idx).*loc_single_qlen_means(qlen_idx);
single_total_req = db_num_req + loc_single_qlen_means + loc_single_threads_means;

loc_double_qlen_means = qlen_res_double.*2;
loc_double_threads_means = (threads_res_double - 8)./2;
double_qlen_idx = loc_double_qlen_means <= 1;
double_db_num_req = 40*ones(length(loc_double_qlen_means), 1);
double_db_num_req(double_qlen_idx) = ...
    double_db_num_req(double_qlen_idx).*loc_double_qlen_means(double_qlen_idx);
double_total_req = double_db_num_req + loc_double_qlen_means + ...
    loc_double_threads_means';

hold off
figure()
hold on
boxplot(single_total_req, idx_single, 'symbol', '')
boxplot(double_total_req, idx_double, 'symbol', '')

medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['r', 'b'];
plot(1:12, 10:10:120, 'color', 'g', 'linewidth', 2)
for i = 1:2
    currMedians = medians(((i-1)*numMedians + 1):(i*numMedians));
    xs = zeros(numMedians, 1);
    ys = zeros(numMedians, 1);
    for j = 1:numMedians
        currMedian = currMedians(j);
        xt = currMedian.XData;
        xs(j) = mean(xt);
        yt = currMedian.YData;
        ys(j) = mean(yt);
    end
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
legend('Expectation', '2 Middlewares', ...
    '1 Middleware', 'location', 'northwest')
xlabel('Number of Clients')
ylabel('Requests in middleware or database')
title('Relation between load and number of clients')