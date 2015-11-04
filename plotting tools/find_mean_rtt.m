function mu = find_mean_rtt(data, offset)
effdata = data(offset:end);
mu = mean(effdata);
end