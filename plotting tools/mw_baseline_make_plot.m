q75 = [q751CM, q752CM];
q90 = [q901CM, q902CM];
q95 = [q951CM, q952CM];
q99 = [q991CM, q992CM];

hold on
boxplot(rttData1CM/1000000, rttIdx1CM)
boxplot(rttData2CM/1000000, rttIdx2CM)
xlabel('Number of concurrent Clients')
ylabel('Response time (only within-middleware) (ms)')
title('Middleware latency behaviour')
set(gca, 'YScale', 'log')

medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['g', 'b'];
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
%plot(1:10, q90/1000000, 'color', [1 0.5 0], 'LineStyle', '--', 'linewidth', 2)
%plot(1:10, q95/1000000, 'color', [1 0.25 0], 'LineStyle', ':', 'linewidth', 2)
plot(1:10, q991CM/1000000, 'color', [0 0 1], 'LineStyle', '-.', 'linewidth', 2)
plot(1:10, q992CM/1000000, 'color', [0 1 0], 'LineStyle', '-.', 'linewidth', 2)

legend('50% quantile 2CM', ...
    '50% quantile 1CM', '99% quantile 1CM', '99% quantile 2CM', ...
    'Location', 'northwest')

hold off

figure()
hold on
boxplot(tpData1CM, tpIdx1CM)
boxplot(tpData2CM, tpIdx2CM)
xlabel('Number of concurrent Clients')
ylabel('Throughput (Requests per second)')
title('Middleware throughput behaviour')

medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['g', 'b'];
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
legend('50% quantile 2CM', '50% quantile 1CM', 'Location', 'northwest')