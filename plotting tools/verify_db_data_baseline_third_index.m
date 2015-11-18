clear variables

basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\third_index_db_baseline\';
files = dir(basedir);
numFiles = length(files);

%% First compute the original response time
% concat all rows and concat the seconds and microseconds, such that
% we later can sort on this column
data = [];
for file = 6:numFiles-1
    currFile = files(file).name;
    currData = dlmread(strcat(basedir, currFile));
    [m, ~] = size(currData);
    mergedData = zeros(m, 6);
    mergedData(:, 1:4) = currData(:, 1:4);
    mergedData(:, 5) = currData(:, 5).*10^6;
    mergedData(:, 5) = mergedData(:, 5) + currData(:, 6);
    % append to other data
    data = [data; mergedData];
end

% now sort by the concatenated time column
data = sortrows(data, 5);

% 6th column is used to keep track of database size
% file index = 0 -> 10 inserts, = 1 -> 1 delete
numRows = size(data, 1);
for i = 2:numRows
    if data(i, 4) == 1
        data(i, 6) = data(i-1, 6) + 10;
    else
        if data(i-1, 6) > 0
            data(i, 6) = data(i-1, 6) -1;
        end
    end
end

% only look at 10-second intervals (else plot is too messy)
data(:, 5) = round(data(:, 5)./10^7);
% start at second 0
data(:, 5) = data(:, 5) - min(data(:, 5));
% work with milliseconds
data(:, 3) = round(data(:, 3)/10^3);
% get all inserts
inserts = data(data(:, 4) == 0, :);
% get all deletes
deletes = data(data(:, 4) == 1, :);

frac_two_indices = data(:, 3)./data(:, 6);

% now plot the data
hold on

boxplot(deletes(:, 3), deletes(:, 5), 'symbol', 'r+')

% plot additionally the 50% quantile (media) for readability and
% a nice legend reference
medians = findobj(gca,'tag','Median');
numMedians = length(medians);
xs = zeros(numMedians, 1);
ys = zeros(numMedians, 1);
for j = 1:numMedians
    currMedian = medians(j);
    xt = currMedian.XData;
    xs(j) = mean(xt);
    yt = currMedian.YData;
    ys(j) = mean(yt);
end
plot(xs, ys, 'color', 'red', 'linewidth', 2)

%% Now compute the interactive response time law
% clients used in this experiments
numClients = 40;

% concat all rows and concat the seconds and microseconds, such that
% we later can sort on this column
data = [];
for file = 6:numFiles-1
    currFile = files(file).name;
    currData = dlmread(strcat(basedir, currFile));
    [m, ~] = size(currData);
    mergedData = zeros(m, 6);
    mergedData(:, 1:4) = currData(:, 1:4);
    mergedData(:, 5) = currData(:, 5).*10^6;
    mergedData(:, 5) = mergedData(:, 5) + currData(:, 6);
    % append to other data
    data = [data; mergedData];
end

% now sort by the concatenated time column
data = sortrows(data, 5);

% 6th column is used to keep track of throughput
% file index = 0 -> 10 inserts, = 1 -> 1 delete
numRows = size(data, 1);
for i = 1:numRows
    if data(i, 4) == 1
        data(i, 6) = 0;
    else
        data(i, 6) = 1;
    end
end

% only look at 10-second intervals (else plot is too messy)
data(:, 5) = round(data(:, 5)./10^7);
% start at second 0
data(:, 5) = data(:, 5) - min(data(:, 5));
% work with milliseconds
data(:, 3) = round(data(:, 3)/10^3);

% we have 30 10-second intervals
tp = zeros(35, 1);

% sum up the throughput of each 10-second interval
for i = 1:numRows
    tp(data(i, 5) + 1) = tp(data(i, 5) + 1) + data(i, 6);
end

% we work with 10-second intervals, so scale tp accordingly
tp = tp./10;
% use interactive response time law
rt = numClients./tp;

% we work with milliseconds
rt = rt.*10^3;

% according to log files, we some ms are due to BEGIN and END
% statement latencies in milliseconds, file 2:
%	0.212013	BEGIN
%	176.779195		SELECT * FROM remove_top_message_from_queue(1, 1);
%	5.564781	END;
% Try to find an estimation for this (pice-wise linear)

% Z = approx. think time at 0, 100k, 300k, 500k entries
% see db_baseline_thinktime summary for overview 
Z = [4 6 11 19];
a = Z(1);
b = Z(2);
c = Z(3);
d = Z(4);
% 100k hit at 10 seconds
% 300k hit at 70 seconds
% 500k hit at 240 seconds
Zs = [a b b b b b b c c c c, ... % 100 seconds
    c c c c c c c c c c, ... % 200 seconds
    c c c d d d d d d d d d d d]'; % 300 seconds
rt = rt - Zs;

% plot response time from interactive law
plot(rt, 'color', 'blue', 'linewidth', 2)

%% Also get the active number of clients to prove the point
% of early termination of some clients
first_actions = data(data(:, 2) == 0, :);
last_actions = data(data(:, 2) == 2999, :);

% we have 40 clients active during most of the time
active_clients = ones(35, 1)*40;
% set ending numbers
active_clients(33) = 40 - ...
    length(last_actions(last_actions(:, 5) == 31));
active_clients(34) = active_clients(32) - ...
    length(last_actions(last_actions(:, 5) == 32));
active_clients(35) = active_clients(33) - ...
    length(last_actions(last_actions(:, 5) == 33));

% scale this line to make an appealing plot
% the values are corrected by second axis
active_clients = active_clients * 12.5;

plot(active_clients, 'color', 'green', 'linewidth', 2)

%% Finish the plot
% create legend
legend('remove top message response time', ...
    'Interactive response time law', ...
    'Active Clients', ...
    'Location', 'northwest')
xlabel('Operation time (seconds)')
ylabel('Time needed to complete action (ms)')
title('Behaviour of the remove-top-most query')

% set xticks before confusing everything with second yaxis
xticks = char('0', '', '20', '', '40', '', '60', '', '80', '', ...
    '100', '', '120', '', '140', '', '160', '', '180', '', ...
    '200', '', '220', '', '240', '', '260', '', '280', '', '300', '', ...
    '320', '', '340');
set(gca, 'XTickLabel', xticks)
set(gca, 'XLim', [0 35])

set(gca,'Box', 'off');
axesPosition = get(gca, 'Position');
hyNewAxes = axes('Position', axesPosition,...
                'Color', 'none',...
                'YLim', [0 60],...
                'YAxisLocation', 'right',...
                'XTick', [], ...
                'YTick', [0 10 20 30 40 50 60], ...
                'Box', 'off');
ylabel(hyNewAxes,'Number of active Clients');