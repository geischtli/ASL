package org.asl.middleware.database.model;

public class QueueTable {

	public static String CREATE_QUEUE_STRING = "INSERT INTO QUEUE (CREATOR_CLIENT) VALUES (?) RETURNING ID;";
	
	public static String REMOVE_TOP_MESSAGE_STRING = "DELETE FROM MESSAGE WHERE ctid IN " +
			"(SELECT ctid FROM MESSAGE WHERE RECEIVER=? AND QUEUE=? LIMIT 1) RETURNING *;";
	
	public static String READ_ALL_MESSAGES_STRING = "SELECT * FROM MESSAGE WHERE RECEIVER=? AND QUEUE=?;";
	
	public static String GET_QUEUES_FOR_CLIENT_STRING = "SELECT * FROM get_queues_for_client(?);";
}