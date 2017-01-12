package com.tierzero.stacksonstacks.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LogHandler {
	
	private static Logger logger;
	
	public static void initLogger(Logger modLogger) {
		logger = modLogger;
		logInfo("Logger loaded");
	}

	public static void logInfo(String message) {
		logger.log(Level.INFO, message);
	}
	
	public static void logWarning(String message) {
		logger.log(Level.WARN, message);
	}
	
	public static void logError(String message) {
		logger.log(Level.ERROR, message);
	}
		
}
