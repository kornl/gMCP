test.graphSRMTP <- function() {
	
	# Setup from http://cran.r-project.org/web/packages/graph/index.html under Artistic License 2.0	
	V <- LETTERS[1:4]
	edL <- vector("list", length=4)
	names(edL) <- V
	weights=runif(4)
	for(i in 1:4) {		
		edL[[i]] <- list(edges=5-i, weights=weights[i])
	}
	gR <- new("graphSRMTP", nodes=V, edgeL=edL, alpha=c(0.1, 0.1, 0.1))
	
	# Tests
	checkEquals(unlist(edges(gR)), 
			structure(c("D", "C", "B", "A"), .Names = c("A", "B", "C", "D")))
	checkEquals(edgeWeights(gR)$A, structure(weights[1], .Names = "D"))	
	checkException(new("graphSRMTP", nodes=V, edgeL=edL, alpha=c(-1,1,1)))
	checkException(new("graphSRMTP", nodes=V, edgeL=edL, alpha=c(0.5,0.5,0.5)))
	
	bhG5 <- createBonferroniHolmGraph(5)
	
	checkEquals(getAlpha(bhG5),
			structure(list(H1 = 0.01, H2 = 0.01, H3 = 0.01, H4 = 0.01, H5 = 0.01),
					.Names = c("H1", "H2", "H3", "H4", "H5")))
	
}

test.bonferroniHolm <- function() {
	
	bhG3 <- createBonferroniHolmGraph(3, alpha=0.6)
	checkEquals(edges(bhG3), structure(list(H1 = c("H2", "H3"), 
							               H2 = c("H1", "H3"), 
										   H3 = c("H1", "H2")), 
								   .Names = c("H1", "H2", "H3")))
				   
	result <- srmtp(bhG3, pvalues=c(0.1, 0.3, 0.7))
		
}


