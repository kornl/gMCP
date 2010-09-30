getEdges <- function(graph){
	fromL <- c()
	toL <- c()
	weightL <- numeric(0)
	labelx <- numeric(0)
	labely <- numeric(0)
	curveL <- logical(0)
	for (node in nodes(graph)) {
		edgeL <- edgeWeights(graph)[[node]]	
		if (length(edgeL)!=0) {
			for (i in 1:length(edgeL)) {
				# Label: Are these silent "tries" really tested? Or did I just forgot documentating it?
				weight <- try(edgeData(graph, node, names(edgeL[i]), "weight"), silent = TRUE)
				weight <- ifelse(edgeL[i]==0, NaN, edgeL[i])
				x <- try(unlist(edgeData(graph, node, names(edgeL[i]), "labelX")), silent = TRUE)
				if (class(x)!="try-error") {
					labelx <- c(labelx, x)
				} else {
					labelx <- c(labelx, -100)
				}
				y <- try(unlist(edgeData(graph, node, names(edgeL[i]), "labelY")), silent = TRUE)
				if (class(y)!="try-error") {
					labely <- c(labely, y)
				} else {
					labely <- c(labely, -100)
				}								
				fromL <- c(fromL, node)
				toL <- c(toL, names(edgeL[i]))
				weightL <- c(weightL, weight)
				curve <- node%in%unlist(edges(graph, names(edgeL[i])))
				curveL <- c(curveL, curve)
			}
		}
	}
	return(list(from=fromL, to=toL, weight=weightL, labelx=labelx, labely=labely, curve=curveL))
}