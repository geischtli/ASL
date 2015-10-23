-- delete the pgbench_account table, because its big and not needed
-- in this experiment
DROP TABLE pgbench_accounts;

-- simulate a shared resource among all connection/db threads
CREATE SEQUENCE SHARED START 1;

-- increment the shared counter
CREATE OR REPLACE FUNCTION increment_shared_counter()
	RETURNS INTEGER AS $$
DECLARE
	next_val INTEGER;
BEGIN
	SELECT nextval('SHARED') INTO next_val;
	RETURN next_val;
END;
$$ LANGUAGE plpgsql;