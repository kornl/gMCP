test.calcpower <- function() {
	if (Sys.getenv("GMC_UNIT_TESTS")=="extended") {
		# Here we can write down extended tests that will only be used if
		# the environment variable GMC_UNIT_TESTS is set to "extended".
	}
}

test.rqmvnorm <- function() {
	# Check whether the correlation is correctly processed
	# (especially in the right order)
	R <- kronecker(matrix(.3,2,2)+diag(.7,2), matrix(1/2,3,3)+diag(1/2,3))
	checkTrue(all(round(cov(rqmvnorm(10000,mean=1:6,sigma=R)),2)==R))
}