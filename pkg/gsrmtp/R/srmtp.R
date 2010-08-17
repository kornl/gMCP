srmtp <- function(graph, pvalues, verbose=FALSE) {
	if (length(pvalues)!=length(nodes(graph))) {
		stop("Length of pvalues must equal number of nodes.")
	}
	if (is.null(names(pvalues))) {
		names(pvalues) <- nodes(graph)
	}
	sequence <- list(graph)
	while(!is.null(node <- getRejectableNode(graph, pvalues))) {
		if (verbose) cat(paste("Node \"",node,"\" can be rejected.\n",sep=""))
		edgesIn <- c()			
		for (node2 in nodes(graph)) {
			i <- which(names(edgeWeights(graph, node2)[[node2]])==node)
			if (length(i)>0) {
				edge <- edgeWeights(graph, node2)[[1]][i]
				names(edge) <- node2
				edgesIn <- c(edgesIn, edge)
			}
		}
		
		edgesOut <- edgeWeights(graph, node)[[node]]
		if (verbose) cat(paste("There are ",length(edgesIn)," incoming and ",length(edgesOut)," outgoing edges.\n",sep=""))
		
		if (all(TRUE == all.equal(edgesOut, rep(0, length(edgesOut))))) {
			if (verbose) cat("Alpha is passed via epsilon-edges.\n")
		} else {
			if (verbose) cat("Alpha is passed via non-epsilon-edges.\n")
			graph2 <- graph
			# New weights are calculated
			for (to in nodes(graph)[nodes(graph)!=node]) {				
				nodeData(graph2, to, "alpha") <- nodeData(graph, to, "alpha")[[to]] + getWeight(graph,node,to) * nodeData(graph, node, "alpha")[[node]]				
				for (from in nodes(graph)[nodes(graph)!=node]) {
					if (from != to) {
						w <- (getWeight(graph,from,to)+getWeight(graph,from,node)*getWeight(graph,node,to))/
								(1-getWeight(graph,from,node)*getWeight(graph,node,from))
						if (to %in% edges(graph)[[from]]) {
							edgeData(graph2,from,to,"weight") <- w
						} else {
							if (!is.nan(w) & w>0) {
								graph2 <- addEdge(from, to, graph2, w)
							}
						}								
					}
				}								
			}
			graph <- graph2
		}
		if (verbose) cat("Removing edges.")
		for (to in names(edgesOut)) {
			graph <- removeEdge(node, to, graph)
		}
		for (from in names(edgesIn)) {
			graph <- removeEdge(from, node, graph)
		}
		nodeData(graph, node, "alpha") <- 0
		nodeData(graph, node, "rejected") <- TRUE
		sequence <- c(sequence, graph)
	}	
	return(new("srmtpResult", graphs=sequence, pvalues=pvalues))
}

getRejectableNode <- function(graph, pvalues) {
	x <- getAlpha(graph)/pvalues
	x[pvalues==0] <- 1
	x[unlist(nodeData(graph, nodes(graph), "rejected"))] <- NaN
	i <- which.max(x)
	if (length(i)==0) return(NULL)
	if (x[i]>1 | all.equal(unname(x[i]),1)[1]==TRUE) {return(nodes(graph)[i])}
	return(NULL)	 
}

getWeight <- function(graph, from, to) {
	weight <- try(edgeData(graph,from,to,"weight"), silent = TRUE)
	if (class(weight)=="try-error") {
		return(0)
	}
	return(weight[[1]])
} 
