checkAdjPValues <- function(graph, pvalues) {	
	adjP <- gMCP:::adjPValues(graph, pvalues)@adjPValues
	for (p in adjP) {
		alpha <- getAlpha(graph)
		graph2 <- setAlpha(graph, alpha=alpha/sum(alpha)*p)
		result <- gMCP(graph2, pvalues)
		checkEquals(adjP<=p,getRejected(result)) 
	}	
}

test.adjPValues <- function() {
	# Bonferroni-Holm
	graph <- createBonferroniHolmGraph(3)
	pvalues <- c(0.01, 0.02, 0.05)
	checkAdjPValues(graph, pvalues)
	# Improved Parallel Gatekeeping
	graph <- createGraphForImprovedParallelGatekeeping()
	pvalues <- c(0.01, 0.01, 0.03, 0.04)
	checkAdjPValues(graph, pvalues)
	# Parallel Gatekeeping
	graph <- createGraphForParallelGatekeeping()
	pvalues <- c(0.01, 0.01, 0.03, 0.04)
	checkAdjPValues(graph, pvalues)
}