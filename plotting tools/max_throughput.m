clear variables

basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\maxTP\';
dbconns = [20 22 24 26 28 30 32 34 36 38 40 50 60 70 80];
numDBConns = length(dbconns);

res = zeros(7, numDBConns);
resbox_data = zeros(10, numDBConns);
resbox_idx = zeros(10, numDBConns);

for i = 1:length(dbconns)
    foldername = strcat(num2str(dbconns(i)), '_60_120\');
    currfolder = strcat(basedir, foldername);
    mw1tp = dlmread(strcat(currfolder, 'mw1tp.log'));
    mw2tp = dlmread(strcat(currfolder, 'mw2tp.log'));
    mw1tp = find_valid_interval(mw1tp);
    mw2tp = find_valid_interval(mw2tp);
    mwtot = mw1tp + mw2tp;
    % find the valid interval for each 20-second sequence
    % ignore the first 5 seconds, and average over the 15 seconds coming
    % afterwards
    [mw_avg, mw_data, mw_idx] = find_valid_averages(mwtot);
    [val, idx] = max(mw_avg);
    dat = mw_data((idx-1)*10+1:(idx*10));
    resbox_data(:, i) = dat;
    resbox_idx(:, i) = i;
end

resbox_data = reshape(resbox_data, size(resbox_data, 1)*size(resbox_data, 2), 1);
resbox_idx = reshape(resbox_idx, size(resbox_idx, 1)*size(resbox_idx, 2), 1);

hold on
boxplot(resbox_data, resbox_idx)

medians = findobj(gca, 'tag', 'Median');
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

labels = char('20', '22', '24', '26', '28', '30', '32', ...
    '34', '36', '38', '40', '50', '60', '70', '80'); 
set(gca, 'XTickLabel', labels)
xlabel('Number of concurrent database connections')
ylabel('Number of Requests/sec')
title('Maximum Throughput evaluation')
legend('50% Quantile', 'Location', 'northwest')
% % plot additionally the 50% quantile (media) for readability and
% % a nice legend reference
% hold on
% colors = jet(numDBConns);
% medians = findobj(gca,'tag','Median');
% numMedians = length(medians)/7;
% for i = 1:7
%     currMedians = medians(((i-1)*numMedians + 1):(i*numMedians));
%     xs = zeros(numMedians, 1);
%     ys = zeros(numMedians, 1);
%     for j = 1:numMedians
%         currMedian = currMedians(j);
%         xt = currMedian.XData;
%         xs(j) = mean(xt);
%         yt = currMedian.YData;
%         ys(j) = mean(yt);
%     end
%     plot(xs, ys, 'color', colors(i, :), 'linewidth', 2)
% end
% legend('20', '30', '40', '50', '60', '70', '80')