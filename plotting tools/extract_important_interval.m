function ret = extract_important_interval(in)
% ignore the leading zeros which are generated
% when the middleware runs yet alone, since the clients
% are still beeing launched.
nnz_indices = find(in);
in = in(nnz_indices(1):end);
% now ignore the first 20 seconds and take the following
% 30 seconds which are assumed to be representative
ret = in(20:49);
end