package org.asl.middleware.database.model;

public class ClientTable {

	public static String REGISTER_CLIENT_STRING = "SELECT * FROM  register_client(?);";
	
	public static String READ_MESSAGE_FROM_SENDER = "SELECT * FROM read_message_from_sender(?, ?);";
	
	public static String GET_REGISTERED_CLIENTS_STRING = "SELECT * FROM get_registered_clients();";
}
