gMCP <- function(graph, pvalues, test, correlation, alpha=0.05, 
		approxEps=TRUE, eps=10^(-4), ..., verbose=FALSE) {
	if (approxEps) {
		graph <- substituteEps(graph, eps=eps)
	}
	if (length(pvalues)!=length(nodes(graph))) {
		stop("Length of pvalues must equal number of nodes.")
	}
	if (is.null(names(pvalues))) {
		names(pvalues) <- nodes(graph)
	}
	if (missing(test) && (missing(correlation) || length(pvalues)==1)) {
		sequence <- list(graph)
		while(!is.null(node <- getRejectableNode(graph, alpha, pvalues))) {
			if (verbose) cat(paste("Node \"",node,"\" can be rejected.\n",sep=""))
			graph <- rejectNode(graph, node, verbose)
			sequence <- c(sequence, graph)
		}	
		return(new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph), adjPValues=adjPValues(sequence[[1]], pvalues, verbose)@adjPValues))
	} else if (missing(test) && !missing(correlation)) {
		if (missing(correlation) || (!is.matrix(correlation) && !is.character(correlation))) {
			stop("Procedure for correlated tests, expects a correlation matrix as parameter \"correlation\".")
		} else {
			if (is.character(correlation)) {
				samplesize <- list(...)[["samplesize"]]
				if (is.null(samplesize)) samplesize <- getBalancedDesign(correlation, length(pvalues))				
				x <- contrMat(samplesize, type = correlation) # balanced design up to now and only Dunnett will work with n+1
				var <- x %*% diag(length(samplesize)) %*% t(x)
				correlation <- diag(1/sqrt(diag(var)))%*%var%*%diag(1/sqrt(diag(var)))
			}                       
			Gm <- graph2matrix(graph)
			w <- getWeights(graph)
			if( all(w==0) ) {
				rejected <- rep(FALSE,length(w))
				names(rejected) <- nodes(graph)
			} else {
				myTest <- generateTest(Gm, w, correlation, alpha)
				zScores <- -qnorm(pvalues)
				rejected <- myTest(zScores)
				names(rejected) <- nodes(graph)
			}
			return(new("gMCPResult", graphs=list(graph), alpha=alpha, pvalues=pvalues, rejected=rejected, adjPValues=numeric(0)))
		}
	}
}

substituteEps <- function(graph, eps=10^(-4)) {
	from <- rep(names(edges(graph)), unlist(lapply(edges(graph),length)))	
	to <- unlist(edges(graph))
	if (length(from)==0) return(graph)
	for (i in 1:length(from)) {		
		p <- unlist(edgeData(graph, from[i], to[i], "epsilon"))
		if (!all(p==0)) {
			 text <- gsub("e", eps, getWeightStr(graph, from[i], to[i]))	
			 newWeight <- eval(parse(text=text))
			 edgeData(graph, from[i], to[i], "epsilon") <- 0
			 edgeData(graph, from[i], to[i], "weight") <- newWeight
		}		
	}
	return(graph)
}

# This function calculates the number of uncorrelated test statistics given the correlation structure and the number of p-values.
getBalancedDesign <- function (correlation, numberOfPValues) {
	if (correlation == "Dunnett") {
		return (rep(10, numberOfPValues+1))
	}
	stop(paste("The string \"",correlation,"\" does not specify a supported correlation.", sep=""))
}

adjPValues <- function(graph, pvalues, verbose=FALSE) {
	if (length(pvalues)!=length(nodes(graph))) {
		stop("Length of pvalues must equal number of nodes.")
	}
	if (is.null(names(pvalues))) {
		names(pvalues) <- nodes(graph)
	}
	# TODO for graphs with sum(weights)<1 (do we want to allow this) this will give wrong results
	if (sum(getWeights(graph))>0) nodeData(graph, nodes(graph), "nodeWeight") <- getWeights(graph)/sum(getWeights(graph))
	adjPValues <- rep(0, length(nodes(graph)))
	names(adjPValues) <- nodes(graph)	
	J <- nodes(graph)
	names(J) <- nodes(graph)
	sequence <- list(graph)
	pmax <- 0
	while(length(J) >= 1) {
		fraction <- pvalues[J]/getWeights(graph)[J]
		fraction[pvalues[J]==0] <- 0
		j <- which.min(fraction)
		node <- J[j]
		adjPValues[node] <- max(min(pvalues[node]/getWeights(graph)[node], 1), pmax)
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
	#		nodeData(graph2, to, "nodeWeight") <- nodeData(graph, to, "nodeWeight")[[to]] + getWeight(graph,node,to) * nodeData(graph, node, "nodeWeight")[[node]]
	#	}
	#}	
	
	## The following code will be removed in 0.4! ##
	if (all(TRUE == all.equal(unname(edgesOut), rep(0, length(edgesOut))))) {
		if (verbose) cat("Alpha is passed via epsilon-edges.\n")
		for (to in nodes(graph)[nodes(graph)!=node]) {	
			numberOfEpsilonEdges <- sum(TRUE == all.equal(unname(edgesOut), rep(0, length(edgesOut))))
			if (existsEdge(graph, node, to)) {
				nodeData(graph2, to, "nodeWeight") <- nodeData(graph, to, "nodeWeight")[[to]] + nodeData(graph, node, "nodeWeight")[[node]] / numberOfEpsilonEdges
				keepAlpha <- FALSE
			}
		}		
	} else {
		if (verbose) cat("Alpha is passed via non-epsilon-edges.\n")
		for (to in nodes(graph)[nodes(graph)!=node]) {				
			nodeData(graph2, to, "nodeWeight") <- nodeData(graph, to, "nodeWeight")[[to]] + getWeight(graph,node,to) * nodeData(graph, node, "nodeWeight")[[node]]				
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
		nodeData(graph, node, "nodeWeight") <- 0
	}
	nodeData(graph, node, "rejected") <- TRUE	
	return(graph)
}

getRejectableNode <- function(graph, alpha, pvalues) {
	x <- getWeights(graph)*alpha/pvalues
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
