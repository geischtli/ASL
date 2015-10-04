DROP SCHEMA PUBLIC CASCADE;
CREATE SCHEMA PUBLIC;

CREATE SEQUENCE MIDDLEWARE START 1;

CREATE TABLE CLIENT(
	ID SERIAL PRIMARY KEY,
	ON_MIDDLEWARE INT DEFAULT nextval('middleware')
);

CREATE TABLE QUEUE(
	ID SERIAL PRIMARY KEY,
	CREATOR_CLIENT INT REFERENCES CLIENT (ID)
);

CREATE TABLE MESSAGE(
	ID SERIAL PRIMARY KEY,
	SERNDER INT REFERENCES CLIENT (ID),
	RECEIVER INT REFERENCES CLIENT (ID),
	QUEUE INT REFERENCES QUEUE(ID),
	CONTENT TEXT
);

CREATE OR REPLACE FUNCTION create_queue(creator_client INTEGER)
	RETURNS INTEGER AS $$
DECLARE
	queue_id INTEGER;
BEGIN
	INSERT INTO QUEUE (CREATOR_CLIENT) VALUES (creator_client) RETURNING ID INTO queue_id;
	IF NOT FOUND THEN
		RETURN -1;
	ELSE
		RETURN queue_id;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION delete_queue(queue_id INTEGER) 
	RETURNS INTEGER AS $$
BEGIN
	DELETE FROM Queue WHERE ID = queue_id;
	IF NOT FOUND THEN
		RETURN -1;
	ELSE
		RETURN 0;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION register_client(on_middleware INTEGER)
	RETURNS INTEGER AS $$
DECLARE
	client_id INTEGER;
BEGIN
	INSERT INTO CLIENT (ON_MIDDLEWARE) VALUES (on_middleware) RETURNING ID INTO client_id;
	RETURN client_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_queues_for_client(client_receiver INTEGER)
	RETURNS SETOF INTEGER AS $$
	SELECT DISTINCT QUEUE FROM MESSAGE WHERE RECEIVER = client_receiver;
$$ LANGUAGE sql STABLE;

CREATE OR REPLACE FUNCTION read_all_messages_of_queue(receiver_id INTEGER, queue_id INTEGER)
	RETURNS SETOF MESSAGE AS $$
	SELECT * FROM MESSAGE WHERE RECEIVER = receiver_id AND QUEUE = queue_id;
$$ LANGUAGE sql STABLE;

-- probably also possible in plain sql withouth the NULL-stuff, but dont know about the result in the java, need to test
CREATE OR REPLACE FUNCTION read_message_from_sender(sender_id INTEGER, receiver_id INTEGER)
	RETURNS MESSAGE AS $$
DECLARE
	read_message MESSAGE;
BEGIN
	SELECT * FROM MESSAGE WHERE SENDER = sender_id AND RECEIVER = receiver_id LIMIT 1 INTO read_message;
	IF NOT FOUND THEN
		RETURN NULL;
	ELSE
		RETURN read_message;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION remove_top_message_from_queue(receiver_id INTEGER, queue_id INTEGER)
	RETURNS MESSAGE AS $$
DECLARE
	removed_message MESSAGE;
BEGIN
	DELETE FROM MESSAGE WHERE ctid IN (SELECT ctid FROM MESSAGE WHERE RECEIVER = receiver_id AND QUEUE = queue_id LIMIT 1) RETURNING * INTO removed_message;
	IF NOT FOUND THEN
		RETURN NULL;
	ELSE
		RETURN removed_message;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION send_message(sender_id INTEGER, receiver_id INTEGER, queue_id INTEGER, content TEXT)
	RETURNS INTEGER AS $$
BEGIN
	INSERT INTO MESSAGE (SENDER, RECEIVER, QUEUE, CONTENT) VALUES (sender_id, receiver_id, queue_id, content);
	IF NOT FOUND THEN
		RETURN -1;
	ELSE
		RETURN 0;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION register_middleware()
	RETURNS INTEGER AS $$
DECLARE
	middleware_id INTEGER;
BEGIN
	SELECT nextval('middleware') INTO middleware_id;
	IF NOT FOUND THEN
		RETURN -1;
	ELSE
		RETURN middleware_id;
	END IF;
END;
$$ LANGUAGE plpgsql;

-- create the index, for explanations see in thougths on index.txt
CREATE INDEX msg_rcv_q_idx ON MESSAGE (RECEIVER, QUEUE);