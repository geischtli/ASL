clear variables

%basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\db_data_baseline\';
basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\third_index_db_baseline\';
files = dir(basedir);
numFiles = length(files);

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

% now plot the data
hold on

% scale the table size data such that the plot looks appealing
% intentionally wrong output, but use text to set marks
data(:, 6) = data(:, 6)./983.085454545455;
boxplot(data(:, 6), data(:, 5), 'symbol', 'g+')

boxplot(inserts(:, 3), inserts(:, 5), 'symbol', 'b+')
boxplot(deletes(:, 3), deletes(:, 5), 'symbol', 'r+')

% plot additionally the 50% quantile (media) for readability and
% a nice legend reference
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/3;
colors = ['r', 'b', 'g'];
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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end

% create legend
legend('remove top message', ...
    'insert 10 new messages', ...
    'size of message table', 'Location', 'northwest')
xlabel('Operation time (seconds)')
ylabel('Time needed to complete action (ms)')
title('Behaviour of the remove-top-most query')

% set xticks before confusing everything with second yaxis
xticks = char('0', '', '20', '', '40', '', '60', '', '80', '', ...
    '100', '', '120', '', '140', '', '160', '', '180', '', ...
    '200', '', '220', '', '240', '', '260', '', '280', '', '300');
set(gca, 'XTickLabel', xticks)
set(gca, 'XLim', [0 32])

set(gca,'Box', 'off');
axesPosition = get(gca, 'Position');
hyNewAxes = axes('Position', axesPosition,...
                'Color', 'none',...
                'YLim', [0 7.4],...
                'YAxisLocation', 'right',...
                'XTick', [], ...
                'YTick', [1 2 3 4 5 6 7], ...
                'Box', 'off');
ylabel(hyNewAxes,'Message Table size * 10^5(# of entries)');