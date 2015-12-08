function rho_n = calc_req_prob(rho, rho0, m, n_end)
rho_n = zeros(length(rho), n_end);
for i = 1:n_end
    if i < m
        rho_n(:, i) = rho0.*((m.*rho).^i./factorial(i));
    else
        rho_n(:, i) = rho0.*((rho.^i.*m^m)./factorial(m));
    end
end
end