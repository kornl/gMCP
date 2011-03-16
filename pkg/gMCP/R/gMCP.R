gMCP <- function(graph, pvalues, test="Bonferroni", ..., verbose=FALSE) {
	if (!test %in% c("Bonferroni", "correlated")) {
		stop("Parameter \"test\" must be one of the following: \"Bonferroni\", \"correlated\".")
	}
	if (length(pvalues)!=length(nodes(graph))) {
		stop("Length of pvalues must equal number of nodes.")
	}
	if (is.null(names(pvalues))) {
		names(pvalues) <- nodes(graph)
	}
	if (test=="Bonferroni") {
		sequence <- list(graph)
		while(!is.null(node <- getRejectableNode(graph, pvalues))) {
			if (verbose) cat(paste("Node \"",node,"\" can be rejected.\n",sep=""))
			graph <- rejectNode(graph, node, verbose)
			sequence <- c(sequence, graph)
		}	
		return(new("gMCPResult", graphs=sequence, pvalues=pvalues, adjPValues=adjPValues(sequence[[1]], pvalues, verbose)@adjPValues))
	} else if (test=="correlated") {
		if (is.missing(correlation) || !is.matrix(correlation)) {
			stop("Procedure for correlated tests, expects a correlation matrix as parameter \"correlation\".")
			Gm <- graph2matrix(createdGraph)
			w <- graph2matrix(createdGraph)
			myTest <- generateTest(Gm, w, correlation, sum(getAlpha(graph)))
			return(myTest(pvalues))
		}
	}
}

adjPValues <- function(graph, pvalues, verbose=FALSE) {
	if (length(pvalues)!=length(nodes(graph))) {
		stop("Length of pvalues must equal number of nodes.")
	}
	if (is.null(names(pvalues))) {
		names(pvalues) <- nodes(graph)
	}
	nodeData(graph, nodes(graph), "alpha") <- getAlpha(graph)/sum(getAlpha(graph))
	adjPValues <- rep(0, length(nodes(graph)))
	names(adjPValues) <- nodes(graph)	
	J <- nodes(graph)
	names(J) <- nodes(graph)
	sequence <- list(graph)
	pmax <- 0
	while(length(J) >= 1) {
		j <- which.min(pvalues[J]/getAlpha(graph)[J])
		node <- J[j]
		adjPValues[node] <- max(min(pvalues[node]/getAlpha(graph)[node], 1), pmax)
		pmax <- adjPValues[node]
		if (verbose) cat(paste("We will update the graph with node \"",node,"\".\n",sep=""))
		graph <- rejectNode(graph, node, verbose)
		J <- J[J!=node]
		sequence <- c(sequence, graph)
	}	
	return(new("gMCPResult", graphs=sequence, pvalues=pvalues, adjPValues=adjPValues))
}

rejectNode <- function(graph, node, verbose=FALSE) {
	edgesIn <- c()			
	for (node2 in nodes(graph)) {
		i <- which(names(edgeWeights(graph, node2)[[node2]])==node)
		if (length(i)>0) {
			edge <- edgeWeights(graph, node2)[[1]][i]
			names(edge) <- node2
			edgesIn <- c(edgesIn, edge)
		}
	}
	
	edgesOut <- edgeWeights(graph, node)[[node]]
	if (verbose) cat(paste("There are ",length(edgesIn)," incoming and ",length(edgesOut)," outgoing edges.\n",sep=""))
	
	keepAlpha <- TRUE
	
	graph2 <- graph
	
	#for (to in nodes(graph)[nodes(graph)!=node]) {
	#	if ((getWeight(graph,node,to))>0) {
	#		keepAlpha <- FALSE
	#		nodeData(graph2, to, "alpha") <- nodeData(graph, to, "alpha")[[to]] + getWeight(graph,node,to) * nodeData(graph, node, "alpha")[[node]]
	#	}
	#}	
	
	## The following code will be removed in 0.4! ##
	if (all(TRUE == all.equal(unname(edgesOut), rep(0, length(edgesOut))))) {
		if (verbose) cat("Alpha is passed via epsilon-edges.\n")
		for (to in nodes(graph)[nodes(graph)!=node]) {	
			numberOfEpsilonEdges <- sum(TRUE == all.equal(unname(edgesOut), rep(0, length(edgesOut))))
			if (existsEdge(graph, node, to)) {
				nodeData(graph2, to, "alpha") <- nodeData(graph, to, "alpha")[[to]] + nodeData(graph, node, "alpha")[[node]] / numberOfEpsilonEdges
				keepAlpha <- FALSE
			}
		}		
	} else {
		if (verbose) cat("Alpha is passed via non-epsilon-edges.\n")
		for (to in nodes(graph)[nodes(graph)!=node]) {				
			nodeData(graph2, to, "alpha") <- nodeData(graph, to, "alpha")[[to]] + getWeight(graph,node,to) * nodeData(graph, node, "alpha")[[node]]				
		}	
		keepAlpha <- FALSE
	}
	
	#################################################
	
	for (to in nodes(graph)[nodes(graph)!=node]) {						
		for (from in nodes(graph)[nodes(graph)!=node]) {
			if (from != to) {
				enum <- (getWeight(graph,from,to)+getWeight(graph,from,node)*getWeight(graph,node,to))
				denum <- (1-getWeight(graph,from,node)*getWeight(graph,node,from)) 
				w <- enum / ifelse(denum==0, 1, denum)						
				if (to %in% edges(graph)[[from]]) {
					edgeData(graph2,from,to,"weight") <- w
				} else {
					if (!is.nan(w) & w>0) {
						graph2 <- addEdge(from, to, graph2, w)
					} else if (existsEdge(graph,from,to) || (existsEdge(graph,from,node) && existsEdge(graph,node,to))) {
						graph2 <- addEdge(from, to, graph2, 0)
					}
				}								
			}
		}								
	}
	
	graph <- graph2
	
	if (verbose) cat("Removing edges.\n")
	for (to in names(edgesOut)) {
		graph <- removeEdge(node, to, graph)
	}
	for (from in names(edgesIn)) {
		graph <- removeEdge(from, node, graph)
	}
	if (!keepAlpha) {
		nodeData(graph, node, "alpha") <- 0
	}
	nodeData(graph, node, "rejected") <- TRUE	
	return(graph)
}

getRejectableNode <- function(graph, pvalues) {
	x <- getAlpha(graph)/pvalues
	x[pvalues==0] <- 1
	x[unlist(nodeData(graph, nodes(graph), "rejected"))] <- NaN
	i <- which.max(x)
	if (length(i)==0) return(NULL)
	if (x[i]>1 | all.equal(unname(x[i]),1)[1]==TRUE) {return(nodes(graph)[i])}
	return(NULL)	 
}

existsEdge <- function(graph, from, to) {
	weight <- try(edgeData(graph,from,to,"weight"), silent = TRUE)
	if (class(weight)=="try-error") {
		return(FALSE)
	}
	return(TRUE)
} 

getWeight <- function(graph, from, to) {
	weight <- try(edgeData(graph,from,to,"weight"), silent = TRUE)
	if (class(weight)=="try-error") {
		return(0)
	}
	return(weight[[1]])
} 
