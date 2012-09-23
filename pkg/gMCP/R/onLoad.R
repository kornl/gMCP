.onLoad <- function(libname, pkgname) {
	if (!.jniInitialized) {
		.jinit(parameters="-Xrs")
	}
	.jpackage(pkgname)	
	.jpackage("JavaGD")
	
	jarsSC <- c("commons-collections", "commons-lang", 
			"commons-logging", "commons-validator", "forms", 
			"iText", "jhlir.jar", "jlatexmath", "jxlayer", 
			"log4j", "mysql-connector-java", 
			"swing-worker")
	
	jars <- c()	
	classes <- system.file("java", package = "CommonJavaJars", lib.loc = NULL)
	files <- list.files(classes, full.names = TRUE)
	# For now always ignore the jars that require Java >= 6.
	files <- grep("J6", files, TRUE, value = TRUE, invert = TRUE)
	for (j in jarsSC) {
		jars <- c(jars, grep(j, files, TRUE, value = TRUE)[1])
	}
	
	.jpackage("CommonJavaJars", jars=jars)
	
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
