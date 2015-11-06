//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

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
import org.asl.middleware.database.dao.common.CommonDAO;
import org.asl.middleware.database.model.ExperimentTable;

public class ExperimentDAO implements IExperimentDAO {

	public static ExperimentDAO getExperimentDAO() {
		return new ExperimentDAO();
	}
	
	@Override
	public void baselineDummy(int clientId, int requestId, MiddlewareInfo mi) throws BaselineDummyException {
		try (ConnectionWrapper conn = ASLDatabase.getNewConnection().get()) {
			PreparedStatement baselineDummy = conn.get().prepareStatement(ExperimentTable.BASELINE_DUMMY_STRING);
			CommonDAO.executeQuery(conn.get(), baselineDummy, clientId, requestId, mi);
		} catch (IOException | InterruptedException | ExecutionException | SQLException e) {
			throw new BaselineDummyException(e);
		}
	}

}
