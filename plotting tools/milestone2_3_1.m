% try to fit an M/M/m onto the middleware
clear variables
close all

tp_means = [13803 25007 33652 39093 42545 44534 44790 45520 45982 45392]';
rt_means = [0.7296 0.8013 0.8934 1.0286 1.1778 1.3429 1.5513 1.7495 1.9538 2.1982]';

tp2cm_means = [13803 25007 33652 39093 42545 44534 44790 45520 45982 45392]';
rt2cm_means =  [0.7156 0.7873 0.8444 0.9345 1.0662 1.2029 1.5665 1.7755 2.0148 2.2304]';

% scale to seconds
rt_means = rt_means .* 10^-3;
response_time = rt_means;

rt_means_double = rt2cm_means .* 10^-3;
double_response_time = rt_means_double;

arrival_rate = tp_means;
service_time = rt_means;

double_arrival_rate = tp2cm_means;
double_service_time = double_response_time;

service_rate = 1./service_time;
double_service_rate = 1./double_service_time;

% try all these M/M/m's for all arr/servtime combinations
m = 1:110;
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

diff_rho = abs(1-rho);
imagesc(log((1./diff_rho)+4))
colormap('hot')
ylabels = char('10', '20', '30', '40', '50', '60', '70', '80', '90', '100');
set(gca, 'YTickLabels', ylabels)
ylabel('Total number of clients online')
xlabel('m-Values for the M/M/m model')
title('Translated logarithmic inverse of traffic intensity - 1')
h =colorbar;
ylabel(h, 'Fit of the model (higher is better)')

%% apply mmm model
figure()
hold on
plot(arrival_rate, response_time, 'color', 'blue', 'linewidth', 2, ...
    'linestyle', ':')
plot(double_arrival_rate, double_response_time, 'color', 'red', 'linewidth', 2, ...
    'linestyle', ':')

mu = service_rate;
ms = 95;
for m = ms
    rho = arrival_rate./(m*service_rate);
    rho0 = calculate_rho0(rho, m);
    single_l = ((m.*rho).^m./(factorial(m)*(1-rho))).*rho0
    single_Er = 1./mu.*(1 + (single_l)./(m.*(1-rho)));
    plot(arrival_rate, single_Er, 'r',  'linewidth', 2, 'color', 'blue')
    err = norm(response_time - single_Er)
    
    rho = double_arrival_rate./(m*double_service_rate);
    rho0 = calculate_rho0(rho, m);
    double_l = ((m.*rho).^m./(factorial(m)*(1-rho))).*rho0
    double_Er = 1./(double_service_rate).*(1 + (double_l)./(m.*(1-rho)));
    plot(double_arrival_rate, double_Er, 'r',  'linewidth', 2, 'color', 'red')
    err = norm(response_time - double_Er)
end
legend('Measurements 1 MW', 'Measurements 2 MWs', 'Model for 1 MW', ...
    'Model for 2 MWs', 'location', 'northwest')
xlabel('Arrival Rate (Requests per second)')
ylabel('Response Time (sec)')
title('Fitting of the M/M/95 queueing model')
set(gca, 'YLim', [0 3*10^-3])