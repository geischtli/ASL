function data_int = find_valid_interval(data)
nnzs = find(data);
fnnzi = nnzs(1);
% experiment ran for 7*20=140 seconds
data_int = data(fnnzi:(fnnzi+139));
end