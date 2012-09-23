loadJars <- function(jars, java="J5") {
	jarsFullname <- c()	
	classes <- system.file("java", package = "CommonJavaJars", lib.loc = NULL)
	files <- list.files(classes, full.names = FALSE)
	if (java=="J5") {
		files <- grep("J6", files, TRUE, value = TRUE, invert = TRUE)
	}
	for (j in jarsSC) {
		# Always take the newest jar per default:
		jarsFullname <- c(jarsFullname, sort(grep(j, files, TRUE, value = TRUE), decreasing = TRUE)[1])
	}
	
	.jpackage("CommonJavaJars", jars=jarsFullname)
	return(invisible(jarsFullname))
}