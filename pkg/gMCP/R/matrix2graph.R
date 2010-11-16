matrix2graph <- function(m, alpha=rep(0,dim(m)[1])) {
	# Creating graph without edges:
	if (dim(m)[1]!=dim(m)[2]) stop("Matrix has to be quadratic.")
	hnodes <- rownames(m)
	if (is.null(hnodes)) hnodes <- colnames(m)
	if (is.null(hnodes)) hnodes <- paste("H",1:(dim(m)[1]),sep="")
	edges <- vector("list", length=length(hnodes))
	names(edges)<-hnodes
	for (i in 1:length(hnodes)) edges[[i]] <- list()
	graph <- new("graphMCP", nodes=hnodes, edgeL=edges, alpha=alpha)
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
	
}