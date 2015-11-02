clear variables

basedir = '..\..\..\ASL_LOGS\summary_db_1_60_DB_CONNS_NO_DATA\';
levels = char('level0\', 'level1\', 'level2NIND\', 'level2WIND\');
factors = [1 1 5 5];
colors = [0 0 1; 0 1 0; 1 0.5 0; 1 0 0];

numLevels = size(levels, 1);
numFactors = length(factors);

hold on

%% Throughput plot

% remember how many medians were there before
oldNumMedians = 0;

for level = numLevels:-1:1
    currLevel = levels(level, :);
    % read the files of the current level
    tp = dlmread(strcat(strcat(basedir, currLevel), 'tp_summary.log'));
    idx = dlmread(strcat(strcat(basedir, currLevel), 'idx_summary.log'));
    % apply factor to throughput
    tp = tp * factors(level);
    % plot the data
    boxplot(tp, idx)
    % now add the 50% quantile line
    medians = findobj(gca,'tag','Median');
    numMedians = length(medians);
    myNumMedians = numMedians - oldNumMedians;
    xs = [];
    ys = [];
    medianCounter = 1;
    for j = 1:myNumMedians
        currMedian = medians(j);
        yt = currMedian.YData;
        yyt = mean(yt);
        if yyt ~= 0
            ys(medianCounter) = yyt;
            xt = currMedian.XData;
            xs(medianCounter) = mean(xt);
            medianCounter = medianCounter + 1;
        end
    end
    plot(xs, ys, 'color', colors(level, :), 'linewidth', 2)
    oldNumMedians = oldNumMedians + myNumMedians;
end

% add legend and plot description
title('Database Throughput under different configurations and query sets')
xlabel('Number of concurrent clients')
ylabel('Number of SQL-statements per second')
legend('Level 2 with index', 'Level 2 no index', 'Level 1', 'Level 0', ...
    'Location', 'northwest')
%modify xticks because its too messy with standard distribution
labels = char('1', '', '', '', '5', '', '', '', '', ...
    '10', '', '', '', '', '15', '', '', '', '',...
    '20', '', '', '', '', '25', '', '', '', '', ...
    '30', '', '', '', '', '35', '', '', '', '', ...
    '40', '', '', '', '', '45', '', '', '', '', ...
    '50', '', '', '', '', '55', '', '', '', '', ...
    '60');
set(gca, 'XTickLabel', labels)

%% Latency plot
hold off
% create new window
figure()
hold on
% remember how many medians were there before
oldNumMedians = 0;

for level = numLevels:-1:1
    currLevel = levels(level, :);
    % read the files of the current level
    lat = dlmread(strcat(strcat(basedir, currLevel), 'lat_summary.log'));
    idx = dlmread(strcat(strcat(basedir, currLevel), 'idx_summary.log'));
    % apply factor to latency
    lat = lat / factors(level);
    % plot the data
    boxplot(lat, idx)
    % now add the 50% quantile line
    medians = findobj(gca,'tag','Median');
    numMedians = length(medians);
    myNumMedians = numMedians - oldNumMedians;
    xs = [];
    ys = [];
    medianCounter = 1;
    for j = 1:myNumMedians
        currMedian = medians(j);
        yt = currMedian.YData;
        yyt = mean(yt);
        if yyt ~= 0
            ys(medianCounter) = yyt;
            xt = currMedian.XData;
            xs(medianCounter) = mean(xt);
            medianCounter = medianCounter + 1;
        end
    end
    plot(xs, ys, 'color', colors(level, :), 'linewidth', 2)
    oldNumMedians = oldNumMedians + myNumMedians;
end

% add legend and plot description
title('Database Latency under different configurations and query sets')
xlabel('Number of concurrent clients')
ylabel('Number of SQL-statements per second')
legend('Level 2 with index', 'Level 2 no index', 'Level 1', 'Level 0', ...
    'Location', 'northwest')
%modify xticks because its too messy with standard distribution
labels = char('1', '', '', '', '5', '', '', '', '', ...
    '10', '', '', '', '', '15', '', '', '', '',...
    '20', '', '', '', '', '25', '', '', '', '', ...
    '30', '', '', '', '', '35', '', '', '', '', ...
    '40', '', '', '', '', '45', '', '', '', '', ...
    '50', '', '', '', '', '55', '', '', '', '', ...
    '60');
set(gca, 'XTickLabel', labels)