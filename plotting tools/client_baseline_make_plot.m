close all;

% % make the plot for reqPerSec
% hold on
% colors = ['b', 'r'];
% data = {reqPerSec200, reqPerSec2000};
% indices = {indices200, indices2000};
% oldnumMedians = 1;
% oldval = 0;
% for k = 2:-1:1
%     boxplot(cell2mat(data(k)), cell2mat(indices(k)), 'symbol', strcat(colors(k), '+'));
%     medians = findobj(gca,'tag','Median');
%     numMedians = length(medians);
%     xs = zeros(numMedians, 1);
%     ys = zeros(numMedians, 1);
%     oldnumMedians = numMedians - oldval;
%     for i = 1:oldnumMedians
%         currMedian = medians(i);
%         xt = currMedian.XData;
%         xs(i) = mean(xt);
%         yt = currMedian.YData;
%         ys(i) = mean(yt);
%     end
%     oldval = oldnumMedians;
%     plot(xs(1:oldval), ys(1:oldval), 'color', colors(k), 'linewidth', 2)
%     oldnumMedians = numMedians;
% end
% legend('Message size 2000', 'Message size 200', 'Location', 'northwest')
% xlabel('Number of concurrent clients')
% ylabel({'Number of requests completed'; 'per second averaged over all concurrent clients'})
% title('Client Throughput')
% 
% %% make the plot for writePerSec
% figure();
% hold on
% colors = ['b', 'r'];
% data = {writePerSec200, writePerSec2000};
% indices = {indices200, indices2000};
% oldnumMedians = 1;
% oldval = 0;
% for k = 2:-1:1
%     boxplot(cell2mat(data(k)), cell2mat(indices(k)), 'symbol', strcat(colors(k), '+'));
%     medians = findobj(gca,'tag','Median');
%     numMedians = length(medians);
%     xs = zeros(numMedians, 1);
%     ys = zeros(numMedians, 1);
%     oldnumMedians = numMedians - oldval;
%     for i = 1:oldnumMedians
%         currMedian = medians(i);
%         xt = currMedian.XData;
%         xs(i) = mean(xt);
%         yt = currMedian.YData;
%         ys(i) = mean(yt);
%     end
%     oldval = oldnumMedians;
%     plot(xs(1:oldval), ys(1:oldval), 'color', colors(k), 'linewidth', 2)
%     oldnumMedians = numMedians;
% end
% legend('Message size 2000', 'Message size 200', 'Location', 'northwest')
% xlabel('Number of concurrent clients')
% ylabel({'Time taken for writing to the channel (ms)'; 'per second averaged over all concurrent clients'})
% title('Client Write Time')
% 
% %% make the plot for readPerSec
% figure();
% hold on
% colors = ['b', 'r'];
% data = {readPerSec200, readPerSec2000};
% indices = {indices200, indices2000};
% oldnumMedians = 1;
% oldval = 0;
% for k = 2:-1:1
%     boxplot(cell2mat(data(k)), cell2mat(indices(k)), 'symbol', strcat(colors(k), '+'));
%     medians = findobj(gca,'tag','Median');
%     numMedians = length(medians);
%     xs = zeros(numMedians, 1);
%     ys = zeros(numMedians, 1);
%     oldnumMedians = numMedians - oldval;
%     for i = 1:oldnumMedians
%         currMedian = medians(i);
%         xt = currMedian.XData;
%         xs(i) = mean(xt);
%         yt = currMedian.YData;
%         ys(i) = mean(yt);
%     end
%     oldval = oldnumMedians;
%     plot(xs(1:oldval), ys(1:oldval), 'color', colors(k), 'linewidth', 2)
%     oldnumMedians = numMedians;
% end
% legend('Message size 2000', 'Message size 200', 'Location', 'northwest')
% xlabel('Number of concurrent clients')
% ylabel({'Time taken to read from the channel (ms)'; 'per second averaged over all concurrent clients'})
% title('Client Read Time')

%% make the plot for middleware readPerSec
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
ylabel({'Number of requests completed summed over all active clients'})
title('Client Throughput')

% %% make the plot for writePerSec
% figure();
% hold on
% colors = ['b', 'r'];
% data = {mwwritePerSec200, mwwritePerSec2000};
% indices = {mwindices200, mwindices2000};
% oldnumMedians = 1;
% oldval = 0;
% for k = 2:-1:1
%     boxplot(cell2mat(data(k)), cell2mat(indices(k)), 'symbol', strcat(colors(k), '+'));
%     medians = findobj(gca,'tag','Median');
%     numMedians = length(medians);
%     xs = zeros(numMedians, 1);
%     ys = zeros(numMedians, 1);
%     oldnumMedians = numMedians - oldval;
%     for i = 1:oldnumMedians
%         currMedian = medians(i);
%         xt = currMedian.XData;
%         xs(i) = mean(xt);
%         yt = currMedian.YData;
%         ys(i) = mean(yt);
%     end
%     oldval = oldnumMedians;
%     plot(xs(1:oldval), ys(1:oldval), 'color', colors(k), 'linewidth', 2)
%     oldnumMedians = numMedians;
% end
% legend('Message size 2000', 'Message size 200', 'Location', 'northwest')
% xlabel('Number of concurrent clients')
% ylabel({'Time taken for writing to the channel (ms)'; 'per second averaged over all concurrent clients'})
% title('Middleware Write Time')
% 
% %% make the plot for readPerSec
% figure();
% hold on
% colors = ['b', 'r'];
% data = {mwreadPerSec200, mwreadPerSec2000};
% indices = {mwindices200, mwindices2000};
% oldnumMedians = 1;
% oldval = 0;
% for k = 2:-1:1
%     boxplot(cell2mat(data(k)), cell2mat(indices(k)), 'symbol', strcat(colors(k), '+'));
%     medians = findobj(gca,'tag','Median');
%     numMedians = length(medians);
%     xs = zeros(numMedians, 1);
%     ys = zeros(numMedians, 1);
%     oldnumMedians = numMedians - oldval;
%     for i = 1:oldnumMedians
%         currMedian = medians(i);
%         xt = currMedian.XData;
%         xs(i) = mean(xt);
%         yt = currMedian.YData;
%         ys(i) = mean(yt);
%     end
%     oldval = oldnumMedians;
%     plot(xs(1:oldval), ys(1:oldval), 'color', colors(k), 'linewidth', 2)
%     oldnumMedians = numMedians;
% end
% legend('Message size 2000', 'Message size 200', 'Location', 'northwest')
% xlabel('Number of concurrent clients')
% ylabel({'Time taken to read from the channel (ms)'; 'per second averaged over all concurrent clients'})
% title('Middleware Read Time')

%% make the plot for total latency
figure();
hold on
colors = ['b', 'r'];
data = {mwreadPerSec200+mwwritePerSec200, mwreadPerSec2000+mwwritePerSec2000};
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
ylabel({'Time taken to process incoming messages per second'})
title('Client Latency')