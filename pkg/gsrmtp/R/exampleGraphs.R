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