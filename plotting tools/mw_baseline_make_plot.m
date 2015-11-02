hold on
boxplot(rttData/1000000, rttIdx)
xlabel('Number of concurrent Clients')
ylabel('Response time (only within-middleware) (ms)')
title('Middleware latency behaviour')
set(gca, 'YScale', 'log')

medians = findobj(gca,'tag','Median');
numMedians = length(medians);
xs = zeros(numMedians, 1);
ys = zeros(numMedians, 1);
for i = 1:numMedians
    currMedian = medians(i);
    xt = currMedian.XData;
    xs(i) = mean(xt);
    yt = currMedian.YData;
    ys(i) = mean(yt);
end
plot(xs, ys, 'color', 'blue', 'linewidth', 2)
plot(1:10, q90/1000000, 'color', [1 0.5 0], 'LineStyle', '--', 'linewidth', 2)
plot(1:10, q95/1000000, 'color', [1 0.25 0], 'LineStyle', ':', 'linewidth', 2)
plot(1:10, q99/1000000, 'color', [1 0 0], 'LineStyle', '-.', 'linewidth', 2)

legend('50% quantile', '90% quantile', '95% quantile', '99% quantile', ...
    'Location', 'northwest')

hold off

figure()
hold on
boxplot(tpData, tpIdx)
xlabel('Number of concurrent Clients')
ylabel('Throughput (Requests per second)')
title('Middleware throughput behaviour')

medians = findobj(gca,'tag','Median');
numMedians = length(medians);
xs = zeros(numMedians, 1);
ys = zeros(numMedians, 1);
for i = 1:numMedians
    currMedian = medians(i);
    xt = currMedian.XData;
    xs(i) = mean(xt);
    yt = currMedian.YData;
    ys(i) = mean(yt);
end
plot(xs, ys, 'color', 'blue', 'linewidth', 2)
legend('50% quantile', 'Location', 'northwest')