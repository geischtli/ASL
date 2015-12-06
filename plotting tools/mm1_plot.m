% cut out 0's
mw_uncut_tp_sum = mw_uncut_tp_sum(mw_uncut_tp_sum ~= 0);
mw_uncut_tp_sum = [0; 0; mw_uncut_tp_sum];
figure()
hold on
data_tp = mw_uncut_tp_sum;
data_idx = zeros(124, 1);
for i = 1:62
    for j = 1:2
        data_idx((i-1)*2 + j) = i;
    end
end
boxplot(data_tp, data_idx)
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/1;
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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
labels = char('1', '', '', '', '10', '', '', '', '', ...
    '20', '', '', '', '', '30', '', '', '', '', ...
    '40', '', '', '', '', '50', '', '', '', '', ...
    '60', '', '', '', '', '70', '', '', '', '', ...
    '80', '', '', '', '', '90', '', '', '', '', ...
    '100', '', '', '', '', '110', '', '', '', '', ...
    '120', '', '');
title('Stability in Throughput')
xlabel('Operation Time (sec)')
ylabel('Throughput (Req/sec)')
set(gca, 'XTickLabels', labels)
set(gca, 'YLim', [0, 800])
legend('Throughput 50% Quantile')

tp_stddev = sqrt(var(mw_total_tp));

% plot also rt with wait time
hold off
figure()
hold on

% filter all NAN's out, from division by 0 tp
nonans_idx_1 = ~isnan(mw_uncut_rt(:, 1));
nonans_idx_2 = ~isnan(mw_uncut_rt(:, 2));
nonans_idx = nonans_idx_1.*nonans_idx_2;
loc_mw_uncut_rt = mw_uncut_rt(nonans_idx==1);
loc_mw_uncut_rt  = mean(loc_mw_uncut_rt , 2);
loc_mw_uncut_rt  = [43*10^7; loc_mw_uncut_rt ];
data_idx = zeros(122, 1);
for i = 1:61
    for j = 1:2
        data_idx((i-1)*2 + j) = i;
    end
end
loc_mw_uncut_rt = loc_mw_uncut_rt * 10^-6;
boxplot(loc_mw_uncut_rt, data_idx)
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/1;
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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
plot(1:122, 120./data_tp(3:124)*10^3, 'color', 'red', 'linewidth', 2, ...
    'linestyle', ':')
labels = char('1', '', '', '', '10', '', '', '', '', ...
    '20', '', '', '', '', '30', '', '', '', '', ...
    '40', '', '', '', '', '50', '', '', '', '', ...
    '60', '', '', '', '', '70', '', '', '', '', ...
    '80', '', '', '', '', '90', '', '', '', '', ...
    '100', '', '', '', '', '110', '', '', '', '', ...
    '120', '');
title('Stability in Response Time')
xlabel('Operation Time (sec)')
ylabel('Response Time (ms/Req)')
set(gca, 'XTickLabels', labels)
set(gca, 'YLim', [0, 450])
legend('Response Time 50% Quantile', 'Interactive Response Time Law')

rt_stddev = sqrt(var(loc_mw_uncut_rt(21:100)));

hold off
figure()
hold on

queue_length_data = mw_uncut_effective_queue_length_per_request;
queue_length_data = [0; queue_length_data(~isnan(queue_length_data))];

boxplot(queue_length_data, data_idx)
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/1;
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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2)
end
labels = char('1', '', '', '', '10', '', '', '', '', ...
    '20', '', '', '', '', '30', '', '', '', '', ...
    '40', '', '', '', '', '50', '', '', '', '', ...
    '60', '', '', '', '', '70', '', '', '', '', ...
    '80', '', '', '', '', '90', '', '', '', '', ...
    '100', '', '', '', '', '110', '', '', '', '', ...
    '120', '');
title('Stability in Queue Length per Middleware')
xlabel('Operation Time (sec)')
ylabel('DB Connection Queue Length per Middleware')
set(gca, 'XTickLabels', labels)
set(gca, 'YLim', [0, 45])

mean_queue_length = mean(queue_length_data(21:110));
legend('Queue Length 50% Quantile')