package org.asl.middleware.database.model;

public class ClientSequence {

	private int client_id;
	
	public static String REGISTER_CLIENT_STRING = "SELECT nextval('client');";
}
