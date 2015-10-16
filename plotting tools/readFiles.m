clear variables;

basedir = 'C:\Users\Sandro\Documents\eclipse\ASL\logs\';

files = dir(strcat(basedir, '*.log'));
numFiles = length(files);
% exclude the _0 files
numFiles = numFiles - 2;

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
[startTimeOfFirstClient, firstClientIndex] = min(startTimes);

% subtract starting time from all numbers
for i = 1:numFiles
    currData = cell2mat(data(i));
    currData(1, 1) = currData(1, 1) - startTimeOfFirstClient;
    currData(:, 4) = currData(:, 4) - startTimeOfFirstClient;
    % add the starting time to the numbers
    currData(:, 4) = currData(:, 4) + currData(1, 1);
    data(i) = {currData};
end

% plot starting times in ms
effectiveStartingTimes = zeros(numFiles-1, 1);
for i = 1:numFiles
    currData = cell2mat(data(i));
    effectiveStartingTimes(i) = currData(1, 1)/1000000;
end
plot((1:numFiles), effectiveStartingTimes, 'o')