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

figure()
hold on

boxplot(tp_res_single, idx_single)
boxplot(tp_res_double, idx_double)

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
figure()
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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
legend('1 Middleware', '2 Middlewares', 'location', 'northwest')
xlabel('Number of Clients in the System')
ylabel('Response Time per Request (ms)')
title('Response Time of the whole System')

%% ALSO GO THROUGH CLIENTS FOR INTERACTIVE RESPONSE LAW VERIFICATION

% client result vars
client_res_rt_single = zeros(40*num_clients, 1);
client_res_rt_double = zeros(40*num_clients, 1);

client_res_tp_single = zeros(40*num_clients, 1);
client_res_tp_double = zeros(40*num_clients, 1);

per_mw_rt_single = zeros(num_clients, 1);
per_mw_rt_double = zeros(num_clients, 1);

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
        
        % client data vars
        total_client_rt_per_request = zeros(40, 1);
        total_thinktime_per_request = zeros(40, 1);
        total_client_tp = zeros(40, 1);
        total_client_rt = zeros(40, 1);
        per_mw_tp = zeros(40, 1);
        per_mw_rt = zeros(40, 1);
        per_mw_rt_per_request = zeros(1, 2);
        
        client_basedir = strcat(basedir, curr_folder);
        
        for k = 1:2
            curr_basedir = strcat(client_basedir, ...
                strcat('data_client', ...
                strcat(num2str(k), '\')));
            files = dir(curr_basedir);
            numFiles = length(files);

            for l = 3:3:numFiles
                curr_rt = dlmread(strcat(curr_basedir, files(l).name));
                curr_thinktime = dlmread(strcat(curr_basedir, files(l+1).name));
                curr_tp = dlmread(strcat(curr_basedir, files(l+2).name));

                curr_rt = curr_rt(11:50);
                curr_thinktime = curr_thinktime(11:50);
                curr_tp = curr_tp(11:50);

                total_client_rt_per_request = total_client_rt_per_request + curr_rt./curr_tp;
                total_thinktime_per_request = total_thinktime_per_request + ...
                    curr_thinktime./curr_tp;
                total_client_tp = total_client_tp + curr_tp;
                total_client_rt = total_client_rt + curr_rt;
                per_mw_tp = per_mw_tp + curr_tp;
                per_mw_rt = per_mw_rt + curr_rt;
            end
            per_mw_rt_per_request(k) = mean(per_mw_rt./per_mw_tp);
            per_mw_tp = zeros(40, 1);
            per_mw_rt = zeros(40, 1);
        end
        
        % calculate result matrix indices
        idx_lo = (j-1)*40 + 1;
        idx_hi = j*40;
        
        % store the result
        if i == 1
            client_res_tp_single(idx_lo:idx_hi) = total_client_tp;
            client_res_rt_single(idx_lo:idx_hi) = total_client_rt_per_request./curr_clients;
            idx_single(idx_lo:idx_hi) = 2*curr_clients;
            per_mw_rt_single(j) = mean(per_mw_rt_per_request)*10^-6;
        else
            client_res_tp_double(idx_lo:idx_hi) = total_client_tp;
            client_res_rt_double(idx_lo:idx_hi) = total_client_rt_per_request./curr_clients;
            idx_double(idx_lo:idx_hi) = 2*curr_clients;
            per_mw_rt_double(j) = mean(per_mw_rt_per_request)*10^-6;
        end
        
    end
end

% scale response time to milliseconds
client_res_rt_single = client_res_rt_single * 10^-6;
client_res_rt_double = client_res_rt_double * 10^-6;

%% PLOT IRT LAW

hold off
figure()
hold on

boxplot(client_res_tp_single, idx_single)
boxplot(client_res_tp_double, idx_double)

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
    % store ys for interactive response time law plot afterwards
    if i == 1
        client_double_tp_means = ys;
    else
        client_single_tp_means = ys;
    end
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end

legend('1 Middleware', '2 Middlewares', 'location', 'northwest')
xlabel('Number of Clients in the System')
ylabel('Requests per Second')
title('Throughput of the whole System')

hold off
figure()
hold on

% scale because we have 2 middlewares
client_res_rt_single = client_res_rt_single/2;
client_res_rt_double = client_res_rt_double/2;

boxplot(client_res_rt_single, idx_single)
boxplot(client_res_rt_double, idx_double)

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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
% also plot IRT law
il_rt_client_single = fliplr(2*clients)'./client_single_tp_means*10^3;
il_rt_client_double = fliplr(2*clients)'./client_double_tp_means*10^3;

plot(xs, il_rt_client_single, 'color', 'blue', 'linewidth', 2, 'linestyle', ':')
plot(xs, il_rt_client_double, 'color', 'red', 'linewidth', 2, 'linestyle', ':')

legend('1 Middleware Measurements', '2 Middlewares Measurements', ...
    '2 Middlewares Interactive Law', '1 Middleware Interactive Law', ...
    'location', 'northwest')
xlabel('Number of Clients in the System')
ylabel('Response Time per Request (ms)')
title('Response Time of the whole System')