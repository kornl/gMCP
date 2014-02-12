.onLoad <- function(libname, pkgname) {
  if (!.jniInitialized) {
    .jinit(parameters=c("-Xrs", "-Xss1m"))
  }
  .jpackage(pkgname)	
  .jpackage("JavaGD")
  .jpackage("JGR")
  
  jars <- c(#"afcommons", 
    "commons-collections", "commons-lang", 
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
