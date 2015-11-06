%% make the plot for middleware reqPerSec
figure();
hold on
colors = ['b', 'r'];
data = {mwreqPerSec200, mwreqPerSec2000};
indices = {mwindices200, mwindices2000};
oldnumMedians = 1;
oldval = 0;
for k = 2:-1:1
    boxplot(cell2mat(data(k)), cell2mat(indices(k)), 'symbol', strcat(colors(k), '+'));
    medians = findobj(gca,'tag','Median');
    numMedians = length(medians);
    xs = zeros(numMedians, 1);
    ys = zeros(numMedians, 1);
    oldnumMedians = numMedians - oldval;
    for i = 1:oldnumMedians
        currMedian = medians(i);
        xt = currMedian.XData;
        xs(i) = mean(xt);
        yt = currMedian.YData;
        ys(i) = mean(yt);
    end
    oldval = oldnumMedians;
    plot(xs(1:oldval), ys(1:oldval), 'color', colors(k), 'linewidth', 2)
    oldnumMedians = numMedians;
end
legend('Message size 2000', 'Message size 200', 'Location', 'northwest')
xlabel('Number of concurrent clients')
ylabel({'Generated Requests per second'})
title('Client Throughput')

%% make the plot for total latency
figure();
hold on
colors = ['b', 'r'];
data = {(clreadPerSec200+clwritePerSec200)./clreqPerSec200, (clreadPerSec2000+clwritePerSec2000)./clreqPerSec2000};
indices = {mwindices200, mwindices2000};
oldnumMedians = 1;
oldval = 0;
for k = 2:-1:1
    boxplot(cell2mat(data(k)), cell2mat(indices(k)), 'symbol', strcat(colors(k), '+'));
    medians = findobj(gca,'tag','Median');
    numMedians = length(medians);
    xs = zeros(numMedians, 1);
    ys = zeros(numMedians, 1);
    oldnumMedians = numMedians - oldval;
    for i = 1:oldnumMedians
        currMedian = medians(i);
        xt = currMedian.XData;
        xs(i) = mean(xt);
        yt = currMedian.YData;
        ys(i) = mean(yt);
    end
    oldval = oldnumMedians;
    plot(xs(1:oldval), ys(1:oldval), 'color', colors(k), 'linewidth', 2)
    oldnumMedians = numMedians;
end
legend('Message size 2000', 'Message size 200', 'Location', 'northwest')
xlabel('Number of concurrent clients')
ylabel({'Response time per request (ms)'})
title('Client Latency')
set(gca, 'YLim', [0, 100])
set(gca, 'YScale', 'log')