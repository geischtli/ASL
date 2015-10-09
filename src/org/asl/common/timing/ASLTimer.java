package org.asl.common.timing;

public class ASLTimer {

	private String filename;
	
	public ASLTimer(String filename) {
		this.filename = filename;
	}
	
	public static ASLTimer create(String filename) {
		return new ASLTimer(filename);
	}
	
	public void click(Timing timing) {
		
	}
	
}
