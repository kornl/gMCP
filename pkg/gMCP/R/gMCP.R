gMCP <- function(graph, pvalues, test, correlation, alpha=0.05, 
		approxEps=TRUE, eps=10^(-3), ..., useC=FALSE, verbose=FALSE) {	
	output <- ""
	if (approxEps && !is.numeric(graph@m)) {
		graph <- substituteEps(graph, eps=eps)
	}
	if (!is.numeric(graph@m)) {
		stop("Graph seems to contain variables - please use function replaceVariables.")
		#graph <- parse2numeric(graph) # TODO ask for variables
	}
	sequence <- list(graph)
	if (length(pvalues)!=length(getNodes(graph))) {
		stop("Length of pvalues must equal number of nodes.")
	}
	if (is.null(names(pvalues))) {
		names(pvalues) <- getNodes(graph)
	}
	if (missing(test) && (missing(correlation) || length(pvalues)==1)) {
		# Bonferroni-based test procedure
		m <- graph2matrix(graph)
		if (useC && !is.numeric(m)) { # TODO Why this warning and btw. nothing is done in this case.
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
			adjPValues <- adjPValues(sequence[[1]], pvalues, verbose)@adjPValues
			return(new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph), adjPValues=adjPValues))
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
                adjP <- rep(1,length(w))
				rejected <- rep(FALSE,length(w))
				names(rejected) <- getNodes(graph)
                names(adjP) <- getNodes(graph)
			} else {
				#myTest <- generateTest(Gm, w, correlation, alpha)
				#zScores <- -qnorm(pvalues)
				#rejected <- myTest(zScores)
                adjP <- generatePvals(Gm,w,correlation,pvalues)
                rejected <- adjP <= alpha
                names(adjP) <- getNodes(graph)
				names(rejected) <- getNodes(graph)
			}
			return(new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=rejected, adjPValues=adjP))
		}
	} else if (test=="Simes") {		
		m <- graph2matrix(graph)
		if (all(pvalues>alpha)) {
			result <- new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph))
			if (verbose) {
				output <- "All p-values above alpha."
				cat(output,"\n")
				attr(result, "output") <- output
			}
			return(result)
		}
		if (all(pvalues<=alpha)) {			
			rejected <- rep(TRUE, dim(m)[1])
			names(rejected) <- getNodes(graph)
			result <- new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=rejected)
			if (verbose) {
				output <- "All p-values below alpha."
				cat(output,"\n")
				attr(result, "output") <- output
			}
			return(result)
		}
		while(!is.null(node <- getRejectableNode(graph, alpha, pvalues))) {
			output <- paste(output, paste("Node \"",node,"\" can be rejected by Bonferroni based test.\n",sep=""), sep="\n")
			graph <- rejectNode(graph, node, verbose)
			sequence <- c(sequence, graph)
		}
		n <- sum(!getRejected(graph))
		if (n<3) {
			result <- new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph))
			if (verbose) {
				if (n==2) output <- paste(output, "Only two hypotheses remaining.", sep="\n")
				if (n==1) output <- paste(output, "Only one hypothesis remaining.", sep="\n")
				if (n==0) output <- paste(output, "Everything allready rejected.", sep="\n")
				cat(output,"\n")
				attr(result, "output") <- output
			}
			return(result)
		} else {			
			graph2 <- subgraph(graph, !getRejected(graph))
			output <- paste(output, "Remaining hypotheses (new numeration):", paste(1:length(getNodes(graph2)),": ", getNodes(graph2), sep="",collapse="\n"), sep="\n")
			pvalues2 <- pvalues[!getRejected(graph)]
			allSubsets <- permutations(length(getNodes(graph2)))[-1,]
			result <- cbind(allSubsets, 0)
			weights <- generateWeights(graph2@m, getWeights(graph2))[,(n+(1:n))]
			if (verbose) explanation <- rep("not rejected", dim(allSubsets)[1])
			for (i in 1:dim(allSubsets)[1]) {
				subset <- allSubsets[i,]
				if(!all(subset==0)) {
					J <- which(subset!=0)	
					if (verbose) explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: ",explanation[i], sep="")				
					for (j in J) {
						Jj <- subset!=0 & (pvalues2 <= pvalues2[j]) # & (1:n)!=j
						#cat("j: ",j, ", Jj: ",Jj,"\n")
						#cat("p_",j,"=",pvalues2[j],"<=a*(w_",paste(which(Jj),collapse ="+w_"),")=",alpha,"*(",paste(weights[i, Jj],collapse ="+"),")=",sum(weights[i, Jj]),"\n")
						if (pvalues2[j]<=alpha*sum(weights[i, Jj])) {
							result[i, n+1] <- 1
							if (verbose) {
								explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: p_",j,"=", pvalues2[j],"<=a*(w_",paste(which(Jj),collapse ="+w_"),")\n     =",alpha,"*(",paste(weights[i, Jj],collapse ="+"),")=",alpha*sum(weights[i, Jj]),sep="")
							}
						}
					}					
				} 
			}
			for (i in 1:n) {
				if (all(result[result[,i]==1,n+1]==1)) {
					graph <- rejectNode(graph, getNodes(graph2)[i])
				}
			}
			result <- new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph))
			if (verbose) {
				output <- paste(output, paste(explanation, collapse="\n"), sep="\n")
				cat(output,"\n")
				attr(result, "output") <- output
			}
			return(result)
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
	if (length(pvalues)!=length(getNodes(graph))) {
		stop("Length of pvalues must equal number of nodes.")
	}
	if (is.null(names(pvalues))) {
		names(pvalues) <- getNodes(graph)
	}
	# TODO for graphs with sum(weights)<1 (do we want to allow this) this will give wrong results
	if (sum(getWeights(graph))>0) setWeights(graph, getWeights(graph)/sum(getWeights(graph)), getNodes(graph))
	adjPValues <- rep(0, length(getNodes(graph)))
	names(adjPValues) <- getNodes(graph)	
	J <- getNodes(graph)
	names(J) <- getNodes(graph)
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
	weights <- graph@weights
	graph@weights <- weights+weights[node]*graph@m[node,]
	m <- graph@m	
	for (i in getNodes(graph)) {
		if (m[i, node]*m[node, i]<1) {
			graph@m[i,] <- (m[i,]+m[i,node]*m[node,])/(1-m[i,node]*m[node,i]) 
		} else {
			graph@m[i,] <- 0
		}
	}
	diag(graph@m) <- 0
	graph@m[node,] <- 0
	graph@m[, node] <- 0
	if (!all(m[node,]==0)) graph@weights[node] <- 0	
	graph@nodeAttr$rejected[node] <- TRUE	
	return(graph)
}

getRejectableNode <- function(graph, alpha, pvalues) {
	x <- getWeights(graph)*alpha/pvalues
	x[pvalues==0] <- 1
	x[graph@nodeAttr$rejected] <- NaN
	i <- which.max(x)
	if (length(i)==0) return(NULL)
	if (x[i]>1 | all.equal(unname(x[i]),1)[1]==TRUE) {return(getNodes(graph)[i])}
	return(NULL)	 
}
