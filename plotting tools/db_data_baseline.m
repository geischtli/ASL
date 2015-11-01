clear variables

basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\db_data_baseline\';
files = dir(basedir);
numFiles = length(files);

% concat all rows and concat the seconds and microseconds, such that
% we later can sort on this column
data = [];
for file = 6:numFiles-1
    currFile = files(file).name;
    currData = dlmread(strcat(basedir, currFile));
    [m, ~] = size(currData);
    mergedData = zeros(m, 5);
    mergedData(:, 1:4) = currData(:, 1:4);
    mergedData(:, 5) = currData(:, 5).*10^6;
    mergedData(:, 5) = mergedData(:, 5) + currData(:, 6);
    % append to other data
    data = [data; mergedData];
end

% now sort by the concatenated time column
data = sortrows(data, 5);

% get all inserts
inserts = data(data(:, 4) == 0, :);
% get all deletes
deletes = data(data(:, 4) == 1, :);

% plot them
hold on
plot(inserts(:, 3), 'b')
plot(deletes(:, 3), 'r')
