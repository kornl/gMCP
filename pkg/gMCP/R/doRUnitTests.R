## Adapted from the code from http://rwiki.sciviews.org/doku.php?id=developers:runit
doUnitTestsForGMCP <- function(extended=FALSE) {
	library(RUnit)
	pkg <- "gMCP" 
	path <- system.file("unitTests", package=pkg)
	cat("\nRunning unit tests\n")
	print(list(pkg=pkg, getwd=getwd(), pathToUnitTests=path))
	
	library(package=pkg, character.only=TRUE)
	
	## If desired, load the name space to allow testing of private functions
	## if (is.element(pkg, loadedNamespaces()))
	##     attach(loadNamespace(pkg), name=paste("namespace", pkg, sep=":"), pos=3)
	##
	## or simply call PKG:::myPrivateFunction() in tests
	
	## --- Testing ---
	
	## Define tests
	testSuite <- defineTestSuite(name=paste(pkg, "unit testing"), dirs=path)
	
	## Run
	tests <- runTestSuite(testSuite)
	
	## Default report name
	pathReport <- file.path(path, "report")
	
	## Report to stdout and text files
	cat("------------------- UNIT TEST SUMMARY ---------------------\n\n")
	printTextProtocol(tests, showDetails=FALSE)
	printTextProtocol(tests, showDetails=FALSE,
			fileName=paste(pathReport, "Summary.txt", sep=""))
	printTextProtocol(tests, showDetails=TRUE,
			fileName=paste(pathReport, ".txt", sep=""))
}
