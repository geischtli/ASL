clear variables
close all

% st = service time
db_st = [2.59470428849696, 2.77807451457582, 2.7920286234508, ...
    2.85112480679021, 3.27020155519635, 3.1181418697483, ...
    2.98979616021612, 3.02723271011188, 3.19499897068213, ...
    2.93770492954681, 3.02089042230959, 3.11145817103992].*10^-3';

% subtract network latency
db_st = db_st - 0.5*10^-3;

mw_st = [0.136440093153641, 0.147458767805334, 0.154162826632399, ...
    0.152933225136665, 0.29867159994474, 0.358013076991996, ...
    0.414257624175653, 0.379270762083106, 0.502354988084108, ...
    0.410739758470062, 0.439868421004741, 0.472214474167165].*10^-3';
          

%% INTERPOLATION OF MISSING VALUES

% original positions
x = 10:10:120;

% first extrapolate the values for 1:9
firsts_mw = interp1(x, mw_st, 1:9, 'pchip');
firsts_db = interp1(x, db_st, 1:9, 'pchip');

% now linearly interpolate the other values
interp_mw = zeros(120, 1);
interp_db = zeros(120, 1);

interp_mw(1:9) = firsts_mw;
interp_db(1:9) = firsts_db;
for i = 1:11
    vq = interp1([0 10], [mw_st(i), mw_st(i+1)], 1:9);
    interp_mw(i*10) = mw_st(i);
    interp_mw(i*10 + 1:(i+1)*10 -1) = vq;
    
    vq = interp1([0 10], [db_st(i), ...
        db_st(i+1)], 1:9);
    interp_db(i*10) = db_st(i);
    interp_db(i*10 + 1:(i+1)*10 -1) = vq;
end
interp_mw(end) = mw_st(end);
interp_db(end) = db_st(end);

%mw_service_time = 0.000844386;
%db_service_time = 0.001327447;

N = 120;
mw_factors = [95*ones(1, N)]';
db_factors = [30*ones(1, N)]';

% calculate mu's
mw_mu = mw_factors./interp_mw;
db_mu = db_factors./interp_db;