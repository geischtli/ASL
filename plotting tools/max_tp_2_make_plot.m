% for every column pair make an individual plot which represents a
% different number of clients active
figure()
hold on
for i = 1:length(clients)
    boxplot(tp_data(:, (i-1)*2 + 1), tp_data(:, i*2));
end

% make sure all medians are visible
set(gca, 'YLim', [0, 21000])

% colors for each client line
colors = [0 0 0; 1 0 0; 0 1 0; 0 0 1; 1 0.5 0; ...
    0 1 1; 1 0 1; 1 1 0];

% plot the 50% quantile for the current clients curve
 medians = findobj(gca,'tag','Median');
for i = 1:length(clients)
    % take the 5 latest drawns
    idx_lo = (i-1)*length(dbconns) + 1;
    idx_hi = i*length(dbconns);
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
    plot(xs, ys, 'color', colors(i, :), 'linewidth', 2)
end

xlabel('Number of Database Connections')
ylabel('Requests per Second (ms)')
legend('256 Client', '128 Clients', '64 Clients', '32 Clients', '16 Clients', ...
    '8 Clients', '4 Clients', '2 Clients', ...
    'Location', 'northwest')

% Do the response time analysis with the client data
hold off
figure()
hold on

% for every column pair make an individual plot which represents a
% different number of clients active
hold on
for i = 1:length(clients)
    boxplot(rt_data(:, (i-1)*2 + 1), rt_data(:, i*2));
end

% make sure all lines are visible
set(gca, 'YLim', [0 40])

% colors for each client line
colors = [0 0 0; 1 0 0; 0 1 0; 0 0 1; 1 0.5 0; ...
    0 1 1; 1 0 1; 1 1 0];

% plot the 50% quantile for the current clients curve
 medians = findobj(gca,'tag','Median');
for i = 1:length(clients)
    % take the 5 latest drawns
    idx_lo = (i-1)*length(dbconns) + 1;
    idx_hi = i*length(dbconns);
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
    plot(xs, ys, 'color', colors(i, :), 'linewidth', 2)
end

xlabel('Number of Database Connections')
ylabel('Response time per Request (ms)')
legend('256 Client', '128 Clients', '64 Clients', '32 Clients', '16 Clients', ...
    '8 Clients', '4 Clients', '2 Clients', ...
    'Location', 'northeast')