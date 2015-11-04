clear variables

basedir = 'C:\Users\Sandro\Documents\ASL_LOGS\2k\';
mws = [1 2];
clients = [60 120];
msgsizes = [200 2000];
dbconns = [20 40];

res = zeros(16, 6);

rowptr = 1;
for currmw = 1:2
    for currClients = 1:2
        for currmsgsize = 1:2
            for currdbconns = 1:2
                res(rowptr, 1) = mws(currmw);
                res(rowptr, 2) = clients(currClients);
                res(rowptr, 3) = msgsizes(currmsgsize);
                res(rowptr, 4) = dbconns(currdbconns);
                % open corresponding folder
                folder = strcat(basedir, ...
                    strcat(strcat(num2str(mws(currmw)), '_')), ...
                        strcat(strcat(num2str(clients(currClients)), '_')), ...
                            strcat(strcat(num2str(msgsizes(currmsgsize)), '_')), ...
                                num2str(dbconns(currdbconns))...
                            );
               files = dir(folder);
               folder = strcat(folder, '\');
               % first find throughput through middleware
               if currmw == 1
                   mwtp = dlmread(strcat(folder, files(5).name));
                   res(rowptr, 5) = find_mean(mwtp, 30);
               else
                   mw1tp = dlmread(strcat(folder, files(5).name));
                   mw2tp = dlmread(strcat(folder, files(6).name));
                   mu1 = find_mean(mw1tp, 30);
                   mu2 = find_mean(mw2tp, 30);
                   res(rowptr, 5) = mu1 + mu2;
               end
               % second find the responste time averaged per request
               % over all clients, but computed client-by-client
               % merge both client files
               folderNames = char('2k_client1', '2k_client2');
               musum = 0;
               for i = 1:2
                   currFolder = strcat(folder, folderNames(i, :));
                   clientFiles = dir(currFolder);
                   currFolder = strcat(currFolder, '\');
                   numFiles = length(clientFiles);
                   for j = 3:2:numFiles
                       % first rtt, then tp file of same client
                       currRtt = dlmread(strcat(currFolder, clientFiles(j).name));
                       currTp = dlmread(strcat(currFolder, clientFiles(j+1).name));
                       clientData = currRtt./currTp;
                       musum = musum + find_mean_rtt(clientData, 10);
                   end
               end
               res(rowptr, 6) = musum/clients(currClients);
               rowptr = rowptr + 1;
            end
        end
    end
end
