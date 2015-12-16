clear variables
close all

% at 30 clients
mw_service_time = 0.000844386;
db_service_time = 0.001327447;

N = 120;
mw_factors = [1:95, 95*ones(1, N-95)]';
db_factors = [1:30, 30*ones(1, N-30)]';

% calculate mu's
mw_mu = mw_factors./mw_service_time;
db_mu = db_factors./db_service_time;

% accumulation factor
mw_mu = mw_mu.*2;