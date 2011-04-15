graphAnalysis <- function(graph, file="") {
	result <- ""
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
	cat(result, file=file)
	return(invisible(result))
}