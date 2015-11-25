clear variables
% Model the system as M/M/1
% This model requires two parameters:
% a) The arrival rate, which is equal to the throughput
% b) The service rate, which is equal to 1/(response time)
% Since we take the measurements from the stability experiment
% we know both parameters.

% Let's find the overall means to fix both parameter values
% Find arrival rate
% First read the throughput of the system
mw1tp = dlmread('C:\Users\Sandro\Documents\ASL_LOGS\stability\stability_mw1\throughput.log');
mw2tp = dlmread('C:\Users\Sandro\Documents\ASL_LOGS\stability\stability_mw2\throughput.log');
total_tp = mw1tp + mw2tp;
% ignore the first 10 and the last 20 seconds due to instability coming
% from startup or early finishing of clients.
total_tp = total_tp(13:360);
arrival_rate = mean(total_tp);

% Now find the service rate via the average response time
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
% extract response time
c1_rt = client1rtt(3:340);
c2_rt = client2rtt(3:340);

mean_rt = mean([c1_rt; c2_rt]);
service_rate = 1/(mean_rt*10^-3);

% calculate other insightful variables
traffic_intensity = arrival_rate/service_rate;
mean_number_of_jobs_in_system = ...
    traffic_intensity/(1 - traffic_intensity);
variance_number_of_jobs_in_system = ...
    traffic_intensity/(1 - traffic_intensity)^2;
%%
% TODO: Run experiment and log the number
% of active middleware threads currently running
% hopefully this will give us around 120 per middleware
% which would explain why it's more towards m/m/120