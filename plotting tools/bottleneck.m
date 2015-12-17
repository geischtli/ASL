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
        
        % run through the log files of the middleware(s)
        mw_log_dirs = char('data_mw1\', 'data_mw2\');
        for k = 1:i
            % concat all path strings
            log_folder = strcat(basedir, ...
                strcat(curr_folder, mw_log_dirs(k, :)));
            
            % read the logs
            curr_tp = dlmread(strcat(log_folder, 'throughput.log'));
            curr_rt = dlmread(strcat(log_folder, 'rtt.log'));
            
            % sum them up
            total_mw_tp = total_mw_tp + curr_tp(int_start:int_end);
            total_mw_rt_per_request = total_mw_rt_per_request + ...
                curr_rt(int_start:int_end)./curr_tp(int_start:int_end);
            total_mw_rt = total_mw_rt + curr_rt(int_start:int_end);
        end
        
        % calculate result matrix indices
        idx_lo = (j-1)*40 + 1;
        idx_hi = j*40;
        
        % store the result
        if i == 1
            tp_res_single(idx_lo:idx_hi) = total_mw_tp;
            %rt_res_single(idx_lo:idx_hi) = total_mw_rt_per_request;
            rt_res_single(idx_lo:idx_hi) = total_mw_rt./total_mw_tp;
            idx_single(idx_lo:idx_hi) = 2*curr_clients;
        else
            tp_res_double(idx_lo:idx_hi) = total_mw_tp;
            %rt_res_double(idx_lo:idx_hi) = total_mw_rt_per_request;
            rt_res_double(idx_lo:idx_hi) = total_mw_rt./total_mw_tp;
            idx_double(idx_lo:idx_hi) = 2*curr_clients;
        end
    end
end

% scale the response time to millisseconds
rt_res_single = rt_res_single*10^-6;
rt_res_double = rt_res_double*10^-6;

%% GENERATE PLOTS

h_tp = figure();
hold on

boxplot(tp_res_single, idx_single)

medians = findobj(gca,'tag','Median');
numMedians = length(medians)/1;
colors = ['b', 'b'];
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
    % store ys for interactive response time law plot afterwards
    if i == 1
        double_tp_means = ys;
    else
        single_tp_means = ys;
    end
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
legend('1 Middleware', '2 Middlewares', 'location', 'northwest')
xlabel('Number of Clients in the System')
ylabel('Requests per Second')
title('Throughput of the whole System')

hold off
h_rt = figure();
hold on

boxplot(rt_res_single, idx_single)
boxplot(rt_res_double, idx_double)

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
    if i == 2
        single_rt_means = ys;
    end
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
legend('1 Middleware', '2 Middlewares', 'location', 'northwest')
xlabel('Number of Clients in the System')
ylabel('Response Time per Request (ms)')
title('Response Time of the whole System')

%% BOTTLENECK ANALYSIS

% st = service time
db_st = [2.59470428849696, 2.77807451457582, 2.7920286234508, ...
    2.85112480679021, 3.27020155519635, 3.1181418697483, ...
    2.98979616021612, 3.02723271011188, 3.19499897068213, ...
    2.93770492954681, 3.02089042230959, 3.11145817103992].*10^-3';

db_st = db_st - 1*10^-3;

mw_st = [0.136440093153641, 0.147458767805334, 0.154162826632399, ...
    0.152933225136665, 0.29867159994474, 0.358013076991996, ...
    0.414257624175653, 0.379270762083106, 0.502354988084108, ...
    0.410739758470062, 0.439868421004741, 0.472214474167165].*10^-3';

D_db = db_st;
D_mw = mw_st;

D = D_db + D_mw;
D_max = D_db;
Z = 0.0020*10^-3;
N = 10:10:120;

client_factors = [10 20 30 30 30 30 30 30 30 30 30 30];
f1 = (N./(D + Z)).*client_factors;
f2 = (1./D_max).*client_factors;

tp_bond = min(f1, f2);
rt_bond = max(D.*client_factors, N.*D_max - Z);

figure(h_tp);
hold on
plot(tp_bond, 'color', 'red', 'linewidth', 2)
set(gca, 'YLim', [0, 22000])
legend('1 Middleware', 'Asymptotic Bound', ...
    'location', 'northwest')

figure(h_rt)
hold on
plot(rt_bond, 'color', 'green', 'linewidth', 2)
set(gca, 'YLim', [0, 9])