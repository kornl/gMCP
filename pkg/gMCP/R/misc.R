checkValidWeights <- function(weights) {
	if(!is.numeric(weights)) {
		stop("Weights have to be numeric!")
	}
	if(any(is.na(weights) | is.infinite(weights))) {
		warning("Some of the weights are not real numbers. NA, NaN, Inf and -Inf are not supported.")
	}
	if(any(0 > weights | weights > 1 + .Machine$double.eps ^ 0.25)) {
		stop("Invalid weights: weights must be between 0 and 1")
	}
	if(sum(weights) > 1) {
		stop("Invalid weights: the sum of all weights must be less than 1")
	}
}

# Converts a string like "5+3*e+5*e^2" to the tupel representation c(5,3,5) 
parseEpsPolynom <- function(s) {
	env <- new.env(parent = baseenv())
	assign("epsilon", polynom(), envir=env)
	p <- try(eval(parse(text=s), envir=env), silent = TRUE)
	if (class(p)=="try-error") {
		stop("String does not represent a polynom in epsilon.")
	}
	if(is.numeric(p)) {
		return(p)
	} else {
    	return(coef(p))
	}
}

getDebugInfo <- function() {
	graphs <- ls(pattern="\\.InitialGraph*", all.names=TRUE, envir=globalenv())
	if (exists(".tmpGraph")) {
		graphs <- c(graphs, ".tmpGraph")
	}
	graphInfo <- c()
	for (graph in graphs) {
		.DebugGraph <- get(graph, envir=globalenv())
		graphTXT <- paste(capture.output(print(.DebugGraph)), collapse="\n")
		matrixTXT <- paste("m <-",paste(capture.output(dput(graph2matrix(.DebugGraph))), collapse="\n"),"\n")
		weightsTXT <- paste("w <-",paste(capture.output(dput(getWeights(.DebugGraph))), collapse="\n"),"\n")
		createTXT <- paste("graph <- matrix2graph(m)", "setWeights(graph, w)", sep="\n")
		graphInfo <- c(graphInfo, paste(graphTXT, matrixTXT, weightsTXT, createTXT, sep="\n"))
	}
	if (length(graphInfo)!=0) {
		return(paste(graphInfo, collapse="\n\n"))
	}
	return("Graph not available.")
}

bdiagNA <- function(...) {	
	if (nargs() == 0) 
		return(matrix(nrow=0, ncol=0))
	if (nargs() == 1 && !is.list(...)) 
		return(as.matrix(...))
	asList <- if (nargs() == 1 && is.list(...)) ... else list(...)
	if (length(asList) == 1) 
		return(as.matrix(asList[[1]]))
	n <- 0
	for (m in asList) {
		if (!is.matrix(m)) {
			stop("Only matrices are allowed as arguments.")
		}
		if (dim(m)[1]!=dim(m)[2]) {
			stop("Only quadratic matrices are allowed.")
		}
		n <- n + dim(m)[1]	
	}
	M <- matrix(NA, nrow=n, ncol=n)
	k <- 0
	for (m in asList) {
		for (i in 1:dim(m)[1]) {
			for (j in 1:dim(m)[1]) {
				M[i+k,j+k] <- m[i,j]
			}
		}
		k <- k + dim(m)[1]	
	}	
	return(M)
}

requireLibrary <- function(package) {
	if(!require(package, character.only=TRUE)) {
		answer <- readline(paste("Package ",package," is required - should we install it?", sep=""))
		if (substr(answer, 1, 1) %in% c("y","Y")) {
			install.packages(package)
			require(package, character.only=TRUE)
		} else {
			stop(paste("Required package ",package," should not be installed.", sep=""))
		}
	}
}