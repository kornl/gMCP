substituteEps <- function(graph, eps=10^(-4)) {
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
	greek <- c("\\\\alpha", "\\\\beta", "\\\\gamma", "\\\\delta", "\\\\epsilon", "\\\\zeta", "\\\\eta", 
			"\\\\theta", "\\\\iota", "\\\\kappa", "\\\\lambda", "\\\\mu", "\\\\nu", "\\\\xi", 
			"\\\\omicron", "\\\\pi", "\\\\rho", "\\\\sigma", "\\\\tau", "\\\\nu", "\\\\phi",
			"\\\\chi", "\\\\psi", "\\\\omega")
	
	for (g in greek) {
		if (length(grep(g, graph@m))!=0) {
			if (is.null(answer <- variables[[g]])) {
				if(interactive()) {
					answer <- readline(paste("Value for variable ",g,"? ", sep=""))
				} else {
					stop(paste("Value for variable",g,"not specified."))
				}
			}
			graph@m <- gsub(g, answer, graph@m) 
		}
	}
	return(parse2numeric(graph))
}

parse2numeric <- function(graph) {
	m <- matrix(sapply(graph@m, function(x) {
						result <- try(eval(parse(text=x)), silent=TRUE);
						ifelse(class(result)=="try-error",NA,result)
					}), nrow=length(getNodes(graph)))
	rownames(m) <- colnames(m) <- getNodes(graph)
	graph@m <- m
	return(graph)
}