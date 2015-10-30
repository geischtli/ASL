clear variables;

basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\client_baseline_';
msgLen = '200\';
for k = 1:2
    basedir = strcat(basedir, msgLen);

    indices = [];
    reqPerSec = [];
    writePerSec = [];
    readPerSec = [];
    minSecondsIdx = 10;
    maxSecondsIdx = 26;
    if k == 1
        maxFolderIdx = 9;
    else
        maxFolderIdx = 8;
    end
    for i = 0:maxFolderIdx
        numClients = 2^i;
        currDir = strcat(basedir, strcat(num2str(numClients), 'C\'));
        currFile = strcat(currDir, 'clientTimes.log');
        mwTimes = dlmread(currFile);
        % delete all rows with 0's (mostly first few measurements)
        mwTimes(~all(mwTimes, 2), :) = [];
        numRows = size(mwTimes, 1);
        % go through all lines and sum up each column for every second
        secondsIndices = ones(numClients, 1);
        secondsData = zeros(ceil(numRows/numClients), 3);
        for j = 1:numRows
            currRow = mwTimes(j, :);
            currClient = currRow(1);
            % don't count warm-up and cool-down phase
            if (secondsIndices(currClient) >= minSecondsIdx && ...
                secondsIndices(currClient) <= maxSecondsIdx)
                    secondsData(secondsIndices(currClient), :) = ... 
                        secondsData(secondsIndices(currClient), :) + currRow(2:4);
            end
             secondsIndices(currClient) = secondsIndices(currClient) + 1;
        end
        % effective number of considered seconds
        numEffectiveRows = maxSecondsIdx - minSecondsIdx + 1;
        % take the average of all client-seperate sums
        secondsData = secondsData / numEffectiveRows;
        % fill the vectors
        % this data all belongs to the 2^i testset
        indices(end+1:(end+numEffectiveRows), :) = numClients;
        reqPerSec(end+1:(end+numEffectiveRows), :) = secondsData(minSecondsIdx:maxSecondsIdx, 1);
        writePerSec(end+1:(end+numEffectiveRows), :) = secondsData(minSecondsIdx:maxSecondsIdx, 2);
        readPerSec(end+1:(end+numEffectiveRows), :) = secondsData(minSecondsIdx:maxSecondsIdx, 3);
    end

    if strcmp(msgLen, '200\')
        indices200 = indices;
        reqPerSec200 = reqPerSec;
        writePerSec200 = writePerSec;
        readPerSec200 = readPerSec;
    else
        indices2000 = indices;
        reqPerSec2000 = reqPerSec;
        writePerSec2000 = writePerSec;
        readPerSec2000 = readPerSec;
    end
    basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\client_baseline_';
    msgLen = '2000\';
end

msgLen = '200\';
for k = 1:2
    basedir = strcat(basedir, msgLen);

    indices = [];
    reqPerSec = [];
    writePerSec = [];
    readPerSec = [];
    minSecondsIdx = 10;
    maxSecondsIdx = 26;
    if k == 1
        maxFolderIdx = 9;
    else
        maxFolderIdx = 8;
    end
    for i = 0:maxFolderIdx
        numClients = 2^i;
        currDir = strcat(basedir, strcat(num2str(numClients), 'C\'));
        currFile = strcat(currDir, 'middlewareTimes.log');
        mwTimes = dlmread(currFile);
        % delete all rows with 0's (mostly first few measurements)
        mwTimes(~all(mwTimes, 2), :) = [];
        numRows = size(mwTimes, 1);
        secondsData = mwTimes(minSecondsIdx:(numRows-minSecondsIdx), :);
        numEffectiveRows = size(secondsData, 1);
        % this data all belongs to the 2^i testset
        indices(end+1:(end+numEffectiveRows), :) = numClients;
        reqPerSec(end+1:(end+numEffectiveRows), :) = secondsData(:, 1);
        writePerSec(end+1:(end+numEffectiveRows), :) = secondsData(:, 2);
        readPerSec(end+1:(end+numEffectiveRows), :) = secondsData(:, 3);
    end

    if strcmp(msgLen, '200\')
        mwindices200 = indices;
        mwreqPerSec200 = reqPerSec;
        mwwritePerSec200 = writePerSec;
        mwreadPerSec200 = readPerSec;
    else
        mwindices2000 = indices;
        mwreqPerSec2000 = reqPerSec;
        mwwritePerSec2000 = writePerSec;
        mwreadPerSec2000 = readPerSec;
    end
    basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\client_baseline_';
    msgLen = '2000\';
end
