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
	
	# The following few lines are based on the code of the rJava .jpackage function
  # TODO This is work in progress. What about MacOS?
	if (length(grep("64-bit", sessionInfo()$platform))>0) {
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
				.jaddClassPath(classes)
				jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
				if (length(jars)) { 
					.jaddClassPath(jars)
				}		
			}
		}	
		# If we have a rJava version > 0.9-3 load JRIEngine.jar and REngine.jar
		if (rJavaVersion > "0.9-3") {
		  classes <- system.file("JRI", package = "CommonJavaJars", lib.loc = NULL)
		  if (nzchar(classes)) {
		    .jaddClassPath(classes)
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
	
	#options(warn=-1)
	#require("graph")
	#options(warn=0)
	
	#TODO How to test for 64-bit R? In the moment I use: .Machine$sizeof.pointer != 8
	#if (length(grep("64-bit", sessionInfo()$platform))==0 || .Machine$sizeof.pointer != 8) {
	#	rversion <- c(sessionInfo()$R.version$major, unlist(strsplit(sessionInfo()$R.version$minor, "\\.")))
	#	if ((rversion[1]==2&&rversion[2]==15&&rversion[3]>=2)|| rversion[1]>=3) warning("WARNING: You may experience crashes due to memory problems.\nPlease try to either i) use an old R<=2.15.2 or ii) R with 64-bit on Windows 64.\nWe are working on this problem.\nIf you experience no crashes everything is fine.")
	#}
	
	# packageStartupMessage or cat for furter information (package incompatibilities / updates)
}  

.onUnload <- function(libpath) {
	# TODO Unload jars?
}
