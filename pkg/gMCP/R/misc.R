checkValidAlpha <- function(alpha) {
	if(any(0 > alpha | alpha > 1)) {
		stop("invalid alpha: alphas must be between 0 and 1")
	}
	if(sum(alpha) > 1) {
		stop("invalid alpha: the sum of all alphas must be less than 1")
	}
}

# Converts a string like "5+3*e+5*e^2" to the tupel representation c(5,3,5) 
parseEpsPolynom <- function(s) {
	e <- polynom()
	p <- try(eval(parse(text=s)))
	if (class(p)=="try-error") {
		stop("String does not represent a polynom in e.")
	}
	if(is.numeric(p)) {
		return(p)
	} else {
    	return(coef(p))
	}
}

getDebugInfo <- function() {	
	if (exists(".InitialGraph")) {
		graphTXT <- paste(capture.output(print(.InitialGraph)), collapse="\n")
		matrixTXT <- paste("m <-",paste(capture.output(dput(graph2matrix(.InitialGraph))), collapse="\n"),"\n")
		weightsTXT <- paste("w <-",paste(capture.output(dput(getWeights(.InitialGraph))), collapse="\n"),"\n")
		createTXT <- paste("graph <- matrix2graph(m)", "setWeights(graph, w)", sep="\n")
		return(paste(graphTXT, matrixTXT, weightsTXT, createTXT, sep="\n"))
	}
	return("Graph not available.")
}