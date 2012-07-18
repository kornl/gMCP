substituteEps <- function(graph, eps=10^(-3)) {
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

replaceVariables <-function(graph, variables=list()) {
	greek <- c("alpha", "beta", "gamma", "delta", "epsilon", "zeta", "eta", 
			"theta", "iota", "kappa", "lambda", "mu", "nu", "xi", 
			"omicron", "pi", "rho", "sigma", "tau", "nu", "phi",
			"chi", "psi", "omega")
	if (is.matrix(graph)) { m <- graph } else {m <- graph@m}	
	for (g in c(greek,  letters)) {
		if (length(grep(g, m))!=0) {
			if (is.null(answer <- variables[[g]])) {
				if(interactive()) {
					answer <- readline(paste("Value for variable ",g,"? ", sep=""))
				} else {
					stop(paste("Value for variable",g,"not specified."))
				}
			}
			m <- gsub(paste(ifelse(nchar(g)==1,"","\\\\"), g, sep=""), answer, m) 
		}
	}
	if (is.matrix(graph)) return(parse2numeric(m))
	graph@m <- m
	return(parse2numeric(graph))
}

parse2numeric <- function(graph) {
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