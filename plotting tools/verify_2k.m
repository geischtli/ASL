clear variables
% Apply interactive response time law to verify the 2k results measured
% in milestone 1. Use RT = #Clients/TP
TP = [12427 14707 11859 16133 12413 18233 13238 16428 13488 15954 ...
    12289 15855 13017 17701 13054 17619]';

RT = [5.12 3.87 4.84 3.66 9.98 7.07 9.14 6.97 4.69 3.87 4.94 4.19 9.16 ...
    6.57 9.31 6.73]';

clients60 = [60 60 60 60];
clients120 = [120 120 120 120];
clients = [clients60 clients120 clients60 clients120];

il_rt = clients'./TP;

% we work with milliseconds, so scale accordingly
il_rt = il_rt*10^3;

% round to 2 digits, to have same precision as input RT
il_rt = round(il_rt*100)/100;

% calc absolute difference in milliseconds
diff_rt = abs(il_rt - RT);

% print percentage difference with respect original measurements
perc_diff = (diff_rt./RT).*100;

disp('Percentual differences:')
perc_diff