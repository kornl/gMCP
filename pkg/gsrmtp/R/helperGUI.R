getEdges <- function(graph){
	fromL <- c()
	toL <- c()
	weightL <- numeric(0)
	for (node in nodes(graph)) {
		edgeL <- edgeWeights(graph)[[node]]	
		if (length(edgeL)!=0) {
			for (i in 1:length(edgeL)) {
				weight <- try(edgeData(graph, names(edgeL[i]), node,"weight"), silent = TRUE)							
				weight <- ifelse(edgeL[i]==0, NaN, edgeL[i])
				fromL <- c(fromL, node)
				toL <- c(toL, names(edgeL[i]))
				weightL <- c(weight, weightL)
			}
		}
	}
	return(list(from=fromL, to=toL, weight=weightL))
}