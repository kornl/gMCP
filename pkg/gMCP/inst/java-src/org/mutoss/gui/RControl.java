package org.mutoss.gui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.af.commons.errorhandling.ErrorDialog;
import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.logging.ApplicationLog;
import org.af.commons.logging.LoggingSystem;
import org.af.jhlir.backends.rengine.RCallServicesREngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.JRI.JRIEngine;

public class RControl {

	private static final Log logger = LogFactory.getLog(RControl.class);

	protected static RControl rc = null;

	public static RControl getRControl(boolean debug) {
		if (rc==null) {
			rc = new RControl(debug); 
		}
		return rc;
	}

	public static RCallServicesREngine getR() {
		getRControl(true);
		return rcs;
	}

	protected static RCallServicesREngine rcs = null;
	public static DebugTextConsole console = null;

	protected RControl(boolean debug) {
		if (!LoggingSystem.alreadyInitiated()) {
			LoggingSystem.init(
					"/org/mutoss/gui/commons-logging.properties",
					System.getProperty("eclipse") == null && !debug,
					System.getProperty("eclipse") != null || debug,
					new ApplicationLog());
			ErrorHandler.init("rohmeyer@small-projects.de", "http://www.algorithm-forge.com/report/bugreport.php", true, true, ErrorDialog.class);

		}
		Rengine rengine = Rengine.getMainEngine();
		if (rengine == null) {
			// Call java with VM arguments: -Declipse="true"
			if (System.getProperty("eclipse") != null) {
				console = new DebugTextConsole();
				rengine = new Rengine(new String[] {"--vanilla"}, true, console);
			} else {
				rengine = new Rengine();
			}
		}
		try {
			rcs = new RCallServicesREngine(new JRIEngine(rengine));
			if (System.getProperty("eclipse") != null) {
				//rcs.eval(".setenv <- if (exists(\"Sys.setenv\")) Sys.setenv else Sys.putenv");
				//rcs.eval(".setenv(\"JAVAGD_CLASS_NAME\"=\"org/mutoss/gui/JavaGD\")");
				//rcs.eval("require(JavaGD)");					
				rcs.eval("require(gMCP)");
				rcs.eval("graph <- createGraphFromBretzEtAl()");
				rcs.eval("graph <- createBonferroniHolmGraph(5)");
			}
		} catch (REngineException e) {
			ErrorHandler.getInstance().makeErrDialog("Error creating RCallServicesREngine!", e);
		}
		System.setOut(new PrintStream(new LoggingOutputStream(logger), true));
	}

}

class LoggingOutputStream extends ByteArrayOutputStream { 

	private String lineSeparator;    
	Log logger;

	public LoggingOutputStream(Log logger) { 
		super(); 
		this.logger = logger; 
		lineSeparator = System.getProperty("line.separator"); 
	} 

	public void flush() throws IOException { 
		String record; 
		synchronized(this) { 
			super.flush(); 
			record = this.toString(); 
			super.reset(); 
			if (record.length() == 0 || record.equals(lineSeparator)) return; 
			logger.info(record); 
		} 
	} 
} 
