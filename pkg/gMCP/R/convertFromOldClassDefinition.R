updateGraphToNewClassDefinition <- function(object) {
	requireLibrary(graph)
	nodes <- object@nodes
	edges <- object@edgeL
	eData <- object@edgeData@data
	nData <- object@nodeData@data
	renderInfo <- object@renderInfo
	m <- matrix(0, nrow=length(nodes), ncol=length(nodes))
	edgeData <- list()
	nodeData <- list()
	w <- c()
	rejected <- c()
	for (n in nodes) {
		w <- c(w, nData[[n]]$nodeWeight)
		rejected <- c(rejected, (!is.null(nData[[n]]$rejected)) && nData[[n]]$rejected)
	}
	names (w) <- names(rejected) <- nodes
	nodeData$rejected <- rejected
	nodeData$X <- renderInfo@nodes$nodeX
	nodeData$Y <- renderInfo@nodes$nodeY
	for (i in 1:length(edges)) {
		indices <- edges[i][[1]]$edges	
		for(j in indices) {
			d <- eData[paste(nodes[i], nodes[j], sep="|")][[1]]
			m[i,j] <- d$weight			
			if (is.nan(d$weight)) {				
				if (!is.null(d$variableWeight)) {
					m[i,j] <- d$variableWeight
				} else if (!is.null(d$epsilon)) {
					m[i,j] <- makeEpsilonString(d$epsilon)	
				}
			}
			if (is.null(edgeData[[nodes[i]]])) edgeData[[nodes[i]]] <- list()
			if (!any(is.null(c(d$labelX, d$labelY)))) {
				if (is.null(edgeData[[nodes[i]]][[nodes[j]]])) edgeData[[nodes[i]]][[nodes[j]]] <- list()
				edgeData[[nodes[i]]][[nodes[j]]]$coordinates <- c(d$labelX, d$labelY)
			}
		}		
	}
	rownames(m) <- colnames(m) <- nodes
	return(new("graphMCP", m=m, weights=w, edgeData=edgeData, nodeData=nodeData))
}