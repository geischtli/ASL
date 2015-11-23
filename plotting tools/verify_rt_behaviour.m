clear variables

basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\RT behaviour\';

mws = [1 2];
msgSizes = [200 2000];
clients = [20 40 60 80 100 120];
clientfolders = char('\data_client1\', '\data_client2\');

numMws = length(mws);
numMsgSizes = length(msgSizes);
numClients = length(clients);

% stable seconds
num_secs = 40;

% mw | msgsize | dbconns | rt or tp
res_rt = zeros(24*num_secs, 4);
res_tp = zeros(24*num_secs, 4);
rowptr = 1;

for i = 1:numMws
    for j = 1:numMsgSizes
        for k = 1:numClients
            currMw = mws(i);
            currMsgSize = msgSizes(j);
            currClients = clients(k);
            
            mwstr = strcat(num2str(currMw), '_');
            msgSizeStr = strcat(num2str(currMsgSize), '_');
            clientStr = num2str(currClients);
            
            foldername = strcat(strcat(mwstr, msgSizeStr), clientStr);
            
            clientData_rt = zeros(num_secs, 1);
            clientData_tp = zeros(num_secs, 1);
            for l = 1:2
                clientfolder = strcat(strcat(basedir, foldername), clientfolders(l, :));
                files = dir(clientfolder);
                for m = 3:2:(currClients+2)
                    % first rtt then tp file
                    currRtt = dlmread(strcat(clientfolder, files(m).name));
                    currTp = dlmread(strcat(clientfolder, files(m+1).name));
                    currclientData = currRtt./currTp;
                    % forget about the then first and last seconds
                    clientData_rt = clientData_rt + currclientData(11:50);
                    clientData_tp = clientData_tp + currTp(11:50);
                end
            end
            
            run_data = [currMw, currMsgSize, currClients];
            run_data = repmat(run_data, num_secs, 1);
            res_rt((((rowptr-1)*num_secs) + 1):(rowptr*num_secs), 1:3) = run_data;
            res_rt((((rowptr-1)*num_secs) + 1):(rowptr*num_secs), 4) = clientData_rt/currClients;
            
            res_tp((((rowptr-1)*num_secs) + 1):(rowptr*num_secs), 1:3) = run_data;
            res_tp((((rowptr-1)*num_secs) + 1):(rowptr*num_secs), 4) = clientData_tp;
            
            rowptr = rowptr + 1;
        end
    end
end

% generate the plot
numRows = 24*num_secs;
numQuarterRows = numRows/4;

singleMw200rt_data = res_rt(1:numQuarterRows, 4);
singleMw200tp_data = res_tp(1:numQuarterRows, 4);
singleMw200_idx = res_rt(1:numQuarterRows, 3);

singleMw2000rt_data = res_rt((numQuarterRows+1):(2*numQuarterRows), 4);
singleMw2000tp_data = res_tp((numQuarterRows+1):(2*numQuarterRows), 4);
singleMw2000_idx = res_rt((numQuarterRows+1):(2*numQuarterRows), 3);

doubleMw200rt_data = res_rt((2*numQuarterRows+1):(3*numQuarterRows), 4);
doubleMw200tp_data = res_tp((2*numQuarterRows+1):(3*numQuarterRows), 4);
doubleMw200_idx = res_rt((2*numQuarterRows+1):(3*numQuarterRows), 3);

doubleMw2000rt_data = res_rt((3*numQuarterRows+1):(4*numQuarterRows), 4);
doubleMw2000tp_data = res_tp((3*numQuarterRows+1):(4*numQuarterRows), 4);
doubleMw2000_idx = res_rt((3*numQuarterRows+1):(4*numQuarterRows), 3);

h_single_mw = figure();
hold on

boxplot(singleMw200rt_data, singleMw200_idx)
boxplot(singleMw2000rt_data, singleMw2000_idx)

% plot additionally the 50% quantile (media) for readability and
% a nice legend reference
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['b', 'r'];
linestyles = [':', ':'];
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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2, 'linestyle', linestyles(i))
end

hold off
h_double_mw = figure();
hold on

boxplot(doubleMw200rt_data, doubleMw200_idx)
boxplot(doubleMw2000rt_data, doubleMw2000_idx)

% plot additionally the 50% quantile (media) for readability and
% a nice legend reference
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['b', 'r'];
linestyles = [':', ':'];
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
        'linestyle', linestyles(i))
end
% do the verification with the interactive response time law
il_rt = res_rt;
il_rt(:, 4) = res_rt(:, 3)./res_tp(:, 4)*10^3;

% generate the plot
numRows = 24*num_secs;
numQuarterRows = numRows/4;

singleMw200rt_data = il_rt(1:numQuarterRows, 4);
singleMw200_idx = il_rt(1:numQuarterRows, 3);

singleMw2000rt_data = il_rt((numQuarterRows+1):(2*numQuarterRows), 4);
singleMw2000_idx = il_rt((numQuarterRows+1):(2*numQuarterRows), 3);

doubleMw200rt_data = il_rt((2*numQuarterRows+1):(3*numQuarterRows), 4);
doubleMw200_idx = il_rt((2*numQuarterRows+1):(3*numQuarterRows), 3);

doubleMw2000rt_data = il_rt((3*numQuarterRows+1):(4*numQuarterRows), 4);
doubleMw2000_idx = il_rt((3*numQuarterRows+1):(4*numQuarterRows), 3);

hold off
figure(h_single_mw)
hold on
boxplot(singleMw200rt_data, singleMw200_idx)
boxplot(singleMw2000rt_data, singleMw2000_idx)

% plot additionally the 50% quantile (media) for readability and
% a nice legend reference
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/4;
colors = ['b', 'r'];
linestyles = ['-', '-'];
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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2, 'linestyle', linestyles(i))
end
legend('1 Middleware, Message Length 2000 measured', ...
    '1 Middleware, Message Length 200 measured', ...
    '1 Middleware, Message Length 2000 IL', ...
    '1 Middleware, Message Length 200 IL', ...
    'Location', 'northwest')
%set(gca, 'YLim', [2, 10])
title('Response Time Behaviour Verification')
xlabel('Number of concurrent Clients')
ylabel('Response Time per Request (ms)')

hold off
figure(h_double_mw)
hold on

boxplot(doubleMw200rt_data, doubleMw200_idx)
boxplot(doubleMw2000rt_data, doubleMw2000_idx)

% plot additionally the 50% quantile (media) for readability and
% a nice legend reference
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/4;
colors = ['b', 'r'];
linestyles = ['-', '-'];
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
        'linestyle', linestyles(i))
end
legend('2 Middlewares, Message Length 2000 measured', ...
    '2 Middlewares, Message Length 200 measured', ...
    '2 Middlewares, Message Length 2000 IL', ...
    '2 Middlewares, Message Length 200 IL', ...
    'Location', 'northwest')
%set(gca, 'YLim', [2, 10])
title('Response Time Behaviour Verification')
xlabel('Number of concurrent Clients')
ylabel('Response Time per Request (ms)')