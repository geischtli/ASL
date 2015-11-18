% gather the tp per 10-second interval
numRows = length(tpData1CM);
tps = zeros(10, 2);
for j = 1:2
    for i = 10:10:100
        idx = i/10;
        if j == 1
           	tps(idx, j) = sum(tpData1CM(tpIdx == i));
        else
            tps(idx, j) = sum(tpData2CM(tpIdx == i));
        end
    end
end
% we have 31 seconds per number of clients
tps = tps./31;

% use interactive response law
numClients = (10:10:100)';
rt1cm = numClients./tps(:, 1);
rt2cm = numClients./tps(:, 2);

% we work with milliseconds, so scale accordingly
rt1cm = rt1cm * 10^3;
rt2cm = rt2cm * 10^3;


hold on
boxplot(rttData1CM/1000000, rttIdx1CM)
boxplot(rttData2CM/1000000, rttIdx2CM)
xlabel('Number of concurrent Clients')
ylabel('Response time (only within-middleware) (ms)')
title('Middleware latency behaviour')
%set(gca, 'YScale', 'log')

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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2, ...
        'linestyle', ':')
end

% plot the computed response by the interactive law
plot(rt1cm, 'color', 'blue', 'linewidth', 2)
plot(rt2cm, 'color', 'green', 'linewidth', 2)

legend('50% quantile 2CM', ...
    '50% quantile 1CM', 'IL 1CM', 'IL 2CM', ...
    'Location', 'northwest')

set(gca, 'YLim', [0.5, 4])