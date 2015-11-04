function mu = find_mean(data, numSecondsToInclude)
% find first zero
fres = find(data);
fnnz = fres(1);
tpdata = data((fnnz+10):(fnnz+10+numSecondsToInclude));
mu = mean(tpdata);
end