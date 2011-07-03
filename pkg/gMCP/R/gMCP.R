gMCP <- function(graph, pvalues, test, correlation, alpha=0.05, 
		approxEps=TRUE, eps=10^(-3), ..., useC=FALSE, verbose=FALSE) {
	sequence <- list(graph)
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
		# Bonferroni-based test procedure
		m <- graph2matrix(graph)
		if (useC && !is.numeric(m)) {
			warning("Option useC=TRUE will be ignored since graph contains epsilons or variables.")			
		} else if (useC) {
			w <- getWeights(graph)
			result <- fastgMCP(m=m, w=w, p=pvalues, a=alpha)
			lGraph <- matrix2graph(result$m)
			lGraph <- setWeights(lGraph, result$w)			
			lGraph <- setRejected(lGraph, result$rejected)
			sequence <- c(sequence, lGraph)
			return(new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(lGraph), adjPValues=numeric(0)))
		} else {
			while(!is.null(node <- getRejectableNode(graph, alpha, pvalues))) {
				if (verbose) cat(paste("Node \"",node,"\" can be rejected.\n",sep=""))
				graph <- rejectNode(graph, node, verbose)
				sequence <- c(sequence, graph)
			}	
			return(new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph), adjPValues=numeric(0))) # adjPValues(sequence[[1]], pvalues, verbose)@adjPValues))
		}
	} else if (missing(test) && !missing(correlation)) {
		# Calling the code from Florian
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
				#myTest <- generateTest(Gm, w, correlation, alpha)
				#zScores <- -qnorm(pvalues)
				#rejected <- myTest(zScores)
                                adjP <- generatePvals(Gm,w,correlation,pvalues)
                                rejected <- adjP <= alpha
                                names(adjP) <- nodes(graph)
				names(rejected) <- nodes(graph)
			}
			return(new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=rejected, adjPValues=adjP))
		}
	}
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
	if (sum(getWeights(graph))>0) setWeights(graph, getWeights(graph)/sum(getWeights(graph)), nodes(graph))
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
		adjPValues[node] <- max(min(ifelse(pvalues[node]==0,0,pvalues[node]/getWeights(graph)[node]), 1), pmax)
		pmax <- adjPValues[node]
		if (verbose) cat(paste("We will update the graph with node \"",node,"\".\n",sep=""))
		graph <- rejectNode(graph, node, verbose)
		J <- J[J!=node]
		sequence <- c(sequence, graph)
	}	
	return(new("gMCPResult", graphs=sequence, pvalues=pvalues, adjPValues=adjPValues))
}

rejectNode <- function(graph, node, verbose=FALSE) {

	# TODO (Remember: Keep alpha & do we need to copy the graph?)
	graph@weights[node] <- 0
	
	graph@nodeData$rejected[node] <- TRUE
	
	return(graph)
}

getRejectableNode <- function(graph, alpha, pvalues) {
	x <- getWeights(graph)*alpha/pvalues
	x[pvalues==0] <- 1
	x[graph@nodeData$rejected] <- NaN
	i <- which.max(x)
	if (length(i)==0) return(NULL)
	if (x[i]>1 | all.equal(unname(x[i]),1)[1]==TRUE) {return(nodes(graph)[i])}
	return(NULL)	 
}
