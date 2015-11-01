clear variables

basedir = '..\..\..\ASL_LOGS\summary_db_1_60_DB_CONNS_NO_DATA\';
levels = char('level0\', 'level1\', 'level2NIND\', 'level2WIND\');
factors = [1 1 5 5];
colors = [0 0 1; 0 1 0; 1 0.5 0; 1 0 0];

numLevels = size(levels, 1);
numFactors = length(factors);

hold on

% remember how many medians were there before
oldNumMedians = 0;

for level = numLevels:-1:1
    currLevel = levels(level, :);
    % read the files of the current level
    tp = dlmread(strcat(strcat(basedir, currLevel), 'tp_summary.log'));
    lat = dlmread(strcat(strcat(basedir, currLevel), 'lat_summary.log'));
    idx = dlmread(strcat(strcat(basedir, currLevel), 'idx_summary.log'));
    % apply factor to throughput and latency
    tp = tp * factors(level);
    lat = lat * factors(level);
    % plot the data
    boxplot(tp, idx)
    % now add the 50% quantile line
    medians = findobj(gca,'tag','Median');
    numMedians = length(medians);
    myNumMedians = numMedians - oldNumMedians;
    xs = zeros(myNumMedians, 1);
    ys = zeros(myNumMedians, 1);
    for j = 1:myNumMedians
        currMedian = medians(j);
        xt = currMedian.XData;
        xs(j) = mean(xt);
        yt = currMedian.YData;
        ys(j) = mean(yt);
    end
    plot(xs, ys, 'color', colors(level, :), 'linewidth', 2)
    oldNumMedians = oldNumMedians + myNumMedians;
end
