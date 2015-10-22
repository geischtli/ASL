package org.asl.experiments.baselines.database;

import java.util.ArrayList;
import java.util.List;

import org.asl.client.ClientInfo;
import org.asl.common.request.Request;
import org.asl.common.request.Request.RequestType;
import org.asl.common.request.builder.RequestBuilder;
import org.asl.common.timing.Timing;
import org.asl.middleware.MiddlewareInfo;

public class DatabaseBaselineClient implements Runnable {

	private ClientInfo ci;
	private final int numRequests;
	private final boolean performDummyRequests;
	private List<RequestType> requestList;
	private MiddlewareInfo mi;
	
	public DatabaseBaselineClient(int clientId, int numRequests, boolean performDummyRequests) {
		this.ci = ClientInfo.create();
		this.ci.setClientId(clientId);
		this.numRequests = numRequests;
		this.performDummyRequests = performDummyRequests;
		this.requestList = new ArrayList<RequestType>();
		this.mi = MiddlewareInfo.create();
		gatherRequests();
	}
	
	public static DatabaseBaselineClient create(int clientId, int numRequests, boolean performDummyRequests) {
		return new DatabaseBaselineClient(clientId, numRequests, performDummyRequests);
	}
	
	public void gatherRequests() {
		if (performDummyRequests) {
			// simulate minimal work
			RequestBuilder.addRequestTypes(
					requestList,
					new RequestType[] {
							RequestType.BASELINE_DUMMY
							},
					numRequests
					);
		} else {
			// simulate expected work
			RequestBuilder.addRequestTypes(
					requestList,
					new RequestType[] {
							RequestType.HANDSHAKE,
							RequestType.CREATE_QUEUE
							},
					1
					);
			RequestBuilder.addRequestTypes(
					requestList,
					new RequestType[] {
							RequestType.GET_REGISTERED_CLIENTS,
							RequestType.GET_REGISTERED_QUEUES,
							RequestType.SEND_MESSAGE
							},
					numRequests/3
					);
		}
	}
	
	@Override
	public void run() {
		System.out.println("Client " + ci.getClientId() + " started");
		ci.initTimeLogger();
		int requestListSize = requestList.size();
		for (int i = 1; i <= requestListSize; i++) {
			ci.getMyTimeLogger().click(Timing.EXPERIMENT_START_REQUEST, ci.getClientId(), ci.getRequestId(), ci.getStartTime());
			Request req = RequestBuilder.getRequest(requestList.get(ci.getRequestId()), ci);
			req.processOnMiddleware(mi);
			ci.getMyTimeLogger().click(Timing.EXPERIMENT_END_REQUEST, ci.getClientId(), ci.getRequestId(), ci.getStartTime());
			ci.incrementRequestId();
		}
		ci.getMyTimeLogger().stopMyTimeLogger();
		System.out.println("Client " + ci.getClientId() + " is done");
	}
	
}
