graphAnalysis <- function(graph, file="") {
	result <- ""
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
	cat(result, file=file)
	return(invisible(result))
}

accessible <- function(graph) {
	m <- graph@m
	m <- ifelse(!is.na(as.num(m)) && as.num(m) != 0, 0, 1)
	for (i in getNodes(graph)) {
		
	}
}

as.num <- function(x) {
	options(warn=-1)
	result <- as.numeric(x)
	options(warn=0)
	return(result)
}