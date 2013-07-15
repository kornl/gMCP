## Adapted and extended from the code from http://rwiki.sciviews.org/doku.php?id=developers:runit
unitTestsGMCP <- function(extended=FALSE, java=FALSE, interactive=FALSE, junitLibrary, outputPath) {
	if(!require("RUnit", quietly=TRUE)) {
		stop("Please install package RUnit to run the unit tests.")
	}
	if (extended) Sys.setenv(GMCP_UNIT_TESTS=paste(Sys.getenv("GMCP_UNIT_TESTS"),"extended"))
	if (interactive) Sys.setenv(GMCP_UNIT_TESTS=paste(Sys.getenv("GMCP_UNIT_TESTS"),"interactive"))
	if (missing(outputPath)) {
		if (Sys.getenv("GMCP_UNIT_TEST_OPATH")=="") {
			Sys.setenv(GMCP_UNIT_TEST_OPATH=getwd())
		}
	} else {
		Sys.setenv(GMCP_UNIT_TEST_OPATH=outputPath)
	}
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
	
	if (java || "java" %in% strsplit(Sys.getenv("GMCP_UNIT_TESTS"),",")[[1]]) {
		# Test whether junit*.jar is in classpath
		if (!missing(junitLibrary)) {
			.jaddClassPath(junitLibrary)
		}
		if (Sys.getenv("GMCP_JUNIT_LIBRARY")!="") {
			.jaddClassPath(Sys.getenv("GMCP_JUNIT_LIBRARY"))
		}
		#testClass <- .jcall(.jnew("tests/RControlTest"), "Ljava/lang/Class;", method="getClass")
		testClasses <- .jcall(.jnew("tests/TestSuite"), "[Ljava/lang/Class;", method="getClasses", evalArray=FALSE)
		result <- try(.jcall("org.junit.runner.JUnitCore", "Lorg/junit/runner/Result;", method="runClasses", testClasses))
		if (("try-error" %in% class(result))) {
			cat("JUnit 4 is needed for JUnit tests (See http://www.junit.org/).")
			stop("Please specify the path to junit 4 jar file via junitLibrary.")
		}
		if (.jcall(result, "I", "getFailureCount")>0) {
			cat("------------------- JUNIT TEST SUMMARY --------------------\n\n")
			cat(.jcall(.jnew("tests/TestSuite"), "S", method="getResultString", result))
			stop(paste(.jcall(result, "I", "getFailureCount"),"failures in JUnit tests!"))
		} else {
			cat(.jcall(result, "I", "getRunCount"), " Java Unit Tests successful! (Runtime: ",.jcall(result, "J", "getRunTime")/1000," sec)")
		}
	}
}

equals <- function(graph1, graph2, checkAttributes=FALSE, verbose=FALSE) {
	if (length(getNodes(graph1))!=length(getNodes(graph2))) {
		if (verbose) cat("Wrong number of hypotheses.\n")
		return(FALSE);
	}
	if (any(getNodes(graph1)!=getNodes(graph2))) {
		if (verbose) cat("Names of nodes differ.\n")
		return(FALSE);
	}
	if ("entangledMCP" %in% class(graph1) != "entangledMCP" %in% class(graph2)) {
		if (verbose) cat("Only one graph is of class entangledMCP.\n")
		return(FALSE);
	}
	# Call this function recursivly for entangled graphs.	
	if ("entangledMCP" %in% class(graph1)) {
		equal <- TRUE
		for(i in 1:length(graph1@subgraphs)) {
			if (verbose) cat("Checking subgraphs at position ",i,".\n")
			if (!equals(graph1@subgraphs[[i]], graph2@subgraphs[[i]])) {
				equal <- FALSE
			}
		}
		return(equal)
	}
	# Real function:
	if (any(graph1@m!=graph2@m)) {
		if (verbose) cat("Adjacency matrices differ.\n")
		return(FALSE);
	}
	if (any(graph1@weights!=graph2@weights)) {
		if (verbose) cat("Node weights differ.\n")
		return(FALSE);
	}
	return(TRUE)
	#TODO Implement checkAttributes=TRUE	
}