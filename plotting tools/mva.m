function [X, R, U] = mva(N, Z, M, S, V)
% S and V vectors with values for each device
Q = zeros(N, M);
R = zeros(N, M+1);
X = zeros(N, 1);
Qi = zeros(N, 1);
for n = 1:N
    for i = 1:M
        R(n, i) = S(i)*(1 + ((N-1)/N)*Qi(i));
        R(n, end) = R(n, end) + R(n, i)*V(i);
    end
    X(n) = n/(Z + R(n, end));
    for i = 1:M
        Q(n, i) = X(n)*V(i)*R(n, i);
        Qi(i) = Q(n, i);
    end
end
%U = X.*S.*V;
U = zeros(N, M);
for i = 1:M
    U(:, i) = X.*S(i).*V(i);
end