package org.asl.middleware.database.model;

public class ClientTable {

	public static String REGISTER_CLIENT_STRING = "INSERT INTO CLIENT (ON_MIDDLEWARE) VALUES (?) RETURNING ID;";
	
	public static String READ_MESSAGE_FROM_SENDER = "SELECT * FROM MESSAGE WHERE SENDER=? AND RECEIVER=? LIMIT 1;";
}
