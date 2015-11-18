clear variables

basedir = '..\..\..\ASL_LOGS\summary_db_1_60_DB_CONNS_NO_DATA\';
levels = char('level0\', 'level1\', 'level2WIND\', 'level2NIND\');
factors = [1 1 5 5];
colors = [0 0 1; 0 1 0; 1 0.5 0; 1 0 0];

numLevels = size(levels, 1);
numFactors = length(factors);

% distance between real RT and computed IL (interactive law) RT
% real world - interactive law distance
rw_il_distance_mu = zeros(4, 1);
rw_il_distance_var = zeros(4, 1);

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
    plot(xs, ys, 'color', colors(level, :), 'linewidth', 2, ...
        'linestyle', ':')
    oldNumMedians = oldNumMedians + myNumMedians;
    % plot interactive law too
    interactive_law_rt = zeros(60, 1);
    effective_lat_plotted = zeros(60, 1);
    for i = 1:60
        curr_tp = tp(idx == i);
        curr_eff_lat = eff_lat(idx == i);
        sum_curr_tp = sum(curr_tp)./length(curr_tp);
        sum_curr_eff_lat = sum(curr_eff_lat)./length(curr_eff_lat);
        interactive_law_rt(i) = i/sum_curr_tp;
        effective_lat_plotted(i) = sum_curr_eff_lat;
    end
    % work with microseconds
    interactive_law_rt = interactive_law_rt.*1000000;
    % only sum over non-nan entries
    non_nans_idx = ~isnan(effective_lat_plotted);
    diff = interactive_law_rt(non_nans_idx) ...
            - effective_lat_plotted(non_nans_idx);
    rw_il_distance_mu(level) = ...
        sqrt(sum(diff).^2)/ ...
            (60 - sum(~non_nans_idx));
    rw_il_distance_var(level) = ...
        1/(59 - sum(~non_nans_idx))*sum( ...
        (diff - rw_il_distance_mu(level)).^2);
    plot(1:60, interactive_law_rt, 'color', colors(level, :), ...
        'linewidth', 2)
end

% add legend and plot description
title('DB Latency under different query sets')
xlabel('Number of concurrent clients')
ylabel('Average latency per SQL-statement (microseconds)')
legend('Baseline Level 0', 'IL Level 0', ...
    'Baseline Level 1', 'IL Level 0', ...
    'Baseline Level 2 with indices', 'IL Level 2 with indices', ...
    'Baseline Level 2 no index', 'IL Level 2 no index', ...
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

% print the 2-norm of the error between the real data and
% the computed interactive law per level 0, 1, 2+, 2-
% averaged over all available client numbers
rw_il_distance_mu
rw_il_distance_stddev = sqrt(rw_il_distance_var);
rw_il_distance_stddev_half = rw_il_distance_stddev./2