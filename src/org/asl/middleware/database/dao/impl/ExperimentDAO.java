package org.asl.middleware.database.dao.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.asl.common.request.types.experiments.exceptions.BaselineDummyException;
import org.asl.middleware.MiddlewareInfo;
import org.asl.middleware.database.config.ASLDatabase;
import org.asl.middleware.database.connectionpool.ConnectionWrapper;
import org.asl.middleware.database.dao.IExperimentDAO;
import org.asl.middleware.database.model.ExperimentTable;

public class ExperimentDAO implements IExperimentDAO {

	public static ExperimentDAO getExperimentDAO() {
		return new ExperimentDAO();
	}
	
	@Override
	public void baselineDummy(MiddlewareInfo mi) throws BaselineDummyException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement baselineDummy = conn.get().prepareStatement(ExperimentTable.BASELINE_DUMMY_STRING);
			baselineDummy.execute();
		} catch (IOException | InterruptedException | ExecutionException | SQLException e) {
			throw new BaselineDummyException(e);
		}
	}

}
