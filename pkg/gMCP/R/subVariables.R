substituteEps <- function(graph, eps=10^(-4)) {
	from <- rep(names(edges(graph)), unlist(lapply(edges(graph),length)))	
	to <- unlist(edges(graph))
	if (length(from)==0) return(graph)
	for (i in 1:length(from)) {		
		p <- unlist(edgeData(graph, from[i], to[i], "epsilon"))
		if (!all(p==0)) {
			text <- gsub("\\\\epsilon", eps, getWeightStr(graph, from[i], to[i]))	
			newWeight <- eval(parse(text=text))
			edgeData(graph, from[i], to[i], "epsilon") <- 0
			edgeData(graph, from[i], to[i], "weight") <- newWeight
		}		
	}
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