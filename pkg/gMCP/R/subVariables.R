substituteEps <- function(graph, eps=10^(-4)) {
	if (is.numeric(graph@m)) return(graph)
	m <- matrix(gsub("\\\\epsilon", eps, graph@m), nrow=length(nodes(graph)))
	options(warn=-1)
	m2 <- matrix(sapply(graph@m, function(x) {
						result <- try(eval(parse(text=x)), silent=TRUE);
						ifelse(class(result)=="try-error",NA,result)
					}), nrow=length(nodes(graph)))
	options(warn=0)
	if (all(is.na(m)==is.na(m2))) m <- m2
	rownames(m) <- colnames(m) <- nodes(graph)
	graph@m <- m
	return(graph)
}


replaceVariables <-function(graph, variables=list()) {
	greek <- c("\\\\alpha", "\\\\beta", "\\\\gamma", "\\\\delta", "\\\\epsilon", "\\\\zeta", "\\\\eta", 
			"\\\\theta", "\\\\iota", "\\\\kappa", "\\\\lambda", "\\\\mu", "\\\\nu", "\\\\xi", 
			"\\\\omicron", "\\\\pi", "\\\\rho", "\\\\sigma", "\\\\tau", "\\\\nu", "\\\\phi",
			"\\\\chi", "\\\\psi", "\\\\omega")
	if(interactive()) {
		
	} else {
		
	}
}