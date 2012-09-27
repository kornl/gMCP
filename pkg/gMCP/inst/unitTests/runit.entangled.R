
test.graphTest <- function() {
	G1 <- rbind(c(0,0.5,0.5,0,0), c(0,0,1,0,0),
			c(0, 0, 0, 1-0.01, 0.01), c(0, 1, 0, 0, 0),
			c(0, 0, 0, 0, 0))
	G2 <- rbind(c(0,0,1,0,0), c(0.5,0,0.5,0,0),
			c(0, 0, 0, 0.01, 1-0.01), c(0, 0, 0, 0, 0),
			c(1, 0, 0, 0, 0))
	weights <- rbind(c(1, 0, 0, 0, 0), c(0, 1, 0, 0, 0))
	pvals <- c(0.024, 0.05, 0.01, 0.003, 0.009)
	out <- graphTest(pvals, weights, alpha=0.05, G=list(G1, G2), subgraphWeights=c(1/2,1/2), verbose = TRUE)
	
}