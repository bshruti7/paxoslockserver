package com.uw.paxos.utils;

public class Utils {
	public static void logMessage(String message) {
		System.out.println("Time: " + System.currentTimeMillis() + ", " + message);
    }
	
	public static void logError(String error) {
	    System.err.println("Time: " + System.currentTimeMillis() + ", " + error);
    }
	
	public static void printErrorAndExit(String error) {
	    logError(error);
	    System.exit(1);
    }
}
