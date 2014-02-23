.onLoad <- function(libname, pkgname) {
	if (!.jniInitialized) {
		.jinit(parameters=c("-Xrs", "-Xss1m"))
	} else {
    warning("JVM was already initialized with unknown memory settings.")
	}
	.jpackage(pkgname)	
	.jpackage("JavaGD")
	
	jars <- c("afcommons", "commons-collections", "commons-lang", 
			"commons-logging", "commons-validator", "forms", 
			"iText", "jhlir.jar", "jlatexmath", "jxlayer", 
			"log4j", "swing-worker")
	
	loadJars(jars)
	
	# Some of following lines are based on the code of the rJava .jpackage function
	if (length(grep("64-bit", sessionInfo()$platform))>0) { # Necessary?!
	  classes <- system.file("jri/x64", package = "rJava", lib.loc = NULL)
	} else {
	  classes <- system.file("jri/i386", package = "rJava", lib.loc = NULL)
	}
	if (nzchar(classes)) {
	  .jaddClassPath(classes)
	}
	classes <- system.file("jri", package = "rJava", lib.loc = NULL)
	if (nzchar(classes)) {
		#.jaddClassPath(classes)
		jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
		if (length(jars)) { 
			.jaddClassPath(jars)
		}		
	}
	
	rJavaVersion <- utils::sessionInfo()$otherPkgs$rJava$Version
	
	# If we have a rJava version < 0.8-3 load JRIEngine.jar and REngine.jar
  if (!is.null(rJavaVersion)) {
  	if (rJavaVersion < "0.8-3") {
			classes <- system.file("R28", package = "CommonJavaJars", lib.loc = NULL)
			if (nzchar(classes)) {
				.jaddClassPath(classes) # Necessary?!
				jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
				if (length(jars)) { 
					.jaddClassPath(jars)
				}		
			}
		}	
		# If we have a rJava version > 0.9-3 load JRIEngine.jar and REngine.jar
    # TODO: Should we check for rJava again containing JRIEngine and REngine.jar in later versions?
		if (rJavaVersion > "0.9-3") {
		  classes <- system.file("JRI", package = "CommonJavaJars", lib.loc = NULL)
		  if (nzchar(classes)) {
		    .jaddClassPath(classes) # Necessary?!
		    jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
		    if (length(jars)) { 
		      .jaddClassPath(jars)
		    }		
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
}  

.onUnload <- function(libpath) {
	# TODO Unload jars?
}
