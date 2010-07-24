srmtp <- function(graph, pvalues) {
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
	return(node=="H1")
	# TODO
	return(TRUE);
}