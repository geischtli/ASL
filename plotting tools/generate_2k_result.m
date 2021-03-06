clear variables

% A = #middlewares: -1 for 1, 1 for 2
% B = #clients: -1 for 60, 1 for 120
% C = msg size: -1 for 200, 1 for 2000
% D = #dbconns: -1 for 20, 1 for 40

mat = [1 -1 -1 -1 -1 1 1 1 1 1 1 -1 -1 -1 -1 1; ...
    1 -1 -1 -1 1 1 1 -1 1 -1 -1 -1 1 1 1 -1; ...
    1 -1 -1 1 -1 1 -1 1 -1 1 -1 1 -1 1 1 -1; ...
    1 -1 -1 1 1 1 -1 -1 -1 -1 1 1 1 -1 -1 1; ...
    1 -1 1 -1 -1 -1 1 1 -1 -1 1 1 1 -1 1 -1; ...
    1 -1 1 -1 1 -1 1 -1 -1 1 -1 1 -1 1 -1 1; ...
    1 -1 1 1 -1 -1 -1 1 1 -1 -1 -1 1 1 -1 1; ...
    1 -1 1 1 1 -1 -1 -1 1 1 1 -1 -1 -1 1 -1; ...
    1 1 -1 -1 -1 -1 -1 -1 1 1 1 1 1 1 -1 -1; ...
    1 1 -1 -1 1 -1 -1 1 1 -1 -1 1 -1 -1 1 1; ...
    1 1 -1 1 -1 -1 1 -1 -1 1 -1 -1 1 -1 1 1; ...
    1 1 -1 1 1 -1 1 1 -1 -1 1 -1 -1 1 -1 -1; ...
    1 1 1 -1 -1 1 -1 -1 -1 -1 1 -1 -1 1 1 1; ...
    1 1 1 -1 1 1 -1 1 -1 1 -1 -1 1 -1 -1 -1; ...
    1 1 1 1 -1 1 1 -1 1 -1 -1 1 -1 -1 -1 -1; ...
    1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1];

TP = [12427 14707 11859 16133 12413 18233 13238 16428 13488 15954 ...
    12289 15855 13017 17701 13054 17619]';

RT = [5.12 3.87 4.84 3.66 9.98 7.07 9.14 6.97 4.69 3.87 4.94 4.19 9.16 ...
    6.57 9.31 6.73]';

qsTP = mat'*TP/16;
qsTP = qsTP(2:16);
SST_TP = 16*sum(qsTP.^2);
effectFactorsTP = (qsTP.^2)*16;
tp_effects = effectFactorsTP./SST_TP*100

qsRT = mat'*RT/16;
qsRT = qsRT(2:16);
SST_RT = 16*sum(qsRT.^2);
effectFactorsRT = (qsRT.^2)*16;
rt_effects = effectFactorsRT./SST_RT*100