.onLoad <- function(libname, pkgname) {
	if (!.jniInitialized) {
		.jinit(parameters="-Xrs")
	}
	.jpackage(pkgname)	
	.jpackage("JavaGD")
	
	jars <- c("commons-collections", "commons-lang", 
			"commons-logging", "commons-validator", "forms", 
			"iText", "jhlir.jar", "jlatexmath", "jxlayer", 
			"log4j", "swing-worker")
	
	loadJars(jars)
	
	# The following few lines are based on the code of the rJava .jpackage function
	classes <- system.file("jri", package = "rJava", lib.loc = NULL)
	if (nzchar(classes)) {
		.jaddClassPath(classes)
		jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
		if (length(jars)) { 
			.jaddClassPath(jars)
		}		
	}
	
	# If we have a rJava version < 0.8-3 load JRIEngine.jar and REngine.jar
    if (!is.null(sessionInfo()$otherPkgs$rJava$Version) && sessionInfo()$otherPkgs$rJava$Version < "0.8-3") {
		classes <- system.file("R28", package = "CommonJavaJars", lib.loc = NULL)
		if (nzchar(classes)) {
			.jaddClassPath(classes)
			jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
			if (length(jars)) { 
				.jaddClassPath(jars)
			}		
		}
	}
	
	## We supply our own JavaGD class
	Sys.setenv("JAVAGD_CLASS_NAME"="org/mutoss/gui/JavaGD")  
	
	# Optional Deducer integration:
	if(exists(".deducer")) {
		if (!is.null(.deducer)) {
			deducer.addMenuItem("Multiple Test Graph",,"graphGUI()","Analysis")
			if(.jgr){
				jgr.addMenuSeparator("Analysis")
				jgr.addMenuItem("Analysis","Multiple Test Graph","graphGUI()")
			}
		}
	}
	
	#options(warn=-1)
	#require("graph")
	#options(warn=0)
	
	# packageStartupMessage or cat for furter information (package incompatibilities / updates)
}  
