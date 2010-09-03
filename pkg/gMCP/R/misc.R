checkValidAlpha <- function(alpha) {
	if(any(0 > alpha | alpha > 1)) {
		stop("invalid alpha: alphas must be between 0 and 1")
	}
	if(sum(alpha) >= 1) {
		stop("invalid alpha: the sum of all alphas must be less than 1")
	}
}

