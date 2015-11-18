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

% mw | msgsize | dbconns | rt
res = zeros(24*num_secs, 4);
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
            
            clientData = zeros(num_secs, 1);
            for l = 1:2
                clientfolder = strcat(strcat(basedir, foldername), clientfolders(l, :));
                files = dir(clientfolder);
                for m = 3:2:(currClients+2)
                    % first rtt then tp file
                    currRtt = dlmread(strcat(clientfolder, files(m).name));
                    currTp = dlmread(strcat(clientfolder, files(m+1).name));
                    currclientData = currRtt./currTp;
                    % forget about the then first and last seconds
                    clientData = clientData + currclientData(11:50);
                end
            end
            
            run_data = [currMw, currMsgSize, currClients];
            run_data = repmat(run_data, num_secs, 1);
            res((((rowptr-1)*num_secs) + 1):(rowptr*num_secs), 1:3) = run_data;
            res((((rowptr-1)*num_secs) + 1):(rowptr*num_secs), 4) = clientData/currClients;
            
            rowptr = rowptr + 1;
        end
    end
end

% generate the plot
numRows = 24*num_secs;
numQuarterRows = numRows/4;

singleMw200_data = res(1:numQuarterRows, 4);
singleMw200_idx = res(1:numQuarterRows, 3);

singleMw2000_data = res((numQuarterRows+1):(2*numQuarterRows), 4);
singleMw2000_idx = res((numQuarterRows+1):(2*numQuarterRows), 3);

doubleMw200_data = res((2*numQuarterRows+1):(3*numQuarterRows), 4);
doubleMw200_idx = res((2*numQuarterRows+1):(3*numQuarterRows), 3);

doubleMw2000_data = res((3*numQuarterRows+1):(4*numQuarterRows), 4);
doubleMw2000_idx = res((3*numQuarterRows+1):(4*numQuarterRows), 3);

hold on
boxplot(singleMw200_data, singleMw200_idx)
boxplot(singleMw2000_data, singleMw2000_idx)

% plot additionally the 50% quantile (media) for readability and
% a nice legend reference
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['b', 'b'];
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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2, 'linestyle', linestyles(i))
end
legend('1 Middleware, Message Length 2000', ...
    '1 Middleware, Message Length 200', ...
    'Location', 'northwest')
%set(gca, 'YLim', [2, 10])
title('Response Time Behaviour')
xlabel('Number of concurrent Clients')
ylabel('Response Time per Request (ms)')

hold off
figure()
hold on

boxplot(doubleMw200_data, doubleMw200_idx)
boxplot(doubleMw2000_data, doubleMw2000_idx)

% plot additionally the 50% quantile (media) for readability and
% a nice legend reference
medians = findobj(gca,'tag','Median');
numMedians = length(medians)/2;
colors = ['b', 'b'];
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
    plot(xs, ys, 'color', colors(i), 'linewidth', 2, 'linestyle', linestyles(i))
end
legend('1 Middleware, Message Length 2000', ...
    '1 Middleware, Message Length 200', ...
    'Location', 'northwest')
%set(gca, 'YLim', [2, 10])
title('Response Time Behaviour')
xlabel('Number of concurrent Clients')
ylabel('Response Time per Request (ms)')