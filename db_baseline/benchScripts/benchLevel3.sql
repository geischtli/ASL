-- this bench level is used to measure the throughtput
-- and latency behaviour when repeatidly inserting
-- new messages into the system. The main interest
-- is about where the database begins to slow down
-- expects 100 clients and queues in the database registered
BEGIN
	\setrandom myClientId 1 100
	\setrandom queue 1 100
	SELECT * FROM send_message(:myClientId, :myClientId, :queue, 'this is some random content to simulate real world behaviour');
END;