
test.gMCP <- function() {
	Gm <- matrix(0,nr=4,nc=4) 
	Gm[1,3] <- 1 
	Gm[2,4] <- 1 
	Gm[3,2] <- 1 
	Gm[4,1] <- 1 
	w <- c(1/2,1/2,0,0) 
	G <- matrix2graph(Gm,w) 
	Cm <- matrix(NA,nr=4,nc=4) 
	diag(Cm) <- 1 
	Cm[1,2] <- 1/2 
	Cm[2,1] <- 1/2 
	Cm[3,4] <- 1/2 
	Cm[4,3] <- 1/2 
	p <- c(0.0131,0.1,0.012,0.01) 
	x <- unname(gMCP(G,p,corr=Cm,alpha=0.025)@rejected)
	#checkEquals(c(TRUE, FALSE, TRUE, FALSE), x)
}