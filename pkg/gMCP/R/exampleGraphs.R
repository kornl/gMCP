createBonferroniHolmGraph <- function(n, alpha=0.05) {
	alpha <- rep(alpha/n, n)
	hnodes <- paste("H", 1:n, sep="")
	edges <- vector("list", length=n)
	for(i in 1:n) {
		edges[[i]] <- list(edges=hnodes[(1:n)[-i]], weights=rep(1/(n-1),n-1))
	}
	names(edges)<-hnodes
	BonferroniHolmGraph <- new("graphSRMTP", nodes=hnodes, edgeL=edges, alpha=alpha)
	# Visualization settings
	nodeX <- 100+(0:(n-1))*200
	nodeY <- rep(200, n)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(BonferroniHolmGraph) <- list(nodeX=nodeX, nodeY=nodeY)	
	# Label settings
	for (i in 1:n) {
		n1 <- hnodes[i]
		for (j in (1:n)[-i]) {
			n2 <- hnodes[j]
			x <- ((i+j)*200-200)/2
			y <- 200 + ((i-j)*50)
			edgeData(BonferroniHolmGraph, n1, n2, "labelX") <- x
			edgeData(BonferroniHolmGraph, n1, n2, "labelY") <- y
		}
	}	
	return(BonferroniHolmGraph)
}

createGraphFromBretzEtAl <- function(alpha=0.05) {
	# Nodes:
	alpha <- rep(c(alpha/3,0), each=3)	
	hnodes <- paste("H", rep(1:3, 2), rep(1:2, each=3), sep="")
	# Edges:
	edges <- vector("list", length=6)
	edges[[1]] <- list(edges=c("H21","H12"), weights=rep(1/2, 2))
	edges[[2]] <- list(edges=c("H11","H31","H22"), weights=rep(1/3, 3))
	edges[[3]] <- list(edges=c("H21","H32"), weights=rep(1/2, 2))
	edges[[4]] <- list(edges="H21", weights=1)
	edges[[5]] <- list(edges=c("H11","H31"), weights=rep(1/2, 2))
	edges[[6]] <- list(edges="H21", weights=1)
	names(edges)<-hnodes
	# Graph creation
	graph <- new("graphSRMTP", nodes=hnodes, edgeL=edges, alpha=alpha)
	# Visualization settings
	nodeX <- rep(c(100, 300, 500), 2)
	nodeY <- rep(c(100, 300), each=3)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)
	# Label placement
	edgeData(graph, "H11", "H21", "labelX") <- 200
	edgeData(graph, "H11", "H21", "labelY") <- 80	
	edgeData(graph, "H31", "H21", "labelX") <- 400
	edgeData(graph, "H31", "H21", "labelY") <- 80	
	edgeData(graph, "H21", "H11", "labelX") <- 200
	edgeData(graph, "H21", "H11", "labelY") <- 120	
	edgeData(graph, "H21", "H31", "labelX") <- 400
	edgeData(graph, "H21", "H31", "labelY") <- 120	
	edgeData(graph, "H12", "H21", "labelX") <- 150
	edgeData(graph, "H12", "H21", "labelY") <- 250	
	edgeData(graph, "H22", "H11", "labelX") <- 250
	edgeData(graph, "H22", "H11", "labelY") <- 250	
	edgeData(graph, "H32", "H21", "labelX") <- 450
	edgeData(graph, "H32", "H21", "labelY") <- 250	
	edgeData(graph, "H22", "H31", "labelX") <- 350
	edgeData(graph, "H22", "H31", "labelY") <- 250	
	return(graph)	
}

createGraphForParallelGatekeeping <- function(alpha=0.05) {
	# Nodes:
	alpha <- rep(c(alpha/2,0), each=2)	
	hnodes <- paste("H", 1:4, sep="")
	# Edges:
	edges <- vector("list", length=4)
	edges[[1]] <- list(edges=c("H3","H4"), weights=rep(1/2, 2))
	edges[[2]] <- list(edges=c("H3","H4"), weights=rep(1/2, 2))
	edges[[3]] <- list(edges=c("H4"), weights=1)
	edges[[4]] <- list(edges=c("H3"), weights=1)
	names(edges)<-hnodes
	# Graph creation
	graph <- new("graphSRMTP", nodes=hnodes, edgeL=edges, alpha=alpha)
	# Visualization settings
	nodeX <- rep(c(100, 300), 2)
	nodeY <- rep(c(100, 300), each=2)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)
	return(graph)	
}


createGraphForImprovedParallelGatekeeping <- function(alpha=0.05) {
	graph <- createGraphForParallelGatekeeping()
	graph <- addEdge("H3", "H1", graph, 0)
	graph <- addEdge("H4", "H2", graph, 0)
	return(graph)	
}