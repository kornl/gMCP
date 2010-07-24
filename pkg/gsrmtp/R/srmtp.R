srmtp <- function(graph, pvalues) {
	if (is.null(names(pvalues))) {
		names(pvalues) <- nodes(graph)
	}
	for (node in nodes(graph)) {
		if (canBeRejected(graph, node, pvalues)) {
			for (to in edges(graph)[[node]]) {
				graph <- removeEdge(node, to, graph)
			}
		}
	}	
	return(graph)
}

canBeRejected <- function(graph, node, pvalues) {	
	return(getAlpha(graph)[[node]]>pvalues[[node]] | (all.equal(getAlpha(graph)[[node]],pvalues[[node]])==TRUE));
}