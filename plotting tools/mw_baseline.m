clear variables

folders = char('mw_baseline_fixed', 'mw_baseline_fixed_2ClientMachines');
for folder = 1:2
    j = 1;
    tpData = [];
    tpIdx = [];
    rttData = [];
    rttIdx = [];
    q75 = [];
    q90 = [];
    q95 = [];
    q99 = [];
    basedir = strcat('C:\Users\Sandro\Documents\ASL_LOGS\', folders(folder, :));
    for i = 10:10:100
        filename = strcat(strcat(basedir, '\mw_baseline_'), int2str(i));
        rtt = dlmread(strcat(filename, 'C\rtt.log'));
        % dont read the first 100k inserts
        currRtt = rtt(100001:end-100000);
        % we have to smooth it here, because there is too much data oO
        len = length(currRtt);
        ptr = 1;
        incr = 1000;
        ctr = 1;
        smoothRttData = [];
        while ptr <= len
            if ptr+incr <= len
                val = mean(currRtt(ptr:(ptr+incr)));
                smoothRttData(end+1, :) = val;
            else
                smoothRttData(end+1, :) = mean(currRtt(ptr:end));
            end
            ptr = ptr + incr;
            ctr = ctr + 1;
        end
        rttData((end+1):(end+length(smoothRttData)), :) = smoothRttData;
        rttIdx((end+1):(end+length(smoothRttData)), :) = i;
        q75 = [q75; quantile(smoothRttData, 0.75)];
        q90 = [q90; quantile(smoothRttData, 0.90)];
        q95 = [q95; quantile(smoothRttData, 0.95)];
        q99 = [q99; quantile(smoothRttData, 0.99)];
        
        tp = dlmread(strcat(filename, 'C\throughput.log'));
        fnnz = find(tp);
        currTp = tp((fnnz+10):(fnnz+40));
        tpData((end+1):(end+31), :) = currTp;
        tpIdx((end+1):(end+31), :) = i;
        j = j + 1;
    end
    % assign results to variables we later plot on
    % CM == ClientMachine
    if folder == 1
        rttData1CM = rttData;
        rttIdx1CM = rttIdx;
        q751CM = q75;
        q901CM = q90;
        q951CM = q95;
        q991CM = q99;
        tpData1CM = tpData;
        tpIdx1CM = tpIdx;
    else
        rttData2CM = rttData;
        rttIdx2CM = rttIdx;
        q752CM = q75;
        q902CM = q90;
        q952CM = q95;
        q992CM = q99;
        tpData2CM = tpData;
        tpIdx2CM = tpIdx;
    end
    sprintf('done with folder %s', folders(1, :))
end
sprintf('done with folder %s', folders(2, :))