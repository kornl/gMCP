getStr <- function(x) {
	frt <- rep(1:100, times=100)/rep(1:100, each=100)
	w <- which(TRUE==all.equal(rep(x,10000), frt))
	if (length(w)==0) return(as.character(x))
	return(paste(w[1]%%100,w[1]%/%100,sep="/"))
}


options(warn=-1)
if (require("graph", quietly=TRUE)) {
	options(warn=0)
	hnodes <- getNodes(graph)
	graph <- as(new("graphAM", adjMat=graph@m, edgemode="directed"), "graphNEL")
	accessible <- acc(graph, hnodes)
	for (i in names(accessible)) {
		missingNodes <- c()
		for (n in nodes(graph)) {
			if (!(n %in% names(accessible[[i]])) && n!=i) {
				missingNodes <- c(missingNodes, n)
			}			
		}
		if (length(missingNodes)!=0) {
			result <- paste(result, "The following nodes are not accessible from '",i,"': ",
					paste(missingNodes, collapse =", "), "\n", sep="")
		}
	}
	if (result=="") {
		result <- "Each node is accessible from each other node.\n"
	} else {
		
	}
} else {
	options(warn=0)
	result <- paste("Install package \"graph\" for graph analysis:",
			"",
			"source(\"http://www.bioconductor.org/biocLite.R\")",
			"biocLite(\"graph\")",
			"",
			"and restart R.", sep="\n");
}