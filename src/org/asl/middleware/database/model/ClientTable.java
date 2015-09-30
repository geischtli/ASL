package org.asl.middleware.database.model;

public class ClientTable {

	public static String REGISTER_CLIENT_STRING = "INSERT INTO CLIENT (ON_MIDDLEWARE) VALUES (?) RETURNING ID;";
}
