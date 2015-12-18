%% make the plot for total latency
colors = ['b', 'r'];
rt_data = {(clreadPerSec200+clwritePerSec200)./clreqPerSec200, ...
    (clreadPerSec2000+clwritePerSec2000)./clreqPerSec2000};
tp_data = {mwreqPerSec200, mwreqPerSec2000};
indices = {mwindices200, mwindices2000};

% first gather the throughput over all clients
exponents_200 = 0:9;
exponents_2000 = 0:8;
clients_200 = 2.^exponents_200;
clients_2000 = 2.^exponents_2000;
il_rt_200 = zeros(length(clients_200), 1);
il_rt_2000 = zeros(length(clients_2000), 1);

% gather 200 tp
for i = clients_200
    curr_client_filter = mwindices200 == i;
    curr_client_tp = mwreqPerSec200(curr_client_filter);
    curr_tp = sum(curr_client_tp)/length(curr_client_tp);
    il_rt_200(log2(i) + 1) = i/curr_tp;
end
% gather 2000 tp
for i = clients_2000
    curr_client_filter = mwindices2000 == i;
    curr_client_tp = mwreqPerSec2000(curr_client_filter);
    curr_tp = sum(curr_client_tp)/length(curr_client_tp);
    il_rt_2000(log2(i) + 1) = i/curr_tp;
end

% we work with milliseconds
il_rt_200 = il_rt_200 * 10^3;
il_rt_2000 = il_rt_2000 * 10^3;
hold on
oldnumMedians = 1;
oldval = 0;
for k = 2:-1:1
    boxplot(cell2mat(rt_data(k)), cell2mat(indices(k)), 'symbol', '');
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
    plot(xs(1:oldval), ys(1:oldval), 'color', colors(k), 'linewidth', 2, ...
        'linestyle', ':')
    oldnumMedians = numMedians;
end

% plot interactive law response times
plot(il_rt_200, 'color', 'b', 'linewidth', 2)
plot(il_rt_2000, 'color', 'r', 'linewidth', 2)

legend('Message size 2000', 'Message size 200', ...
    'Interactive Law 200', 'Interactive Law 2000', ...
    'Location', 'northwest')
xlabel('Number of concurrent clients')
ylabel({'Response time per request (ms)'})
title('Client Latency')
set(gca, 'YLim', [0, 100])
set(gca, 'YScale', 'log')