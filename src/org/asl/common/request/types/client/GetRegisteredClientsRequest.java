package org.asl.common.request.types.client;

import java.util.List;
import java.util.Random;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.GetRegisteredClientsException;
import org.asl.common.timing.ASLTimer;
import org.asl.middleware.database.dao.impl.ClientDAO;

public class GetRegisteredClientsRequest extends Request {

	private static final long serialVersionUID = 105L;
	private List<Integer> clients;
	
	public GetRegisteredClientsRequest() {
		this.clients = null;
		this.exception = new GetRegisteredClientsException();
	}
	
	public List<Integer> getClients() {
		return clients;
	}
	
	public void setClients(List<Integer> clients) {
		this.clients = clients;
	}
	
	@Override
	public void processOnMiddleware(ASLTimer timer, int reqCount) {
		try {
			setClients(ClientDAO.getClientDAO().getRegisteredClients(timer, reqCount));
		} catch (GetRegisteredClientsException e) {
			setException(e);
		}
	}
	
	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			//System.out.println("Successfully found " + clients.size() + " clients");
			if (clients.size() > 0) {
				/*System.out.print("Clients are: ");
				for (int i = 0; i < clients.size(); i++) {
					System.out.print(clients.get(i) + " ");
				}*/
				// set a random client as receiver of next message
				int nextReceiver = new Random().nextInt(clients.size());
				//System.out.println("Next receiver: " + (nextReceiver + 1));
				ci.setSendReceiverId(clients.get(nextReceiver));
			}
		} else {
			throw getException();
		}
	}
	
}
