-- This is a function which is only used for the baseline experiment
-- of the database. It does per se nothing. This allows to simulate the
-- real minimum work a database could do.
CREATE OR REPLACE FUNCTION baseline_dummy()
	RETURNS VOID AS $$
BEGIN
END;
$$ LANGUAGE plpgsql;