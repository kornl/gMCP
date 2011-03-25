matrix2graph <- function(m, weights=rep(1/dim(m)[1],dim(m)[1])) {
	# Checking for 0 on diagonal:
	if (!all(TRUE == all.equal(unname(diag(m)), rep(0, length(diag(m)))))) {
		warning("Matrix has a diagonal not equal to zero. Loops are not allowed.")
		diag(m) <- rep(0, length(diag(m)))
	}
	# Creating graph without edges:
	if (dim(m)[1]!=dim(m)[2]) stop("Matrix has to be quadratic.")
	hnodes <- rownames(m)
	if (is.null(hnodes)) hnodes <- colnames(m)
	if (is.null(hnodes)) hnodes <- paste("H",1:(dim(m)[1]),sep="")
	edges <- vector("list", length=length(hnodes))
	names(edges)<-hnodes
	for (i in 1:length(hnodes)) edges[[i]] <- list()
	graph <- new("graphMCP", nodes=hnodes, edgeL=edges, weights=weights)
	# Creating edges:
	for (i in 1:length(hnodes)) {
		for (j in 1:length(hnodes)) {
			if (m[i,j]!=0) {
				graph <- addEdge(hnodes[i], hnodes[j], graph, m[i,j])
			}
		}
	}
	return(graph)
}

graph2matrix <- function(graph) {
	hnodes <- nodes(graph)
	m <- matrix(nrow=length(hnodes),ncol=length(hnodes))
	colnames(m) <- hnodes
	rownames(m) <- hnodes
	for (i in 1:length(hnodes)) {
		for (j in 1:length(hnodes)) {			
			m[i,j] <- getWeight(graph, hnodes[i], hnodes[j])
		}
	}
	return(m)
}