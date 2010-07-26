srmtp <- function(graph, pvalues, verbose=FALSE) {
	if (is.null(names(pvalues))) {
		names(pvalues) <- nodes(graph)
	}
	for (node in nodes(graph)) {
		if (verbose) cat(paste("Investigating node \"",node,"\".\n",sep=""))
		if (canBeRejected(graph, node, pvalues)) {
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
			
			print("edgesIn:")
			print(edgesIn)
			print("edgesOut:")
			print(edgesOut)
			
			if (all(TRUE == all.equal(edgesOut, rep(0, length(edgesOut))))) {
				if (verbose) cat("Alpha is passed via epsilon-edges.\n")
			} else {
				if (verbose) cat("Alpha is passed via non-epsilon-edges.\n")
				
				# New weights are calculated
				for (to in names(edgesOut)) {
					nodeData(graph, to, "alpha") <- nodeData(graph, to, "alpha")[[to]] + edgesOut[to] * nodeData(graph, node, "alpha")[[node]] 
					
					for (from in names(edgesIn)) {
						if (from != to) {
							edgeData(graph,from,to,"weight") <-
								(getWeight(graph,from,to)+getWeight(graph,from,node)*getWeight(graph,node,to))/
								(1-getWeight(graph,from,node)*getWeight(graph,node,from))
						}
					}
					
					
				}
				if (verbose) cat("Removing edges.")
				for (to in names(edgesOut)) {
					graph <- removeEdge(node, to, graph)
				}
				for (from in names(edgesIn)) {
					graph <- removeEdge(from, node, graph)
				}
				nodeData(graph, node, "alpha") <- 0
			}
		}
	}	
	return(graph)
}

getWeight <- function(graph, from, to) {
	weight <- try(edgeData(graph,from,to,"weight"))
	if (class(weight)=="try-error") {
		return(0)
	}
	return(weight[[1]])
} 

canBeRejected <- function(graph, node, pvalues) {	
	return(getAlpha(graph)[[node]]>pvalues[[node]] | (all.equal(getAlpha(graph)[[node]],pvalues[[node]])[1]==TRUE));
}