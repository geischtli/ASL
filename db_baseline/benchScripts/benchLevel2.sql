-- this level tries to simulate the real working environment of the whole ASL
-- project. This also means we work with the standard tables and functions
BEGIN
	\set myClientId :client_id + 1
	\set numClients :scale
	\setrandom queue 1 :numClients
	SELECT * FROM send_message(:myClientId, :myClientId, :queue, 'this is some random content to simulate real world behaviour');
	SELECT * FROM get_queues_for_client(:myClientId);
	SELECT * FROM read_all_messages_of_queue(:myClientId, :queue);
	SELECT * FROM read_message_from_sender(:myClientId, :myClientId);
	SELECT * FROM remove_top_message_from_queue(:myClientId, :queue);
END;