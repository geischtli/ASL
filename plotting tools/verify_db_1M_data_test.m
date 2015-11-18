clear variables

basedirA = 'C:\Users\Sandro\Documents\ASL_LOGS\level2_1M_200_5INCR_1_60\summaries\';
basedirB = 'C:\Users\Sandro\Documents\ASL_LOGS\level2_1M_2000_5INCR_1_60\summaries\';


tp200 = dlmread(strcat(basedirB, 'tp_summary.log'));
lat200 = dlmread(strcat(basedirB, 'lat_summary.log'));
idx200 = dlmread(strcat(basedirB, 'idx_summary.log'));

tp2000 = dlmread(strcat(basedirA, 'tp_summary.log'));
lat2000 = dlmread(strcat(basedirA, 'lat_summary.log'));
idx2000 = dlmread(strcat(basedirA, 'idx_summary.log'));

% gather the tp data
il_rt_200 = zeros(12, 1);
il_rt_2000 = zeros(12, 1);
numClients = 1:5:60;
j = 1;
for i = numClients
    filtered = tp200(idx200 == i);
    tp_sum = sum(filtered)/length(filtered);
    il_rt_200(j) = i/tp_sum;
    j = j + 1;
end
j = 1;
for i = numClients
    filtered = tp2000(idx2000 == i);
    tp_sum = sum(filtered)/length(filtered);
    il_rt_2000(j) = i/tp_sum;
    j = j + 1;
end

hold on
boxplot(lat200/1000000, idx200)
boxplot(lat2000/1000000, idx2000)


medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['b', 'r'];
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

title('Response Time with 10^9 entries')
xlabel('Number of concurrent connections and clients')
ylabel('Time per request (sec)')
legend('2000', '200')