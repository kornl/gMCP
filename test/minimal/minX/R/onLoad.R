.onLoad <- function(libname, pkgname) {
	if (!.jniInitialized) {
		.jinit(parameters=c("-Xrs", "-Xss1m"))
	}
	.jpackage(pkgname)	
	#.jpackage("JavaGD")
	#.jpackage("JGR")
	
	#jars <- c("afcommons", "commons-collections", "commons-lang", 
	#		"commons-logging", "commons-validator", "forms", 
	#		"iText", "jhlir.jar", "jlatexmath", "jxlayer", 
	#		"log4j", "swing-worker")
	
	#loadJars(jars)
	
	# The following few lines are based on the code of the rJava .jpackage function
  # TODO This is work in progress. What about MacOS?
	#if (length(grep("64-bit", sessionInfo()$platform))>0) {
	#  classes <- system.file("jri/x64", package = "rJava", lib.loc = NULL)
	#} else {
	#  classes <- system.file("jri/i386", package = "rJava", lib.loc = NULL)
	#}
	#if (nzchar(classes)) {
	#  .jaddClassPath(classes)
	#}
	classes <- system.file("jri", package = "rJava", lib.loc = NULL)
	if (nzchar(classes)) {
		#.jaddClassPath(classes)
		jars <- grep(".*\\.jar", list.files(classes, full.names = TRUE), TRUE, value = TRUE)
		if (length(jars)) { 
			.jaddClassPath(jars)
		}		
	}
	
	#TODO How to test for 64-bit R? In the moment I use: .Machine$sizeof.pointer != 8
	#if (length(grep("64-bit", sessionInfo()$platform))==0 || .Machine$sizeof.pointer != 8) {
	#	rversion <- c(sessionInfo()$R.version$major, unlist(strsplit(sessionInfo()$R.version$minor, "\\.")))
	#	if ((rversion[1]==2&&rversion[2]==15&&rversion[3]>=2)|| rversion[1]>=3) warning("WARNING: You may experience crashes due to memory problems.\nPlease try to either i) use an old R<=2.15.2 or ii) R with 64-bit on Windows 64.\nWe are working on this problem.\nIf you experience no crashes everything is fine.")
	#}
	
	# packageStartupMessage for furter information (package incompatibilities / updates)
}  

.onUnload <- function(libpath) {
	# TODO Unload jars?
}
