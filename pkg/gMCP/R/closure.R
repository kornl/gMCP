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
	m2 <- m
	w2 <- w
	.Call("cgMCP", m2, w2, p, a)
	return(list(m2,w2))
}
