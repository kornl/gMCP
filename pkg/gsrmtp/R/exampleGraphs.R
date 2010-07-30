createBonferroniHolmGraph <- function(n, alpha=0.05) {
	alpha <- rep(alpha/n, n)
	hnodes <- paste("H", 1:n, sep="")
	edges <- vector("list", length=n)
	for(i in 1:n) {
		edges[[i]] <- list(edges=hnodes[(1:n)[-i]], weights=rep(1/(n-1),n-1))
	}
	names(edges)<-hnodes
	BonferroniHolmGraph <- new("graphSRMTP", nodes=hnodes, edgeL=edges, alpha=alpha)
	return(BonferroniHolmGraph)
}

createGraphFromBretzEtAl <- function() {
	alpha=0.05
	alpha <- rep(c(alpha/3,0), each=3)
	hnodes <- paste("H", rep(1:3, 2), rep(1:2, each=3), sep="")
	edges <- vector("list", length=6)
	edges[[1]] <- list(edges=c("H21","H12"), weights=rep(1/2, 2))
	edges[[2]] <- list(edges=c("H11","H31","H22"), weights=rep(1/3, 3))
	edges[[3]] <- list(edges=c("H21","H32"), weights=rep(1/2, 2))
	edges[[4]] <- list(edges="H21", weights=1)
	edges[[5]] <- list(edges=c("H11","H31"), weights=rep(1/2, 2))
	edges[[6]] <- list(edges="H21", weights=1)
	names(edges)<-hnodes
	graph <- new("graphSRMTP", nodes=hnodes, edgeL=edges, alpha=alpha)
	# Layout see Rgraphviz:::layoutGraphviz
	nodeX <- rep(c(10,20,30),2)
	nodeY <- rep(c(10,20),each=3)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)	
	return(graph)
}