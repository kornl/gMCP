.onLoad <- function(libname, pkgname) {
	.jpackage(pkgname)
	# Does this work?
	.jpackage(rJava)
	## we supply our own JavaGD class
	#.setenv <- if (exists("Sys.setenv")) Sys.setenv else Sys.putenv
	#.setenv("JAVAGD_CLASS_NAME"="org/mutoss/gui/JavaGD")  
}  