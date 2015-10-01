package org.asl.middleware.database.model;

public class QueueTable {

	public static String CREATE_QUEUE_STRING = "INSERT INTO QUEUE (CREATOR_CLIENT) VALUES (?);";
	public static String REMOVE_TOP_MESSAGE_STRING = "DELETE FROM MESSAGE WHERE ctid IN " +
			"(SELECT ctid FROM MESSAGE WHERE RECEIVER=? AND QUEUE=? LIMIT 1) RETURNING *;";
}