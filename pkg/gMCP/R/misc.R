checkValidAlpha <- function(alpha) {
	if(any(0 > alpha | alpha > 1)) {
		stop("invalid alpha: alphas must be between 0 and 1")
	}
	if(sum(alpha) >= 1) {
		stop("invalid alpha: the sum of all alphas must be less than 1")
	}
}

# Converts a string like "5+3e+5*e^2" to the tupel representation c(5,3,5) 
parseEpsPolynom <- function(s) {
	
}