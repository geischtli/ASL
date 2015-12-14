%% mean value analysis for 1 MW
clear variables
close all

% system arrival rate
X = [2481.5 4773.5 7015.5 9311.5 10346.5 12302 13152 13005.5 12321 13383 13013.5 12642.5]';

% device service times
S_db_plus_network = [2.59470428849696 2.77807451457582  2.7920286234508 ...
    2.85112480679021 3.27020155519635 3.11814186974835 2.98979616021612 ...
    3.02723271011188 3.19499897068213 2.93770492954681 3.02089042230959 ...
    3.11145817103992]';
S_mw =  [0.136440093153641 0.147458767805334 0.154162826632399 ...
    0.152933225136665 0.029867159994474 0.0358013076991996 ...
    0.0414257624175653 0.0379270762083106 0.0502354988084108 ...
    0.0410739758470062 0.0439868421004741 0.0472214474167165]';

% Scale the service times from ms to sec
S_db_plus_network = S_db_plus_network * 10^-3;
S_mw = S_mw * 10^-3;

% Number of visits per device and request
V_db_plus_network = X;
V_mw = X;

% Number of devices
M = 2;
N = 120;
Z = 0;
%% Do actual calculations
clients = 10:10:120;
num_clients = length(clients);
X = zeros(num_clients, 1);
idx = 1;
for curr_clients = clients;
    [X(idx), ~] = mva(curr_clients, Z, M, ...
        [S_db_plus_network(idx); S_mw(idx)], ...
        [1; 1]);
    idx = idx + 1;
end
