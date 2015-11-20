% only do the computations in here, such that when debugging the plot
% the computation does not have to be rerun. Also run this file before
% the verification of the experiment.

clear variables

%basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\max_TP_2\';
basedir = 'C:\Users\Sandro\Desktop\max_TP_2_stored201115\';

mw1foldername = 'max_tp_logs_mw1';
mw2foldername = 'max_tp_logs_mw2';
c1foldername = 'max_tp_logs_c1';
c2foldername = 'max_tp_logs_c2';

dbconns = 20:10:80;
clients = 2.^(0:7);

% size of the interval which is assumed to be representative
interval_size = 30;

% here store the throughput and index numbers for later plotting
% i-th lines are the throughput numbers
% (i+1)-lines are the corresponding indices of the current number of
% of db connections. This means x = #dbconns, and a new line for each
% new number of clients settings
tp_data = zeros(interval_size * length(dbconns), length(clients) * 2);

for i = 1:length(clients)
    currClients = clients(i);
    for j = 1:length(dbconns)
        currDbConns = dbconns(j);
        dbstr = num2str(currDbConns);
        clientstr = num2str(currClients);
        foldername = strcat(dbstr, strcat('_', clientstr));
        
        % read normal throughput by adding both middlware tp's
        mw1tpfile = strcat(foldername, ...
            strcat('\', strcat(mw1foldername, strcat('\', 'throughput.log'))));
        mw1tp = extract_important_interval(dlmread(strcat(basedir, mw1tpfile)));
        mw2tpfile = strcat(foldername, ...
            strcat('\', strcat(mw2foldername, strcat('\', 'throughput.log'))));
        mw2tp = extract_important_interval(dlmread(strcat(basedir, mw2tpfile)));
        total_tp = mw1tp + mw2tp;
        
        % calc indices for res matrix
        row_low = (j-1)*interval_size + 1;
        row_hi = j*interval_size;
        col_data = (i-1)*2 + 1;
        col_idx = i*2;
        
        % add the data to the overall result matrix
        tp_data(row_low:row_hi, col_data) = total_tp;
        tp_data(row_low:row_hi, col_idx) = currDbConns;
    end
end

% here store the final response times per request
rt_data = zeros(interval_size * length(dbconns), length(clients) * 2);

for i = 1:length(clients)
    currClients = clients(i);
    for j = 1:length(dbconns)
        currDbConns = dbconns(j);
        dbstr = num2str(currDbConns);
        clientstr = num2str(currClients);
        foldername = strcat(dbstr, strcat('_', clientstr));
        
        % store the response time per request here for all clients of
        % the current configuration
        curr_rt_per_req = zeros(interval_size, 1);
        
        % read the individual response time and throughput of each client
        % and calculate the response time per request
        
        % get file names for client machine 1
        filenames = dir(strcat(basedir, strcat(foldername, ...
            strcat('\', c1foldername))));
        for k = 1:currClients
            rtfilename = strcat('\', filenames(k*2 + 1).name);
            tpfilename = strcat('\', filenames(k*2 + 2).name);
            c1rtfile = strcat(foldername, strcat('\', strcat(c1foldername, rtfilename)));
            c1tpfile = strcat(foldername, strcat('\', strcat(c1foldername, tpfilename)));
            c1rt = extract_important_interval(dlmread(strcat(basedir, c1rtfile)));
            c1tp = extract_important_interval(dlmread(strcat(basedir, c1tpfile)));
            curr_rt_per_req = curr_rt_per_req + (c1rt ./ c1tp);
        end
        
        % do the same for client 2
        filenames = dir(strcat(basedir, strcat(foldername, ...
            strcat('\', c2foldername))));
        for k = 1:currClients
            rtfilename = strcat('\', filenames(k*2 + 1).name);
            tpfilename = strcat('\', filenames(k*2 + 2).name);
            c2rtfile = strcat(foldername, strcat('\', strcat(c2foldername, rtfilename)));
            c2tpfile = strcat(foldername, strcat('\', strcat(c2foldername, tpfilename)));
            c2rt = extract_important_interval(dlmread(strcat(basedir, c2rtfile)));
            c2tp = extract_important_interval(dlmread(strcat(basedir, c2tpfile)));
            curr_rt_per_req = curr_rt_per_req + (c2rt ./ c2tp);
        end
        
        % average over all active clients * 2, because we ran 2 client
        % machines, each running currClients clients
        curr_rt_per_req = curr_rt_per_req / (2 * currClients);
        
        % calc indices for res matrix
        row_low = (j-1)*interval_size + 1;
        row_hi = j*interval_size;
        col_data = (i-1)*2 + 1;
        col_idx = i*2;
        
        % add the data to the overall result matrix
        rt_data(row_low:row_hi, col_data) = curr_rt_per_req;
        rt_data(row_low:row_hi, col_idx) = currDbConns;
    end
end