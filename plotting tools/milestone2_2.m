% The scalability data was gather for milestone 2 for exercise 2. The goal
% of this script is to give an overview about the data, and to allow
% in a next step to plot additional M/M/m lines either veri- or 
% falsify the corresponding models.
clear variables

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
            
            % sum them up
            total_mw_tp = total_mw_tp + curr_tp(int_start:int_end);
            total_mw_rt_per_request = total_mw_rt_per_request + ...
                curr_rt(int_start:int_end)./curr_tp(int_start:int_end);
            total_mw_rt = total_mw_rt + curr_rt(int_start:int_end);
            total_mw_wait = total_mw_wait + curr_wait(int_start:int_end);
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
            idx_single(idx_lo:idx_hi) = 2*curr_clients;
        else
            tp_res_double(idx_lo:idx_hi) = total_mw_tp;
            %rt_res_double(idx_lo:idx_hi) = total_mw_rt_per_request;
            rt_res_double(idx_lo:idx_hi) = total_mw_rt./total_mw_tp;
            wait_res_double(idx_lo:idx_hi) = total_mw_wait./total_mw_tp;
            idx_double(idx_lo:idx_hi) = 2*curr_clients;
        end
    end
end

% scale the response time to millisseconds
rt_res_single = rt_res_single*10^-6;
rt_res_double = rt_res_double*10^-6;
wait_res_single = wait_res_single*10^-6;
wait_res_double = wait_res_double*10^-6;

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

medians = findobj(gca,'tag','Median');
numMedians = length(medians)/3;
colors = ['g', 'r', 'b'];
for i = 1:3
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
        single_service_means = ys;
    elseif i == 2
        single_wait_means = ys;
    else
        single_rt_means = ys;
    end
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end

legend('Service Time', 'Wait Time', 'Response Time', 'location', 'northwest')
xlabel('Number of Clients in the System')
ylabel('Milliseconds')
title('Timings of the whole System')
set(gca, 'YLim', [0, 9])

% calculate other model-related numbers
m = 40;
%traffic intensity
mu = 1./(single_service_means.*10^-3);
rho = single_tp_means./(40*(mu));
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

% normalize and plot rho_n
hold off
figure()
hold on
for i = 1:12
    rho_nn(i, :) = rho_n(i, :)./sum(rho_n(i, :));
    plot(rho_nn(i, :))
end
legend('120', '110', '100', '90', '80', '70', '60', ...
    '50', '40', '30', '20', '10')