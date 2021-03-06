clear variables
close all

basedir = '..\..\..\ASL_LOGS\summary_db_1_60_DB_CONNS_NO_DATA\';
levels = char('level0\', 'level1\', 'level2WIND\', 'level2NIND\');
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
    if level == 4
        tp4_means = fliplr(ys)';
    elseif level == 3
        tp3_means = fliplr(ys)';
    elseif level == 2
        tp2_means = fliplr(ys)';
    else
        tp1_means = fliplr(ys)';
    end
    plot(xs, ys, 'color', colors(level, :), 'linewidth', 2)
    oldNumMedians = oldNumMedians + myNumMedians;
end

% add legend and plot description
title('DB Throughput under different query sets')
xlabel('Number of concurrent clients')
ylabel('Number of SQL-statements per second')
legend('Level 2 no index', 'Level 2 with indices', 'Level 1', 'Level 0', ...
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

for level = 1:numLevels
    currLevel = levels(level, :);
    % read the files of the current level
    tp = dlmread(strcat(strcat(basedir, currLevel), 'tp_summary.log'));
    lat = dlmread(strcat(strcat(basedir, currLevel), 'lat_summary.log'));
    idx = dlmread(strcat(strcat(basedir, currLevel), 'idx_summary.log'));
    % apply factor to throughput
    tp = tp * factors(level);
    % get the actual latency
    eff_lat = lat./tp;
    % plot the data
    boxplot(eff_lat, idx)
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
        if ~isnan(yyt)
            ys(medianCounter) = yyt;
            xt = currMedian.XData;
            xs(medianCounter) = mean(xt);
            medianCounter = medianCounter + 1;
        end
    end
    if level == 4
        rt4_means = fliplr(ys)';
    elseif level == 3
        rt3_means = fliplr(ys)';
    elseif level == 2
        rt2_means = fliplr(ys)';
    else
        rt1_means = fliplr(ys)';
    end
    plot(xs, ys, 'color', colors(level, :), 'linewidth', 2)
    oldNumMedians = oldNumMedians + myNumMedians;
end

% add legend and plot description
title('DB Latency under different query sets')
xlabel('Number of concurrent clients')
ylabel('Average latency per SQL-statement (microseconds)')
legend('Level 0', 'Level 1', 'Level 2 with indices', 'Level 2 no index', ...
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

ms = 1:80;
for m = ms
    rt_means = rt3_means;
    tp_means = tp3_means;

    rt_means = rt_means .* 10^-6;
    response_time = rt_means;

    arrival_rate = tp_means;
    service_time = rt_means;

    service_rate = 1./service_time;

    rho = arrival_rate./(m*service_rate);
    rho0 = calculate_rho0(rho, m);
    l = ((m.*rho).^m./(factorial(m)*(1-rho))).*rho0;
    Er = 1./service_rate.*(1 + (l)./(m.*(1-rho)));
    plot(arrival_rate, Er, 'r',  'linewidth', 2, 'color', 'blue')
    err(m) = norm(response_time - Er);
    rho_i(:, m) = rho;
end

 m = 60;

rt_means = rt3_means;
tp_means = tp3_means;

rt_means = rt_means .* 10^-6;
response_time = rt_means;

arrival_rate = tp_means;
service_time = rt_means;

service_rate = 1./service_time;

rho = arrival_rate./(m*service_rate);
rho0 = calculate_rho0(rho, m);
l = ((m.*rho).^m./(factorial(m)*(1-rho))).*rho0;
Er = 1./service_rate.*(1 + (l)./(m.*(1-rho)));
plot(arrival_rate, Er, 'r',  'linewidth', 2, 'color', 'blue')

% mean waiting time
Ew = l./(m.*service_rate.*(1 - rho));

% q-percentile of the waiting time
q_perc_90 = (Ew./l).*log((100.*l)./(100-90));
q_wait_90 = max([zeros(size(q_perc_90)), q_perc_90], [], 2);


hold off
figure()
hold on

plot(arrival_rate, response_time, 'linewidth', 2, 'color', 'blue')
plot(arrival_rate, Er, 'linewidth', 2, 'color', 'red')
legend('Measurements', 'Model', 'location', 'northwest')
xlabel('Arrival Rate (Requests per Second)')
ylabel('Response Time (ms)')
title('Fitting of the M/M/30 queueing model')
set(gca, 'YLim', [0, 1.4])