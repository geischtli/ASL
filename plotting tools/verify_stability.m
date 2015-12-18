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
        client1rtt = rtt./client1tp;
    else
        client2tp = tp;
        client2rtt = rtt./client2tp;
    end
end
hold on

% collect the tp
il_tp_c1 = zeros(30, 1);
for i = 1:30
    filtered = client1tp(idx == i);
    il_tp_c1(i) = sum(filtered)/length(filtered);
end
il_tp_c2 = zeros(30, 1);
for i = 1:30
    filtered = client2tp(idx == i);
    il_tp_c2(i) = sum(filtered)/length(filtered);
end

% apply interactive response law
numClients = 60;
il_rt_c1 = numClients./il_tp_c1;
il_rt_c2 = numClients./il_tp_c2;

% we work with milliseconds
il_rt_c1 = il_rt_c1 * 10^3;
il_rt_c2 = il_rt_c2 * 10^3;

% ignore second 1:5: 265 tp
% ignore second 6:10: 9090 tp
%boxplot(client1tp(3:end), idx(3:end))
%boxplot(client2tp(3:end), idx(3:end))

%rtt = rtt./tp;
%client1rtt = client1rtt/120;
%client2rtt = client2rtt/120;

boxplot(client1rtt(3:end), idx(3:end), 'symbol', '')
boxplot(client2rtt(3:end), idx(3:end), 'symbol', '')
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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2, ...
        'linestyle', ':')
end

% plot interactive response time law results
plot(il_rt_c1, 'color', 'blue', 'linewidth', 2)
plot(il_rt_c2, 'color', 'red', 'linewidth', 2)

labels = char('1', '', '', '', '5', '', '', '', '', '10', ...
    '', '', '', '', '15', '', '', '', '', '20', ...
    '', '', '', '', '25', '', '', '', '', '30');
set(gca, 'XTickLabel', labels)
xlabel('System up time (min)')
ylabel('Response time per request (ms)')
title('Interactive Law Verification on Stability Experiment')
legend('50% Quantile of Client Machine 1', ...
    '50% Quantile of Client Machine 2', ...
    'Interactive Law Client Machine 2', ...
    'Interactive Law Client Machine 1', ...
    'Location', 'southwest')