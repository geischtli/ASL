clear variables

labels = char('1','6','11','16','21','26','31','36','41','46','51','56');
clients = 1:5:60;
% the experiment ran for 120 seconds each, see run_db_data_experiment.sh
runtime = 120;

% the results got processed by hand into db_data_test_summary_tp_rt_tps
% and copied to this script.
rt1 = [2181	333	455	574	544	1180 1108 1278 1081 2123 2430 2046];
rt2 = [2222	357	491	539	560	1273 1544 1365 1106 1264 2557 2876];
rt3 = [2181	355	457	344	909	1160 1104 1266 1102 2096 1898 2011];

% hold on
% plot(clients, rt1)
% plot(clients, rt2)
% plot(clients, rt3)
% title('Response Time (ms)')
% hold off

tps1 = [0.44	17	24	27	38	21	27	27	37	21	20	27];
tps2 = [0.44	16	22	29	37	20	19	25	36	36	19	19];
tps3 = [0.45	17	23	46	22	22	27	28	37	21	26	27];

% figure();
% hold on
% plot(clients, tps1)
% plot(clients, tps2)
% plot(clients, tps3)
% title('Requests per Second')

tp1 = [55	2161	2900	3342	4630	2644	3356	3380	4450	2599	2518	3283];
tp2 = [54	2012	2685	3561	4500	2450	2409	3163	4448	4366	2393	2336];
tp3 = [55	2146	2887	5566	2770	2688	3367	3411	4464	2633	3223	3340];

% figure();
% hold on
% plot(clients, tp1)
% plot(clients, tp2)
% plot(clients, tp3)
% title('Total Requests per Configuration')

rt1 = rt1./runtime;
rt2 = rt2./runtime;
rt3 = rt3./runtime;

il_rt1 = clients./tp1*10^3;
il_rt2 = clients./tp2*10^3;
il_rt3 = clients./tp3*10^3;

% figure();
% hold on
% plot(clients, il_rt1)
% plot(clients, il_rt2)
% plot(clients, il_rt3)
% title('Interactive Law Response Time (ms)')

% make boxplots
rt_data = [rt1, rt2, rt3]';
rt_idx = [clients, clients, clients]';

il_rt_data = [il_rt1, il_rt2, il_rt3]';
il_rt_idx = rt_idx;

figure()
hold on
boxplot(rt_data, rt_idx);
boxplot(il_rt_data, il_rt_idx)
% plot additionally the 50% quantile (media) for readability and
% a nice legend reference
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
linestyles = ['-', ':'];
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
    plot(xs, ys, 'color', 'blue', 'linewidth', 2, ...
        'linestyle', linestyles(i))
    % store values for later comparison
    if i == 1
        il_medians = ys;
    else
        rl_medians = ys;
    end
end
title('Interactive Response Law Verification')
xlabel('Number of Clients and Database Connections')
ylabel('Response Time per Request (ms)')
legend('Interactive Response Time Law', 'Measured Response Time', ...
    'Location', 'northwest')

disp('Maximum Error in Milliseconds:')
diff = abs(il_medians - rl_medians);
max_diff = max(diff)
disp('Which accounts for percents difference w.r.t to original measurement:')
max_diff_perc = max(max_diff./rl_medians*100)