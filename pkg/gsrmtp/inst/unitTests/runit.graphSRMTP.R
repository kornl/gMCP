test.graphSRMTP <- function() {
	
	hypotheses <- LETTERS[1:4]
	edL <- vector("list", length=4)
	names(edL) <- hypotheses
	weights=runif(4)
	for(i in 1:4) {		
		edL[[i]] <- list(edges=5-i, weights=weights[i])
	}
	gR <- new("graphSRMTP", nodes=hypotheses, edgeL=edL, alpha=c(0.1, 0.1, 0.1, 0))
	
	# Tests
	checkEquals(unlist(edges(gR)), 
			structure(c("D", "C", "B", "A"), .Names = c("A", "B", "C", "D")))
	checkEquals(edgeWeights(gR)$A, structure(weights[1], .Names = "D"))	
	checkException(new("graphSRMTP", nodes=hypotheses, edgeL=edL, alpha=c(0.5,0.5,0.1)))
	checkException(new("graphSRMTP", nodes=hypotheses, edgeL=edL, alpha=c(-1,0.5,0.5,0.1)))
	checkException(new("graphSRMTP", nodes=hypotheses, edgeL=edL, alpha=c(-1,1,1)))
	checkException(new("graphSRMTP", nodes=hypotheses, edgeL=edL, alpha=c(0.5,0.5,0.5)))
	
	bhG5 <- createBonferroniHolmGraph(5)
	
	checkEquals(gsrmtp:::getAlpha(bhG5),
				structure(c(0.01, 0.01, 0.01, 0.01, 0.01),
						.Names = c("H1", "H2", "H3", "H4", "H5")))
	
	checkEquals(edgeWeights(bhG5)[["H1"]],
			structure(c(0.25, 0.25, 0.25, 0.25),
					.Names = c("H2", "H3", "H4", "H5")))
	
}

test.bonferroniHolm <- function() {
	
	bhG3 <- createBonferroniHolmGraph(3, alpha=0.6)
	checkEquals(edges(bhG3), structure(list(H1 = c("H2", "H3"), 
							               H2 = c("H1", "H3"), 
										   H3 = c("H1", "H2")), 
								   .Names = c("H1", "H2", "H3")))
				   
	result <- srmtp(bhG3, pvalues=c(0.1, 0.3, 0.7))
		
}

test.srmtp <- function() {
	bhG3 <- createBonferroniHolmGraph(3, alpha=0.6)
	pvalues=c(0.1, 0.2, 0.3)
	names(pvalues) <- nodes(bhG3)
	checkTrue(gsrmtp:::canBeRejected(bhG3, "H1", pvalues)) 
	checkTrue(gsrmtp:::canBeRejected(bhG3, "H2", pvalues)) 
	checkTrue(!gsrmtp:::canBeRejected(bhG3, "H3", pvalues)) 
	checkException(srmtp(bhG3, 0))
	checkException(srmtp(bhG3, rep(0,6)))
}


