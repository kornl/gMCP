test.calcpower <- function() {
	if (gMCP:::tests("extended")) {
		# Here we can write down extended tests that will only be used if
		# the environment variable GMC_UNIT_TESTS is set to "extended".
	}
	.tmpGraph <- BonferroniHolm(3)
	gMCP:::calcMultiPower(weights=.tmpGraph@weights, alpha=0.05, G=.tmpGraph@m, muL = list(c(0, 0, 0)), sigmaL = list(c(1, 1, 1)), nL = list(c(10, 10, 10)),sigma = matrix(c(1,0,0,0,1,0,0,0,1), nrow=3), nSim = 10000, type = "quasirandom")
}

test.rqmvnorm <- function() {
	if (gMCP:::tests("extended")) {		
		# Check whether the correlation is correctly processed
		# (especially in the right order)
		R <- kronecker(matrix(.3,2,2)+diag(.7,2), matrix(1/2,3,3)+diag(1/2,3))
		checkTrue(all(round(cov(rqmvnorm(1000000, mean=1:6, sigma=R)),2)==R))
		
		# Singular matrix (min eigen value -1.562001e-16 on my system):
		x <- multcomp::contrMat(rep(10,5), type="UmbrellaWilliams")
		R <- t(x) %*% x
		checkTrue(all(round(cov(rqmvnorm(1000000, mean=1:5, sigma=R)),2)==round(R, 2)))
	} else {
		cat("Skipping checks for rqmvnorm.\n")
	}
}