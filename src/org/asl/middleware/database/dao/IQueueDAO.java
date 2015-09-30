package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.CreateQueueException;

public interface IQueueDAO {

	public int createQueue(int creator_id) throws CreateQueueException;
}
