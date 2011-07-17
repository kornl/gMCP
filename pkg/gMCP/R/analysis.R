graphAnalysis <- function(graph, file="") {
	result <- ""
	if (require("graph")) { 
		accessible <- acc(graph, nodes(graph))
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
		result <- paste("Install package \"graph\" for graph analysis:",
				"source(\"http://www.bioconductor.org/biocLite.R\")",
				"biocLite(\"graph\")", sep="\n");
	}
	cat(result, file=file)
	return(invisible(result))
}

acc <- function(graph) {
	m <- graph@m
	m <- ifelse(!is.na(as.num(m)) && as.num(m) != 0, 0, 1)
	for (i in nodes(graph)) {
		
	}
}

as.num <- function(x) {
	options(warn=-1)
	result <- as.numeric(x)
	options(warn=0)
	return(result)
}