package org.asl.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.asl.common.propertyparser.PropertyKey;
import org.asl.common.propertyparser.PropertyParser;
import org.asl.common.timing.TimeLogger;

public class ClientInfo {
	
	private int clientId;
	
	private int deleteQueueId;
	private int readQueueId;
	private int sendQueueId;
	private List<Integer> clientsOnline;
	private List<Integer> queuesOnline;
	private int readFromSenderId;
	private int sendReceiverId;
	private int numberOfMessages;
	private String sendContext;
	private AtomicInteger requestId; // counter for globally unique request id tuple
	private TimeLogger myTimeLogger;
	private long startTimeNS;
	private StringBuilder stringBuilder;
	private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private Random random;
	private PropertyParser propParser;
	private String content;
	// logging
	private int reqCount;
	BufferedWriter tpWriter;
	BufferedWriter rttWriter;
	public AtomicInteger reqPerSec;
	public AtomicLong rttPerSec;
	private Timer logTimer;
	
	public ClientInfo() {
		this.clientId = 0;
		this.deleteQueueId = 0;
		this.readFromSenderId = 0;
		this.sendQueueId = 0;
		this.clientsOnline = null;
		this.queuesOnline = null;
		this.readFromSenderId = 0;
		this.sendReceiverId = 0;
		this.numberOfMessages = -1;
		this.sendContext = "";
		this.requestId = new AtomicInteger(0);
		this.myTimeLogger = TimeLogger.create("CLIENT", this.clientId, System.nanoTime());
		this.startTimeNS = -1;
		this.stringBuilder = new StringBuilder();
		this.random = new Random();
		this.propParser = PropertyParser.create("config/config_common.xml").parse();
		this.content = "";
		this.reqCount = 0;
		this.reqPerSec = new AtomicInteger(0);
		this.rttPerSec = new AtomicLong(0);
		this.logTimer = null;
		initMyContent();
	}
	
	public void initMyContent() {
		int contentLength = Integer.parseInt(propParser.getProperty(PropertyKey.CONTENT_LENGTH));
		for (int i = 0; i < contentLength; i++) {
			stringBuilder.append(alphabet[random.nextInt(alphabet.length)]);
		}
		this.content = stringBuilder.toString();
	}
	
	public static ClientInfo create() {
		return new ClientInfo();
	}
	
	public int getClientId() {
		return clientId;
	}
	
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	public int getDeleteQueueId() {
		return deleteQueueId;
	}
	
	public void setDeleteQueueId(int deleteQueueId) {
		this.deleteQueueId = deleteQueueId;
	}
	
	public int getReadQueueId() {
		return readQueueId;
	}
	
	public void setReadQueueId(int readQueueId) {
		this.readQueueId = readQueueId;
	}
	
	public int getReadFromSenderId() {
		return readFromSenderId;
	}
	
	public void setReadFromSenderId(int readFromSenderId) {
		this.readFromSenderId = readFromSenderId;
	}
	
	public int getSendReceiverId() {
		return sendReceiverId;
	}
	
	public void setSendReceiverId(int sendReceiverId) {
		this.sendReceiverId = sendReceiverId;
	}
	
	public int getSendQueueId() {
		return sendQueueId;
	}
	
	public void setSendQueueId(int sendQueueId) {
		this.sendQueueId = sendQueueId;
	}
	
	public String getSendContentText() {
		//return "This is a message from Client " + this.clientId;
		return content;
	}

	public int getNumberOfMessages() {
		return numberOfMessages;
	}

	public void setNumberOfMessages(int numberOfMessages) {
		this.numberOfMessages = numberOfMessages;
	}

	public List<Integer> getClientsOnline() {
		return clientsOnline;
	}

	public void setClientsOnline(List<Integer> clientsOnline) {
		this.clientsOnline = clientsOnline;
	}

	public List<Integer> getQueuesOnline() {
		return queuesOnline;
	}

	public void setQueuesOnline(List<Integer> queuesOnline) {
		this.queuesOnline = queuesOnline;
	}
	
	public int getRequestId() {
		return requestId.get();
	}
	
	public void incrementRequestId() {
		requestId.incrementAndGet();
	}
	public void setRequestId(int val) {
		requestId.set(val);
	}

	public String getSendContext() {
		return sendContext;
	}

	public void setSendContext(String sendContext) {
		this.sendContext = sendContext;
	}
	
	public void initTimeLogger() {
		setStartTimeNS(System.nanoTime());
		myTimeLogger = TimeLogger.create("CLIENT", getClientId(), getStartTimeNS()/1000000);
		try {
			this.tpWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/experiment_log/client"
					+ this.getClientId() + "tp.log", false));
			this.rttWriter = new BufferedWriter(new FileWriter("/home/ec2-user/ASL/experiment_log/client"
					+ this.getClientId() + "rtt.log", false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.logTimer = new Timer();
		this.logTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					tpWriter.write(String.valueOf(reqPerSec.get()) + "\n");
					rttWriter.write(String.valueOf(rttPerSec.get()) + "\n");
					reqCount = reqCount + reqPerSec.get();
					reqPerSec.set(0);
					rttPerSec.set(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
	}
	
	public void closeLoggers() {
		this.logTimer.cancel();
		try {
			this.tpWriter.close();
			this.rttWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("loggers closed after seeing " + reqCount + " requests");
	}
	
	public TimeLogger getMyTimeLogger() {
		return myTimeLogger;
	}

	public long getStartTimeNS() {
		return startTimeNS;
	}

	public void setStartTimeNS(long startTimeNS) {
		this.startTimeNS = startTimeNS;
	}
	
}