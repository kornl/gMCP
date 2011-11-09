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
	checkEquals(result1$rejected, c(TRUE, TRUE, FALSE, FALSE))
}