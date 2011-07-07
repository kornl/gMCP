substituteEps <- function(graph, eps=10^(-4)) {
	if (is.numeric(graph@m)) return(graph)
	m <- matrix(gsub("\\\\epsilon", eps, graph@m),nrow=length(nodes(graph)))
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