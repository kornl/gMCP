conv <- function(a, b)
	.C("convolve",
			as.double(a),
			as.integer(length(a)),
			as.double(b),
			as.integer(length(b)),
			ab = double(length(a) + length(b) - 1),
			PACKAGE="gMCP")$ab

fastgMCP <- function(m, w, p, a) {
	if (length(a)>1) {
		warning("Only the first value of 'a' is used!")
	}
	n <- dim(m)[1]
	if (dim(m)[2]!=n || length(w)!=n || length(p)!=n) {
		stop("Wrong dimensions in fastgMCP call!")
	}	
	result <- .C("cgMCP", as.double(m), as.double(w), as.double(p), as.double(a), as.integer(n), 
			newM = double(n*n), newW = double(n) )
	return(list(m=matrix(result$newM, nrow=n), w=result$newW))
}
