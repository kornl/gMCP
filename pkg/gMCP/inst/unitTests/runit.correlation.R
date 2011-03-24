
test.gMCP <- function() {
	bhG3 <- createBonferroniHolmGraph(3)
	pvalues <- c(0.1, 0.2, 0.3)
	names(pvalues) <- nodes(bhG3)
	C <- matrix(0.5, nrow=3, ncol=3)
	diag(C) <- 1
	x <- gMCP(bhG3,pvalues,corr=C,alpha=.75)
	y <- gMCP(bhG3,pvalues,corr="Dunnett",alpha=.75)
	checkEquals(x,y)
	checkException(gMCP(bhG3, 0, corr=C, alpha=0.6))
	checkException(gMCP(bhG3, pvalues, corr=C, alpha=1.2))
}