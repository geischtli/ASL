% apply the interactive law to see if the computed response time
% matches the measured one

% Because the law is fitting very well, we add also some numbers
% to prove that the law is not missing but indeed fitting nice.
numClients = length(clients);
numDBConns = length(dbconns);
diff = zeros(numClients, numDBConns);

figure()
hold on

% go through the data tables and compute the interactive law. The i-th
% column caries data, the (i+1)-th column caries the boxplot indices
n = 2*length(clients);
il_rt_data = tp_data;
for i = 1:2:n
    % apply interactive law here
    il_rt_data(:, i) = (2*clients(ceil(i/2)))./il_rt_data(:, i);
    % we work with milliseconds
    il_rt_data(:, i) = il_rt_data(:, i).*10^3;
end

% for every column pair make an individual plot which represents a
% different number of clients active
for i = 1:length(clients)
    boxplot(il_rt_data(:, (i-1)*2 + 1), il_rt_data(:, i*2));
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
        % store mean for later difference evaluation of measurements
        % vs interactive law
        diff(numClients - (i-1), numDBConns - (j-1)) = ys(j);
    end
    plot(xs, ys, 'color', colors(i, :), 'linewidth', 2)
end

% now plot the original response times
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
        % calculate the difference between measurement and interactive law
        diff(numClients - (i-1), numDBConns - (j-1)) = ... 
            diff(numClients - (i-1), numDBConns - (j-1)) - ys(j);
    end
    plot(xs, ys, 'color', colors(i, :), 'linewidth', 2, ...
        'linestyle', ':')
end

title('Interactive Law Verification')
xlabel('Number of Database Connections')
ylabel('Response time per Request')
legend('256 Client', '128 Clients', '64 Clients', '32 Clients', '16 Clients', ...
    '8 Clients', '4 Clients', '2 Clients', ...
    'Location', 'northeast')

% calculate effective absolute differences between measurements and law
diff = abs(diff);
diff = sum(diff, 2)/numDBConns;
disp('Avg ms abs diff btw measured response time and IL (2,4,...256)')
diff