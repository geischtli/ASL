package org.asl.common.timing;

import java.util.function.Consumer;
import java.util.function.Function;

public class Timer {

	public long takeTime(Function<String, Consumer<Integer>> c, String s, int id) {
		long start = System.nanoTime();
		c.apply(s).accept(id);
		long end = System.nanoTime();
		return (end - start)/1000000;
	}
	
}
