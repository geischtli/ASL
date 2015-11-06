//////////////////////////////////////////////////
// Semester:         Fall 2015
//
// Author:           Sandro Huber
// Email:            sanhuber@student.ethz.ch
// Lecture: 	     Advanced System Lab
//
//////////////////////////////////////////////////

package org.asl.common.propertyparser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyParser {

	private final String filename;
	private Properties props;
	
	public PropertyParser(String filename) {
		this.filename = filename;
		this.props = new Properties();
	}
	
	public static PropertyParser create(String filename) {
		return new PropertyParser(filename);
	}
	
	public PropertyParser parse() {
		try {
			props.loadFromXML(new FileInputStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public String getProperty(PropertyKey type) {
		return props.getProperty(type.toString());
	}
	
}
