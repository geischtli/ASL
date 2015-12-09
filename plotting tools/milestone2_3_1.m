% try to fit an M/M/m onto the middleware
clear variables
close all

tp_means = [13803 25007 33652 39093 42545 44534 44790 45520 45982 45392]';
rt_means = [0.7296 0.8013 0.8934 1.0286 1.1778 1.3429 1.5513 1.7495 1.9538 2.1982]';

% scale to seconds
rt_means = rt_means .* 10^-3;

arrival_rate = tp_means;
service_time = rt_means;

service_rate = 1./service_time;

% try all these M/M/m's for all arr/servtime combinations
m = 1:240;
mlen = length(m);

% store all traffic intensities here
rho = zeros(length(tp_means), mlen);

for i = 1:length(tp_means)
    for mi = 1:mlen
        rho(i, mi) = arrival_rate(i)/(service_rate(i)*m(mi));
    end
end

% find the first occurence in each row which is < 1
for i = 1:length(tp_means)
    for mi = 1:mlen
        if rho(i, mi) < 1
            [i*10 mi]
            break
        end
    end
end