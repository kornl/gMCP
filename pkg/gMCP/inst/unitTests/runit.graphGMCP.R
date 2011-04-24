test.graphMCP <- function() {
	
	hypotheses <- LETTERS[1:4]
	edL <- vector("list", length=4)
	names(edL) <- hypotheses
	weights=runif(4)
	for(i in 1:4) {		
		edL[[i]] <- list(edges=5-i, weights=weights[i])
	}
	gR <- new("graphMCP", nodes=hypotheses, edgeL=edL, weights=c(0.1, 0.1, 0.1, 0))
	
	# Tests
	checkEquals(unlist(edges(gR)), 
			structure(c("D", "C", "B", "A"), .Names = c("A", "B", "C", "D")))
	checkEquals(edgeWeights(gR)$A, structure(weights[1], .Names = "D"))	
	checkException(new("graphMCP", nodes=hypotheses, edgeL=edL, alpha=c(0.5,0.5,0.1)))
	checkException(new("graphMCP", nodes=hypotheses, edgeL=edL, alpha=c(-1,0.5,0.5,0.1)))
	checkException(new("graphMCP", nodes=hypotheses, edgeL=edL, alpha=c(-1,1,1)))
	checkException(new("graphMCP", nodes=hypotheses, edgeL=edL, alpha=c(0.5,0.5,0.5)))
	
	bhG5 <- BonferroniHolmGraph(5)
	
	checkEquals(gMCP:::getWeights(bhG5),
				structure(c(0.2, 0.2, 0.2, 0.2, 0.2),
						.Names = c("H1", "H2", "H3", "H4", "H5")))
	
	checkEquals(edgeWeights(bhG5)[["H1"]],
			structure(c(0.25, 0.25, 0.25, 0.25),
					.Names = c("H2", "H3", "H4", "H5")))
	
}

test.bonferroniHolm <- function() {	
	bhG3 <- BonferroniHolmGraph(3)
	checkEquals(edges(bhG3), structure(list(H1 = c("H2", "H3"), 
							               H2 = c("H1", "H3"), 
										   H3 = c("H1", "H2")), 
								   .Names = c("H1", "H2", "H3")))
				   
	result <- gMCP(bhG3, pvalues=c(0.1, 0.3, 0.7), alpha=0.6)
	checkEquals(gMCP:::getRejected(result@graphs[[3]]),
			structure(c(TRUE, TRUE, FALSE), .Names = c("H1", "H2", "H3")))
}

test.gMCP <- function() {
	bhG3 <- BonferroniHolmGraph(3)
	pvalues <- c(0.1, 0.2, 0.3)
	names(pvalues) <- nodes(bhG3)
	checkTrue(gMCP:::canBeRejected(bhG3, "H1", alpha=0.6, pvalues)) 
	checkTrue(gMCP:::canBeRejected(bhG3, "H2", alpha=0.6, pvalues)) 
	checkTrue(!gMCP:::canBeRejected(bhG3, "H3", alpha=0.6, pvalues)) 
	checkException(gMCP(bhG3, 0, alpha=0.6))
	checkException(gMCP(bhG3, rep(0,6), alpha=0.6))
	graph <- matrix2graph(matrix(c(0,0,0,0), nrow=2))
	checkEquals(unname(getRejected(gMCP(graph, pvalues=c(0.0, 0.0)))),
			c(TRUE, TRUE))
}

test.adjPValues <- function() {
	adjPValues <- gMCP:::adjPValues(BonferroniHolmGraph(3), c(0.02,0.055,0.012))@adjPValues
	checkEquals(adjPValues, 
			structure(c(0.04, 0.055, 0.036), .Names = c("H1", "H2", "H3")))
}

test.gMCPBretzEtAl <- function() {
	graph <- graphFromBretzEtAl2009()
	pvalues <- c(0.1, 0.008, 0.005, 0.15, 0.04, 0.006)
	result <- gMCP(graph, pvalues)
	last <- result@graphs[[4]]	
	checkEquals(unname(unlist(nodeData(last, nodes(last), "rejected"))),
			c(FALSE, TRUE, TRUE, FALSE, FALSE, TRUE))
	checkEquals(edges(last), structure(list(H11 = c("H12", "H22"), 
							H21 = character(0), 
							H31 = character(0), 
							H12 = c("H11", "H22"), 
							H22 = "H11", 
							H32 = character(0)), 
					.Names = c("H11", "H21", "H31", "H12", "H22", "H32")))
	checkEquals(edgeWeights(last), structure(list(
							H11 = structure(c(0.666666666666667, 0.333333333333333), .Names = c("H12", "H22")), 
							H21 = numeric(0), 
							H31 = numeric(0), 
							H12 = structure(c(0.5, 0.5), .Names = c("H11", "H22")), 
							H22 = structure(1, .Names = "H11"), 
							H32 = numeric(0)), 
					.Names = c("H11", "H21", "H31", "H12", "H22", "H32")))
}

test.gMCPImprovedParallelGatekeeping <- function() {
	graph <- graphForImprovedParallelGatekeeping()
	graph <- rejectNode(graph, "H1")
	checkEquals(edgeWeights(graph), structure(list(
							H1 = numeric(0),
							H2 = structure(c(0.5, 0.5), .Names = c("H3", "H4")),
							H3 = structure(1, .Names = "H4"),
							H4 = structure(c(1,	0), .Names = c("H3", "H2"))),
					.Names = c("H1", "H2", "H3", "H4")))
	graph <- rejectNode(graph, "H4")
	checkEquals(edgeWeights(graph), structure(list(
							H1 = numeric(0),
							H2 = structure(1, .Names = "H3"), 
							H3 = structure(0, .Names = "H2"),
							H4 = numeric(0)),
					.Names = c("H1", "H2", "H3", "H4")))
}

test.only.no.error <- function() {
	graph <- BonferroniHolmGraph(3)
	pvalues <- c(0.1, 0.2, 0.3)
	graph2latex(graph)
	gMCPReport(graph)
	gMCPReport(gMCP(graph, pvalues))
}

test.graph2matrix <- function() {
	m <- matrix(runif(9)/3, nrow=3)
	diag(m) <- c(0, 0, 0)
	rownames(m) <- LETTERS[1:3]
	colnames(m) <- LETTERS[1:3]
	m2 <- graph2matrix(matrix2graph(m))
	checkEquals(m, m2)
}