package org.asl.middleware.database.model;

public class QueueTable {

	public static String CREATE_QUEUE_STRING = "INSERT INTO QUEUE (CREATOR_CLIENT) VALUES (?);";
	public static String REMOVE_TOP_MESSAGE_STRING = "REMOVE FROM MESSAGE WHERE RECEIVER = ? AND QUEUE = ? LIMIT 1;"; //TODO: Check if this sql statement is correct
}