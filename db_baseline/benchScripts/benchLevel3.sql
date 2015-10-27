-- this bench level is used to measure the throughtput
-- and latency behaviour when performing the most
-- consting operation, i.e. the top remove when the
-- database is consistently growing by 1 message
-- per this file transaction.
BEGIN
	\setrandom myClientIdA 1 100
	\setrandom myClientIdB 1 100
	\setrandom queueA 1 100
	\setrandom queueB 1 100
	SELECT * FROM send_message(:myClientIdA, :myClientIdA, :queueA, 'abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0');
	SELECT * FROM send_message(:myClientIdB, :myClientIdB, :queueB, 'abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0');
	SELECT * FROM remove_top_message_from_queue(:myClientIdA, :queueA);
END;