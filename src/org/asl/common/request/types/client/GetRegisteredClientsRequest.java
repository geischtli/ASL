//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.request.types.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.types.exceptions.ASLException;
import org.asl.common.request.types.exceptions.GetRegisteredClientsException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.dao.impl.ClientDAO;

public class GetRegisteredClientsRequest extends Request {

	private static final long serialVersionUID = 105L;
	private List<Integer> clients;
	
	public GetRegisteredClientsRequest(int clientId, int requestId) {
		super(clientId, requestId);
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
	public void processOnMiddleware(MiddlewareInfo mi) {
		try {
			setClients(ClientDAO.getClientDAO().getRegisteredClients(clientId, requestId, mi));
		} catch (GetRegisteredClientsException e) {
			setException(e);
		}
	}
	
	@Override
	public void processOnClient(ClientInfo ci) throws ASLException {
		if (!getException().carriesError()) {
			if (clients.size() > 0) {
				ci.setClientsOnline(clients);
				// set a random client as receiver of next message
				int nextReceiver = new Random().nextInt(clients.size());
				//System.out.println("Next receiver: " + (nextReceiver + 1));
				ci.setSendReceiverId(clients.get(nextReceiver));
			} else if (clients.size() == 0) {
				ci.setClientsOnline(new ArrayList<Integer>());
			}
		} else {
			throw getException();
		}
	}
	
}
