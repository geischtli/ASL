-- used for bench level3
-- insert 1000 rows, all for client = receiver = queue = 1
BEGIN
	DECLARE @cnt INT = 0
	WHILE @cnt < 1000
	BEGIN
		SELECT * FROM send_message(1, 1, 1, 'abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0abcdhjels0');
	END;
END;