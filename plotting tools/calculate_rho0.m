function rho0 = calculate_rho0(rho, m)
% rho is a vector
% rho0 should also be a vector, and treated row-wise
len = length(rho);
rho0 = zeros(len, 1);
for i = 1:len
    f1 = (m.*rho(i))^m/(factorial(m)*(1-rho(i)));
    f2 = sum((m.*rho(i)).^(1:m-1)/factorial(1:(m-1)));
    rho0(i) = 1 + f1 + f2;
end
rho0 = 1./rho0;
end