conv <- function(a, b)
	.C("convolve",
			as.double(a),
			as.integer(length(a)),
			as.double(b),
			as.integer(length(b)),
			ab = double(length(a) + length(b) - 1),
			PACKAGE="gMCP")$ab
