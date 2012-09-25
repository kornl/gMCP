substituteEps <- function(graph, eps=10^(-3)) {
	# Call this function recursivly for entangled graphs.
	if ("entangledMCP" %in% class(graph)) {
		for(i in 1:length(graph@subgraphs)) {
			graph@subgraphs[[i]] <- substituteEps(graph@subgraphs[[i]], eps)
		}
		return(graph)
	}
	# Real function:
	if (is.numeric(graph@m)) return(graph)
	m <- matrix(gsub("\\\\epsilon", eps, graph@m), nrow=length(getNodes(graph)))
	options(warn=-1)
	m2 <- matrix(sapply(m, function(x) {
						result <- try(eval(parse(text=x)), silent=TRUE);
						ifelse(class(result)=="try-error",NA,result)
					}), nrow=length(getNodes(graph)))
	options(warn=0)
	if (all(is.na(m)==is.na(m2))) m <- m2
	rownames(m) <- colnames(m) <- getNodes(graph)
	graph@m <- m
	return(graph)
}

replaceVariables <-function(graph, variables=list(), ask=TRUE) {
	# Call this function recursivly for entangled graphs.
	if ("entangledMCP" %in% class(graph)) {
		for(i in 1:length(graph@subgraphs)) {
			graph@subgraphs[[i]] <- replaceVariables(graph@subgraphs[[i]], variables, ask)
		}
		return(graph)
	}
	# Real function:
	greek <- c("alpha", "beta", "gamma", "delta", "epsilon", "zeta", "eta", 
			"theta", "iota", "kappa", "lambda", "mu", "nu", "xi", 
			"omicron", "pi", "rho", "sigma", "tau", "nu", "phi",
			"chi", "psi", "omega")
	shouldBeParsed2Numeric <- TRUE
	if (is.matrix(graph)) { m <- graph } else {m <- graph@m}	
	for (g in c(greek,  letters)) {
		if (length(grep(g, m))!=0) {
			if (is.null(answer <- variables[[g]])) {
				if (ask) {
					if(interactive()) {
						answer <- readline(paste("Value for variable ",g,"? ", sep=""))
					} else {
						stop(paste("Value for variable",g,"not specified."))
					}
				} else {
					shouldBeParsed2Numeric <- FALSE
				}
			}
			if(!is.null(answer)) {
				m <- gsub(paste(ifelse(nchar(g)==1,"","\\\\"), g, sep=""), answer, m)
			}
		}
	}
	if (is.matrix(graph)) return(parse2numeric(m))
	graph@m <- m
	return(parse2numeric(graph))
}

parse2numeric <- function(graph) {
	# Call this function recursivly for entangled graphs.
	if ("entangledMCP" %in% class(graph)) {
		for(i in 1:length(graph@subgraphs)) {
			graph@subgraphs[[i]] <- parse2numeric(graph@subgraphs[[i]])
		}
		return(graph)
	}
	# Real function:
	if (is.matrix(graph)) { m <- graph } else {m <- graph@m}
	names <- rownames(m)
	m <- matrix(sapply(m, function(x) {
						result <- try(eval(parse(text=x)), silent=TRUE);
						ifelse(class(result)=="try-error",NA,result)
					}), nrow=dim(m)[1])
	rownames(m) <- colnames(m) <- names
	if (is.matrix(graph)) return(m)
	graph@m <- m
	return(graph)
}

isEpsilon <- function(w) {
	x <- try(eval(parse(text = gsub("\\\\epsilon", 0, w))), silent=TRUE)
	if ("try-error" %in% class(x)) return(FALSE)
	return(x==0)
}