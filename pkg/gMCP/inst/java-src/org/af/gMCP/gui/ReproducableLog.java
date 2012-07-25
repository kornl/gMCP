package org.af.gMCP.gui;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReproducableLog {
	
	private static final Log logger = LogFactory.getLog(ReproducableLog.class);
	
	static Vector<String> log = new Vector<String>();
	static Vector<String> logR = new Vector<String>();
	static Vector<String> logGUI = new Vector<String>();
	
	public static void log(String s) {
		log.add(s);
		logger.info(s);
	}
	
	public static void logR(String s) {
		logR.add(s);
		log.add(s);
		logger.info("R: "+s);
	}
	
	public static void logGUI(String s) {
		logGUI.add(s);
		log.add(s);
		logger.info("GUI: "+s);
	}	
	
}
