test.Simes <- function() {	
	m <- matrix(0,nr=4,nc=4)
	m[1,3] <- m[2,4] <- m[3,2] <- m[4,1] <- 0
	w <- c(1/2, 1/2, 0, 0)
	p1 <- c(0.01, 0.005, 0.01, 0.5)
	p2 <- c(0.01, 0.005, 0.015, 0.022)
	a <- 0.05
	g <- matrix2graph(m, w)
	result1 <- gMCP(g, pvalues=p1, test="Simes", alpha=a)
	result2 <- gMCP(g, pvalues=p2, test="Simes", alpha=a)
	checkEquals(unname(result1@rejected), c(TRUE, TRUE, FALSE, FALSE))
	checkEquals(unname(result2@rejected), c(TRUE, TRUE, TRUE, TRUE))
}

checkWeights <- function(graph, pvalues) {
	# Compares the weights of the gMCP-R-code, gMCP-C-code, power-C-code and parametric-R-code
	result <- gMCP(graph,  pvalues)
	rejected <- getRejected(result)
	weights <- getWeights(result)
	
	result2 <- gMCP(graph,  pvalues, useC=TRUE)
	rejected2 <- getRejected(result2)
	weights2 <- getWeights(result2)
	
	checkEquals(rejected, rejected2)
	checkEquals(weights, weights2)
}

test.checkWeights <- function() {
	graphs <- list(BonferroniHolm(5),
			parallelGatekeeping(),
			improvedParallelGatekeeping(),
			BretzEtAl2011(),
			#HungEtWang2010(),
			#HuqueAloshEtBhore2011(),
			HommelEtAl2007(),
			HommelEtAl2007Simple(),
			MaurerEtAl1995(),
			improvedFallbackI(weights=rep(1/3, 3)),
			improvedFallbackII(weights=rep(1/3, 3)),
			cycleGraph(nodes=paste("H",1:4,sep=""), weights=rep(1/4, 4)),
			fixedSequence(5),
			fallback(weights=rep(1/4, 4)),
			#generalSuccessive(weights = c(1/2, 1/2)),
			simpleSuccessiveI(),
			simpleSuccessiveII(),
			#truncatedHolm(),
			BauerEtAl2001(),
			BretzEtAl2009a(),
			BretzEtAl2009b(),
			BretzEtAl2009c())
	for (graph in graphs) {		
		p <- gMCP:::permutations(length(getNodes(graph)))
		for (i in 1:(dim(p)[1])) {
			checkWeights(graph, p[i,])
		}
	}
}