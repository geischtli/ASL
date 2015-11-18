clear variables

basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\max_TP_2\';

mw1foldername = 'max_tp_logs_mw1';
mw2foldername = 'max_tp_logs_mw2';

dbconns = 20:10:60;
clients = 2.^(0:7);

% size of the interval which is assumed to be representative
interval_size = 30;

% here store the throughput and index numbers for later plotting
% i-th lines are the throughput numbers
% (i+1)-lines are the corresponding indices of the current number of
% of db connections. This means x = #dbconns, and a new line for each
% new number of clients settings
tp_data = zeros(interval_size * length(dbconns), length(clients) * 2);

for i = 1:length(clients)
    currClients = clients(i);
    for j = 1:length(dbconns)
        currDbConns = dbconns(j);
        dbstr = num2str(currDbConns);
        clientstr = num2str(currClients);
        foldername = strcat(dbstr, strcat('_', clientstr));
        
        % read normal throughput by adding both middlware tp's
        mw1tpfile = strcat(foldername, ...
            strcat('\', strcat(mw1foldername, strcat('\', 'throughput.log'))));
        mw1tp = extract_important_interval(dlmread(strcat(basedir, mw1tpfile)));
        mw2tpfile = strcat(foldername, ...
            strcat('\', strcat(mw2foldername, strcat('\', 'throughput.log'))));
        mw2tp = extract_important_interval(dlmread(strcat(basedir, mw2tpfile)));
        total_tp = mw1tp + mw2tp;
        
        % calc indices for res matrix
        row_low = (j-1)*interval_size + 1;
        row_hi = j*interval_size;
        col_data = (i-1)*2 + 1;
        col_idx = i*2;
        
        % add the data to the overall result matrix
        tp_data(row_low:row_hi, col_data) = total_tp;
        tp_data(row_low:row_hi, col_idx) = currDbConns;
    end
end

% for every column pair make an individual plot which represents a
% different number of clients active
hold on
for i = 1:length(clients)
    boxplot(tp_data(:, (i-1)*2 + 1), tp_data(:, i*2));
end

% make sure all medians are visible
set(gca, 'YLim', [0, 20000])

% plot the 50% quantile for the current clients curve
 medians = findobj(gca,'tag','Median');
for i = 1:length(clients)
    % take the 5 latest drawns
    idx_lo = (i-1)*5 + 1;
    idx_hi = i*5;
    currMedians = medians(idx_lo:idx_hi);
    numMedians = length(currMedians);
    xs = zeros(numMedians, 1);
    ys = zeros(numMedians, 1);
    for j = 1:numMedians
        currMedian = currMedians(j);
        xt = currMedian.XData;
        xs(j) = mean(xt);
        yt = currMedian.YData;
        ys(j) = mean(yt);
    end
    plot(xs, ys, 'color', 'blue', 'linewidth', 2)
end