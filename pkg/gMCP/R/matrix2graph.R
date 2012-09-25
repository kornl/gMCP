matrix2graph <- function(m, weights=rep(1/dim(m)[1],dim(m)[1])) {
	# Checking for 0 on diagonal:
	if (!(all(TRUE == all.equal(unname(diag(m)), rep(0, length(diag(m)))))
	   || all(TRUE == all.equal(unname(diag(m)), rep("0", length(diag(m))))))) {
		warning("Matrix has a diagonal not equal to zero. Loops are not allowed.")
		diag(m) <- rep(0, length(diag(m)))
	}
	# Creating graph without edges:
	if (dim(m)[1]!=dim(m)[2]) stop("Matrix has to be quadratic.")
	hnodes <- rownames(m)
	if (is.null(hnodes)) hnodes <- colnames(m)
	if (is.null(hnodes)) hnodes <- paste("H",1:(dim(m)[1]),sep="")
	rownames(m) <- colnames(m) <- hnodes
	graph <- new("graphMCP", m=m, weights=weights)
	return(graph)
}

graph2matrix <- function(graph) {
	if (class(graph) %in% "graphMCP") {
		return(graph@m)
	} else if (class(graph) %in% "entangledMCP"){
		# TODO What do we want to return in this case?
		return(graph@graphs[[1]]@m)
	} else {
		stop("This function should only be used for objects of class graphMCP or entangledMCP.")
	}
}