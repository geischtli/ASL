clear variables
close all

single_mw = [2.7311, 2.9255, 2.9462, 3.0041, 3.3001, 3.1539, ...
    3.0312, 3.0652, 3.2452, 2.9788, 3.0649, 3.1587];

single_db_plus_network_means = [2.5947, 2.7781, 2.7920, 2.8511, 3.2702, ...
    3.1181, 2.9898, 3.0272, 3.1950, 2.9377, 3.0209, 3.1115];

%% INTERPOLATION OF MISSING VALUES

% get real service time of mw
single_mw = single_mw - single_db_plus_network_means;

% original positions
x = 10:10:120;

% first extrapolate the values for 1:9
firsts_mw = interp1(x, single_mw, 1:9, 'pchip');
firsts_db = interp1(x, single_db_plus_network_means, 1:9, 'pchip');

% now linearly interpolate the other values
interp_mw = zeros(120, 1);
interp_db = zeros(120, 1);

interp_mw(1:9) = firsts_mw;
interp_db(1:9) = firsts_db;
for i = 1:11
    vq = interp1([0 10], [single_mw(i), single_mw(i+1)], 1:9);
    interp_mw(i*10) = single_mw(i);
    interp_mw(i*10 + 1:(i+1)*10 -1) = vq;
    
    vq = interp1([0 10], [single_db_plus_network_means(i), ...
        single_db_plus_network_means(i+1)], 1:9);
    interp_db(i*10) = single_db_plus_network_means(i);
    interp_db(i*10 + 1:(i+1)*10 -1) = vq;
end
interp_mw(end) = single_mw(end);
interp_db(end) = single_db_plus_network_means(end);

%% prepare the data for the mva algorithm performed in java

% scale to seconds
interp_mw = interp_mw * 10^-3;
interp_db = interp_db * 10^-3;

% work with req/sec
mu_mw = 1./interp_mw;
mu_db = 1./interp_db;

% because we work with req/sec, scale it such that the parallelism is
% introduced into the model
scaler = [1:40, 40.*ones(1, 80)]';
mu_mw = mu_mw.*scaler;
mu_db = mu_db.*scaler;