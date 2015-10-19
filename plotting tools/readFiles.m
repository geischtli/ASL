clear variables;

basedir = '..\logs\';

files = dir(strcat(basedir, '*.log'));

% get number of clients
numClients = sum(cell2mat(cellfun(@(filename) strcmp( ...
    filename(1:6), 'client'), extractfield(files, 'name'), ...
    'UniformOutput', false))) - 1;
%get number of middlewares
numMiddlewares = sum(cell2mat(cellfun(@(filename) strcmp( ...
    filename(1:10), 'middleware'), extractfield(files, 'name'), ...
    'UniformOutput', false))) - 1;

numFiles = numClients + numMiddlewares;

% read all data files
data = cell(numFiles, 1);
startTimes = zeros(numFiles, 1);
j = 1;
for i = 1:numFiles + 2
    if (files(i).bytes == 0)
        continue;
    end
    currFile = files(i).name;
    data(j) = {dlmread(strcat(basedir, currFile))};
    currData = cell2mat(data(j));
    startTimes(j) = currData(1, 1);
    j = j + 1;
end

%% align the clients by first searching the minimum (e.g. the first
% client which entered the system. Then subtract the minimum from 
% all other numbers. Know we still have a start time for each client
% which is in the end added up to all numbers to then have the final
% result where all clients are aligned.

% find first client
[startTimeOfFirstInstance, firstInstanceIndex] = min(startTimes);

% subtract starting time from all numbers
for i = 1:numFiles
    currData = cell2mat(data(i));
    currData(1, 1) = currData(1, 1) - startTimeOfFirstInstance;
    data(i) = {currData};
end

% plot starting times in ms
%effectiveStartingTimes = zeros(numFiles-1, 1);
%for i = 1:numFiles
%    currData = cell2mat(data(i));
%    effectiveStartingTimes(i) = currData(1, 1)/1000000;
%end
%plot((1:numFiles), effectiveStartingTimes, 'o')

%% gather result for each request
% first find the number of request sent by inspecting all client files
% and checking if all performed the same amount of requests
currData = cell2mat(data(1));
numRequestsPerClient = max(currData(:, 2));
for i = 1:numFiles
    currData = cell2mat(data(i));
    newNumRequests = max(currData(:, 2));
    if (numRequestsPerClient ~= newNumRequests)
        printf('non matching client request num')
    end
    numRequestsPerClient = newNumRequests;
end

% preallocate request vector for each client
numTimeSteps = 17;
timings = zeros(numClients, numRequestsPerClient, numTimeSteps);

% find the timings for each request and client
for i = 1:numFiles
    currData = cell2mat(data(i));
    numLines = size(currData, 1);
    currStartTime = currData(1, 1);
    for j = 1:numLines
        currLine = currData(j, :);
        if (nnz(currLine(1:3)) ~= 3)
            continue;
        end
        clientId = currLine(1);
        requestId = currLine(2);
        timing = currLine(3);
        time = currLine(4);
        timings(clientId, requestId, timing) = time + currStartTime;
    end
end

% for each client find the response time for each request
responseTime = zeros(numClients, numRequestsPerClient);
for i = 1:numClients
    for j = 1:numRequestsPerClient
        responseTime(i, j) = timings(i, j, 4) - timings(i, j, 1);
    end
end

plot(mean(responseTime, 2))