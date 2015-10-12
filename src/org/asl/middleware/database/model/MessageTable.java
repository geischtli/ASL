package org.asl.middleware.database.model;

public class MessageTable {

	public static String SEND_MESSAGE_STRING = "SELECT * FROM send_message(?, ?, ?, ?);";
	public static String GET_NUMBER_OF_MESSAGES_STRING = "SELECT * FROM get_number_of_messages();";
}