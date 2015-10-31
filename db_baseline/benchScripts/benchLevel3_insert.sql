-- used for bench level3
-- insert 1000 rows, all for client = receiver = queue = 1
DECLARE
	cnt INTEGER;
BEGIN
	cnt = 0;
	LOOP
		SELECT * FROM send_message(1, 1, 1, 'abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0');
		cnt = cnt + 1;
		IF cnt == 100 THEN
			EXIT;
		END IF;
	END LOOP;
END;