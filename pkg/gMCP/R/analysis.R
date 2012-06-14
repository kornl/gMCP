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

accessible <- function(graph, node) {
	m <- graph@m
	#m <- ifelse(!is.na(as.num(m)) && as.num(m) != 0, 0, 1)
	s <- which(getNodes(graph)==node)
	n <- dim(m)[1]
	ac <- rep(FALSE, n)
	followed <- rep(FALSE, n)
	for (j in 1:n) {
		for (i in 1:n) {
			if (followed[i]==FALSE && (ac[i]==TRUE || i == s)) {
				ac[m[i,]!=0] <- TRUE
				followed[i] <- TRUE
			}
		}
	}
	return(ac)
}

checkOptimal <- function (graph) {
	nodes <- getNodes(graph)[getWeights(graph)!=0]
	s <- ""
	for (n in nodes) {
		notAccessible <- setdiff(getNodes(graph)[!accessible(graph, n)], n)
		if (length(notAccessible)>0) {
			if (s=="") {
				s <- "The graph is not optimal.\nBy adding edges the test can be improved uniformly."
				s <- paste(s, "Or set exhaustAlpha=TRUE to do an alpha exhaustive test as described in Bretz et al. (2011).", sep="\n")
			}
			s <- paste(s, paste("There is no path from node ",n, " to ", paste(notAccessible, collapse=", "), sep=""),"\n", sep="\n")
		}
	}
	cat(s)
	return(s)
}

as.num <- function(x) {
	options(warn=-1)
	result <- as.numeric(x)
	options(warn=0)
	return(result)
}