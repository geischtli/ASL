function [data_avg, data_data, data_idx] = find_valid_averages(data)
% data is length 140 - > split in subintervals of length 20
% ignore first 5, avg over other 15
num_els = 10;
data_data = zeros(7*num_els, 1);
data_idx = zeros(7*num_els, 1);
data_avg = zeros(7, 1);
for i = 0:6
    currInt = data((i*20)+1:(i+1)*20);
    data_data(((i*num_els)+1):((i+1)*num_els)) = ...
        currInt((num_els+1):end);
    data_avg(i+1) = mean(currInt((num_els+1):end));
    data_idx(((i*num_els)+1):((i+1)*num_els)) = i+1;
end
end