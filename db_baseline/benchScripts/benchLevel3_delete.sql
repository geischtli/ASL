-- used for bench level3
-- remove top message a 100 times, to get average of this operation
BEGIN
	SELECT * FROM remove_top_message_from_queue(1, 1);
END;