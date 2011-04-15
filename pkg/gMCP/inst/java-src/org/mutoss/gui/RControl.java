package org.mutoss.gui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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
			ErrorHandler.init("rohmeyer@small-projects.de", "http://www.algorithm-forge.com/report/bugreport.php", true, true, ErrorDialogSGTK.class);

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
				rcs.eval("require(gMCP)");				
				rcs.eval("graph <- createBonferroniHolmGraph(3)");
				rcs.eval("graph2 <- createGraphFromBretzEtAl()");
				rcs.eval("m <- matrix(0, nrow=2, ncol=2)");
				rcs.eval("dunnettM <- matrix(c(1,1/2,1/2,1), nrow=2)");
				rcs.eval("mu <- c(0.860382, 0.9161474, 0.9732953)");
				rcs.eval("sdEst <- c(0.8759528, 1.291310, 0.8570892)");
				rcs.eval("pval <- c(0.01260, 0.05154, 0.02124)/2");
			}
		} catch (REngineException e) {
			ErrorHandler.getInstance().makeErrDialog("Error creating RCallServicesREngine!", e);
		}
		if (System.getProperty("eclipse") == null && !debug) System.setOut(new PrintStream(new LoggingOutputStream(logger), true));
	}
	
	public static String getFraction(Double d, int cycles) {
		return RControl.getR().eval("as.character(fractions("+d+(cycles==-1?"":", cycles="+cycles)+"))").asRChar().getData()[0];		
	}
	
	public static String getFraction(Double d, boolean useUnicode) {
		return getFraction(d, useUnicode, -1);
	}

	public static String getFraction(Double d, boolean useUnicode, int cycles) {
		String f = getFraction(d, cycles);
		if (true) return f; //TODO boolean useUnicode is ignored - do we want to use this somewhere?
		if (!useUnicode) { return f; }
		if (f.equals("1/2")) return("½");
		if (f.equals("1/3")) return("⅓");
		if (f.equals("2/3")) return("⅔");
		if (f.equals("1/4")) return("¼");
		if (f.equals("3/4")) return("¾");
		/* The following does often not work:
		if (f.equals("1/5")) return("⅕");
		if (f.equals("2/5")) return("⅖");
		if (f.equals("3/5")) return("⅗");
		if (f.equals("4/5")) return("⅘");
		if (f.equals("1/6")) return("⅙");
		if (f.equals("5/6")) return("⅚");
		if (f.equals("1/8")) return("⅛");
		if (f.equals("3/8")) return("⅜");
		if (f.equals("5/8")) return("⅝");
		if (f.equals("7/8")) return("⅞");*/
		return f;
	}

	public static String getFraction(Double d) {
		return getFraction(d, -1);
	}

	public static boolean exists(String obj) {		
		return getR().eval("exists(\""+obj+"\")").asRLogical().getData()[0];
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
