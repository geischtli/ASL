clear variables
basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\stability\stability_client';

tp = zeros(360, 1);
rtt = zeros(360, 1);
% plot in 10 second intervals -> merge two columns
idx = zeros(360, 1);
j = 1;
for i = 1:360
    idx(i) = j;
    if mod(i, 12) == 0
        j = j + 1;
    end
end

numClients = 60;

% we have an average of 5 seconds, to sum up we have to rescale it
% accordingly. This was done to reduce size of log files
factor = 5;

for currClient = 1:2
    currDir = strcat(basedir, strcat(num2str(currClient), '\'));
    files = dir(currDir);
    numFiles = length(files);
    tp = zeros(360, 1);
    rtt = zeros(360, 1);
    for currFile = 3:numFiles
        filename = files(currFile).name;
        tpstr = filename(end-5:end-4);
        rttstr = filename(end-6:end-4);
        data = dlmread(strcat(currDir, filename));
        %data = factor * data;
        datalen = length(data);
        if strcmp(tpstr, 'tp')
            tp(1:datalen) = tp(1:datalen) + data;
        elseif strcmp(rttstr, 'rtt')
            rtt(1:datalen) = rtt(1:datalen) + data;
        end
    end
    if currClient == 1
        client1tp = tp;
        client1rtt = rtt/numClients;
    else
        client2tp = tp;
        client2rtt = rtt/numClients;
    end
end
hold on

% ignore second 1:5: 265 tp
% ignore second 6:10: 9090 tp
boxplot(client1tp(3:end), idx(3:end))
boxplot(client2tp(3:end), idx(3:end))
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['r', 'b'];
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

labels = char('1', '', '', '', '5', '', '', '', '', '10', ...
    '', '', '', '', '15', '', '', '', '', '20', ...
    '', '', '', '', '25', '', '', '', '', '30');
set(gca, 'XTickLabel', labels)
xlabel('System up time (min)')
ylabel('Requests per second')
title('Full System Throughput Client View')

hold off
figure()
hold on
rtt = rtt./tp;
client1rtt = client1rtt/120;
client2rtt = client2rtt/120;

boxplot(client1rtt(3:end), idx(3:end))
boxplot(client2rtt(3:end), idx(3:end))
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['r', 'b'];
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

labels = char('1', '', '', '', '5', '', '', '', '', '10', ...
    '', '', '', '', '15', '', '', '', '', '20', ...
    '', '', '', '', '25', '', '', '', '', '30');
set(gca, 'XTickLabel', labels)
xlabel('System up time (min)')
ylabel('Response time per request (ms)')
title('Full System Response Time')
legend('50% Quantile of Client Machine 1', ...
    '50% Quantile of Client Machine 2', ...
    'Location', 'northwest')

% do some cross-checking with middleware data
mw1tp = dlmread('C:\Users\Sandro\Documents\ASL_LOGS\stability\stability_mw1\throughput.log');
mw2tp = dlmread('C:\Users\Sandro\Documents\ASL_LOGS\stability\stability_mw2\throughput.log');
mwtp = mw1tp + mw2tp;
%mwtp = mwtp * factor;

mwidx = zeros(381, 1);
j = 1;
for i = 1:381
    mwidx(i) = j;
    if mod(i, 12) == 0
        j = j + 1;
    end
end

figure()

boxplot(mwtp, mwidx)

hold on

medians = findobj(gca,'tag','Median');
numMedians = length(medians);
xs = zeros(numMedians, 1);
ys = zeros(numMedians, 1);
for j = 1:numMedians
    currMedian = medians(j);
    xt = currMedian.XData;
    xs(j) = mean(xt);
    yt = currMedian.YData;
    ys(j) = mean(yt);
end
plot(xs, ys, 'color', 'blue', 'linewidth', 2)

labels = char('1', '', '', '', '5', '', '', '', '', '10', ...
    '', '', '', '', '15', '', '', '', '', '20', ...
    '', '', '', '', '25', '', '', '', '', '30', '', '', '', '', '');
set(gca, 'XTickLabel', labels)
xlabel('System up time (min)')
ylabel('Number of requests per second')
title('Full System Throughput')
legend('50% Quantile', ...
    'Location', 'northwest')