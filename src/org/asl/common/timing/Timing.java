//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.timing;

public enum Timing {
	MATLAB_INDEX_PLACEHOLDER, // 0 --> only used to make index begin with 1 (as in matlab)
	CLIENT_START_WRITE, // 1
	CLIENT_END_WRITE, // 2
	CLIENT_START_READ, // 3
	CLIENT_END_READ, // 4
	CLIENT_START_POSTPROCESSING, // 5
	CLIENT_END_POSTPROCESSING, // 6
	MIDDLEWARE_START_READ, // 7
	MIDDLEWARE_END_READ, // 8
	MIDDLEWARE_START_PROCESSING, // 9
	MIDDLEWARE_GOT_CONNECTION, // 10
	MIDDLEWARE_START_QUERY, // 11
	MIDDLEWARE_END_QUERY, // 12
	MIDDLEWARE_START_COMMIT, // 13
	MIDDLEWARE_END_COMMIT, // 14
	MIDDLEWARE_END_PROCESSING, // 15
	MIDDLEWARE_START_WRITE, // 16
	MIDDLEWARE_END_WRITE, // 17
	EXPERIMENT_START_REQUEST,
	EXPERIMENT_END_REQUEST
}