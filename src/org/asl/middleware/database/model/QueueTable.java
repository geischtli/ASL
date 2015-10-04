package org.asl.middleware.database.model;

public class QueueTable {

	public static String CREATE_QUEUE_STRING = "SELECT * FROM create_queue(?);";
	
	public static String DELETE_QUEUE_STRING = "SELECT * FROM delete_queue(?);";
	
	public static String REMOVE_TOP_MESSAGE_STRING = " SELECT * FROM remove_top_message_from_queue(?, ?);";

	public static String READ_ALL_MESSAGES_STRING = "SELECT * FROM  read_all_messages_of_queue(?, ?);";
	
	public static String GET_QUEUES_FOR_CLIENT_STRING = "SELECT * FROM get_queues_for_client(?);";
}