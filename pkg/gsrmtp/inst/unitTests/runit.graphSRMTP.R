test.graphSRMTP <- function() {
	
	# Setup from http://cran.r-project.org/web/packages/graph/index.html under Artistic License 2.0	
	V <- LETTERS[1:4]
	edL <- vector("list", length=4)
	names(edL) <- V
	weights=runif(4)
	for(i in 1:4)
		edL[[i]] <- list(edges=5-i, weights=weights[i])
	gR <- new("graphSRMTP", nodes=V, edgeL=edL, alpha=c(0.1, 0.5, 0.4))
	
	# Tests
	checkEquals(unlist(edges(gR)), 
			structure(c("D", "C", "B", "A"), .Names = c("A", "B", "C", "D")))
	checkEquals(edgeWeights(gR)$A, structure(weights[1], .Names = "D"))	
	checkException(new("graphSRMTP", nodes=V, edgeL=edL, alpha=c(-1,1,1)))
	checkException(new("graphSRMTP", nodes=V, edgeL=edL, alpha=c(0,0,0.5)))
	
}


