close all;

% make the plot for reqPerSec
hold on
colors = ['b', 'r'];
data = {reqPerSec200, reqPerSec2000};
indices = {indices200, indices2000};
oldnumMedians = 1;
oldval = 0;
for k = 1:2
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
legend('Message size 200', 'Message size 2000', 'Location', 'northwest')
xlabel('Number of concurrent clients')
ylabel({'Number of requests completed'; 'per second averaged over all concurrent clients'})
title('Client Throughput')

%% make the plot for writePerSec
figure();
hold on
colors = ['b', 'r'];
data = {writePerSec200, writePerSec2000};
indices = {indices200, indices2000};
oldnumMedians = 1;
oldval = 0;
for k = 1:2
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
legend('Message size 200', 'Message size 2000', 'Location', 'northwest')
xlabel('Number of concurrent clients')
ylabel({'Time taken for writing to the channel (ms)'; 'per second averaged over all concurrent clients'})
title('Client Write Time')

%% make the plot for readPerSec
figure();
hold on
colors = ['b', 'r'];
data = {writePerSec200, writePerSec2000};
indices = {indices200, indices2000};
oldnumMedians = 1;
oldval = 0;
for k = 1:2
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
legend('Message size 200', 'Message size 2000', 'Location', 'northwest')
xlabel('Number of concurrent clients')
ylabel({'Time taken to read from the channel (ms)'; 'per second averaged over all concurrent clients'})
title('Client Read Time')