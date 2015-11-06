//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.dateTuple;

import java.util.Date;

public class DateTriple {
	public final Date sendTime; // when was request sent on middleware
	public final Date arrivalTime; // when arrive on db (before insert)
	public final Date returnTime; // when return on middleware
	
	public DateTriple(Date sendTime, Date arrivalTime, Date returnTime) {
		this.sendTime = sendTime;
		this.arrivalTime = arrivalTime;
		this.returnTime = returnTime;
	}
}
