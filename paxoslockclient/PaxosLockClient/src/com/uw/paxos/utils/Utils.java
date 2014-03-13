package com.uw.paxos.utils;

import java.text.SimpleDateFormat;

public class Utils {
	
	private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
	
	public static void logMessage(String message) {
		System.out.println("Time: " + format.format(System.currentTimeMillis()) + ", " + message);
    }
	
	public static void logError(String error) {
	    System.err.println("Time: " + format.format(System.currentTimeMillis()) + ", " + error);
    }
	
	public static void printErrorAndExit(String error) {
	    logError(error);
	    System.exit(1);
    }
}