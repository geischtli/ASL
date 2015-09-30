package org.asl.middleware.database.model;

public class MessageTable {

	public static String SEND_MESSAGE_STRING = "INSERT INTO MESSAGE(SENDER, RECEIVER, QUEUE, CONTENT)" +
			"VALUES (?, ?, ?, ?) RETURNING ID;";
}