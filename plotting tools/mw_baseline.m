clear variables

j = 1;
tpData = [];
tpIdx = [];
rttData = [];
rttIdx = [];
q75 = [];
q90 = [];
q95 = [];
q99 = [];
for i = 10:10:70
    filename = strcat('C:\Users\Sandro\Documents\ASL_LOGS\mw_baseline_fixed\mw_baseline_', int2str(i));
    rtt = dlmread(strcat(filename, 'C\rtt.log'));
    % dont read the first 100k inserts
    currRtt = rtt(100001:end);
    % we have to smooth it here, because there is too much data oO
    len = length(currRtt);
    ptr = 1;
    incr = 1000;
    ctr = 1;
    smoothRttData = [];
    while ptr <= len
        if ptr+incr <= len
            val = mean(currRtt(ptr:(ptr+incr)));
            smoothRttData(end+1) = val;
        else
            smoothRttData(end+1) = mean(currRtt(ptr:end));
        end
        ptr = ptr + incr;
        ctr = ctr + 1;
    end
    rttData((end+1):(end+length(smoothRttData))) = smoothRttData;
    rttIdx((end+1):(end+length(smoothRttData))) = i;
    q75 = [q75; quantile(smoothRttData, 0.75)];
    q90 = [q90; quantile(smoothRttData, 0.90)];
    q95 = [q95; quantile(smoothRttData, 0.95)];
    q99 = [q99; quantile(smoothRttData, 0.99)];
    
    tp = dlmread(strcat(filename, 'C\throughput.log'));
    fnnz = find(tp);
    currTp = tp((fnnz+10):(fnnz+40));
    tpData((end+1):(end+31)) = currTp;
    tpIdx((end+1):(end+31)) = i;
    j = j + 1;
end