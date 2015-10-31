-- used for bench level3
-- remove top message a 100 times, to get average of this operation
DECLARE
	cnt INTEGER;
BEGIN
	cnt = 0;
	LOOP
		SELECT * FROM remove_top_message_from_queue(1, 1);
		cnt = cnt + 1;
		IF cnt == 100 THEN
			EXIT;
		END IF;
	END LOOP;
END;