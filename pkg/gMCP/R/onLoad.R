.onLoad <- function(libname, pkgname) {
	.jinit(parameters="-Xrs")
	.jpackage(pkgname)

	classes <- system.file("jri", package = "rJava", lib.loc = NULL)
	if (nchar(classes)) {
		.jaddClassPath(classes)
		jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
		if (length(jars)) { 
			.jaddClassPath(jars)
		}		
	}
	
	classes <- system.file("java", package = "JavaGD", lib.loc = NULL)
	if (nchar(classes)) {
		.jaddClassPath(classes)
		jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
		if (length(jars)) { 
			.jaddClassPath(jars)
		}		
	}
	
	classes <- system.file("java", package = "CommonJavaJars", lib.loc = NULL)
	if (nchar(classes)) {
		.jaddClassPath(classes)
		jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
		if (length(jars)) { 
			.jaddClassPath(jars)
		}		
	}
	
	# If we have a rJava version < 0.8-3 load JRIEngine.jar and REngine.jar
    if (!is.null(sessionInfo()$otherPkgs$rJava$Version) && sessionInfo()$otherPkgs$rJava$Version < "0.8-3") {
		classes <- system.file("R28", package = "CommonJavaJars", lib.loc = NULL)
		if (nchar(classes)) {
			.jaddClassPath(classes)
			jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
			if (length(jars)) { 
				.jaddClassPath(jars)
			}		
		}
	}
	
	## we supply our own JavaGD class
	.setenv <- if (exists("Sys.setenv")) Sys.setenv else Sys.putenv
	.setenv("JAVAGD_CLASS_NAME"="org/mutoss/gui/JavaGD")  
	
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
	
	# UNFORTUNATELY THIS DOES NOT WORK, SINCE THE LOADING FAILS EARLIER (graph as suggest would be a workaround):
	# If only install.packages("gMCP") is called, the package graph is still missing.
	# For these people (who did not follow the install instructions) we install the graph package:
	if(!require("graph", character.only=TRUE)) {
		if (interactive()) {
			cat("Required package graph is missing.\n")
			answer <- readline("Do you want to install it (y/N)? ")
			if (substr(answer, 1, 1) %in% c("y","Y")) {
				source("http://bioconductor.org/biocLite.R")
				biocLite("graph")
				require("graph")
			} else {
				warning("Please install package graph from Bioconductor to use gMCP!")
			}
		} else {
			warning("Please install package graph from Bioconductor to use gMCP!")
		}
	}
}  
