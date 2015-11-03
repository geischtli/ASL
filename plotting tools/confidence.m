function [ci_diff, percentage] = confidence(x, t)
n = length(x);
xbar = mean(x);
s = sqrt(sum((x - xbar).^2)/(n-1));
ci_plus = xbar + t*(s/sqrt(n));
ci_minus = xbar - t*(s/sqrt(n));
percentage = (ci_plus - ci_minus)/xbar*100;
ci_diff = ci_plus - ci_minus;