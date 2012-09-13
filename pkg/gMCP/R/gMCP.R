gMCP <- function(graph, pvalues, test, correlation, alpha=0.05, 
		approxEps=TRUE, eps=10^(-3), ..., useC=FALSE, 
		verbose=FALSE, keepWeights=TRUE, adjPValues=TRUE) {
#		, alternatives="less") {	
	if ("entangledMCP" %in% class(graph)) {
		if (!missing(correlation) || !missing(test) && test != "Bonferroni") {
			stop("Only Bonferroni based testing procedures are supported for entangled graphs in this version.")
		}
		
	}
	output <- ""
	callFromGUI <- !is.null(list(...)[["callFromGUI"]])
	if (verbose) {
		output <- paste(output, checkOptimal(graph), sep="\n")
	}
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
	if ((missing(test) && missing(correlation)) || !missing(test) && test == "Bonferroni") {
		if (!missing(correlation)) stop("Bonferroni test can not take correlation into account. Please specify test procedure.")
		# Bonferroni-based test procedure		
		if (useC) {
			w <- getWeights(graph)			
			result <- fastgMCP(m=graph2matrix(graph), w=w, p=pvalues, a=alpha, keepWeights=keepWeights)
			row.names(result$m) <- getNodes(graph)
			lGraph <- matrix2graph(result$m)
			lGraph <- setWeights(lGraph, result$w)			
			lGraph <- setRejected(lGraph, getNodes(lGraph), result$rejected)
			sequence <- c(sequence, lGraph)
			return(new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(lGraph), adjPValues=numeric(0)))
		} else {
			while(!is.null(node <- getRejectableNode(graph, alpha, pvalues))) {
				# if (verbose) cat(paste("Node \"",node,"\" can be rejected.\n",sep=""))
				graph <- rejectNode(graph, node, verbose, keepWeights=keepWeights)
				sequence <- c(sequence, graph)
			}	
			adjPValues <- adjPValues(sequence[[1]], pvalues, verbose)@adjPValues
			return(new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph), adjPValues=adjPValues))
		}
	} else if ((missing(test) && !missing(correlation)) || !missing(test) && test == "Bretz2011" || !missing(test) && test == "simple-parametric") {				
		if (missing(correlation) || !is.matrix(correlation)) {
			stop("Procedure for correlated tests, expects a correlation matrix as parameter \"correlation\".")
		} else {
#			if (is.character(correlation)) {
#				samplesize <- list(...)[["samplesize"]]
#				if (is.null(samplesize)) samplesize <- getBalancedDesign(correlation, length(pvalues))				
#				x <- contrMat(samplesize, type = correlation) # balanced design up to now and only Dunnett will work with n+1
#				var <- x %*% diag(length(samplesize)) %*% t(x)
#				correlation <- diag(1/sqrt(diag(var)))%*%var%*%diag(1/sqrt(diag(var)))
#			}                       
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
                adjP <- generatePvals(Gm, w, correlation, pvalues, exhaust=(missing(test) || test == "Bretz2011")) #, alternatives=alternatives)
                rejected <- adjP <= alpha
                names(adjP) <- getNodes(graph)
				names(rejected) <- getNodes(graph)
			}
			return(new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=rejected, adjPValues=adjP))
		}
	} else if (!missing(test) && test=="Simes") {		
		m <- graph2matrix(graph)
		if (!adjPValues) {
			if (all(pvalues>alpha)) {
				result <- new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph))
				if (verbose) {
					output <- paste(output, "All p-values above alpha.", sep="\n")
					if (!callFromGUI) cat(output,"\n")
					attr(result, "output") <- output
				}
				return(result)
			}
			if (all(pvalues<=alpha)) {			
				rejected <- rep(TRUE, dim(m)[1])
				names(rejected) <- getNodes(graph)
				result <- new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=rejected)
				if (verbose) {
					output <- paste(output, "All p-values below alpha.", sep="\n")
					if (!callFromGUI) cat(output,"\n")
					attr(result, "output") <- output
				}
				return(result)
			}
			while(!is.null(node <- getRejectableNode(graph, alpha, pvalues))) {
				output <- paste(output, paste("Hypothesis \"",node,"\" can be rejected by the weighted Bonferroni based test and therefore by weighted Simes test.\n",sep=""), sep="\n")
				graph <- rejectNode(graph, node, verbose)
				sequence <- c(sequence, graph)
			}
		}
		n <- sum(!getRejected(graph))
		if (n<3 && !adjPValues) {
			result <- new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph))
			if (verbose) {
				if (n==2) output <- paste(output, "Only two hypotheses remaining.", sep="\n")
				if (n==1) output <- paste(output, "Only one hypothesis remaining.", sep="\n")
				if (n==0) output <- paste(output, "Everything allready rejected.", sep="\n")
				if (!callFromGUI) cat(output,"\n")
				attr(result, "output") <- output
			}
			return(result)
		} else {			
			graph2 <- subgraph(graph, !getRejected(graph))
			output <- paste(output, "Remaining hypotheses (new numeration):", paste(1:length(getNodes(graph2)),": ", getNodes(graph2), sep="",collapse="\n"), sep="\n")
			pvalues2 <- pvalues[!getRejected(graph)]
			allSubsets <- permutations(length(getNodes(graph2)))[-1,]
			result <- cbind(allSubsets, 0, Inf)
			weights <- generateWeights(graph2@m, getWeights(graph2))[,(n+(1:n))]
			if (verbose) explanation <- rep("not rejected", dim(allSubsets)[1])
			for (i in 1:dim(allSubsets)[1]) {
				subset <- allSubsets[i,]
				if(!all(subset==0)) {
					J <- which(subset!=0)	
					if (verbose) explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: ",explanation[i], sep="")			
					mJ <- Inf					
					for (j in J) {
						Jj <- subset!=0 & (pvalues2 <= pvalues2[j]) # & (1:n)!=j
						if (adjPValues) {
							mJt <- pvalues2[j]/sum(weights[i, Jj])	
							if (is.na(mJt)) { # this happens only if pvalues2[j] is 0
								mJt <- 0
							}
							#cat("pvalues2:\n", pvalues2, "\nmJt: ", mJt, "\nmJ: ", mJ, "\nj: ", j, "\nJ: ", J, "\nsubset: ", subset, "\n")
							if (mJt<mJ) {
								mJ <- mJt
							}
						}
						#cat("j: ",j, ", Jj: ",Jj,"\n")
						#cat("p_",j,"=",pvalues2[j],"<=a*(w_",paste(which(Jj),collapse ="+w_"),")=",alpha,"*(",paste(weights[i, Jj],collapse ="+"),")=",sum(weights[i, Jj]),"\n")
						if (pvalues2[j]<=alpha*sum(weights[i, Jj])) {
							result[i, n+1] <- 1
							if (verbose) {
								explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: p_",j,"=", pvalues2[j],"<=a*(w_",paste(which(Jj),collapse ="+w_"),")\n     =",alpha,"*(",paste(weights[i, Jj],collapse ="+"),")=",alpha*sum(weights[i, Jj]),sep="")
							}
						}
					}	
					result[i, n+2] <- mJ
				} 
			}
			adjPValuesV <- rep(NA, n)
			for (i in 1:n) {
				if (all(result[result[,i]==1,n+1]==1)) {
					graph <- rejectNode(graph, getNodes(graph2)[i])
				}
				adjPValuesV[i] <- max(result[result[,i]==1,n+2])
			}
			result <- new("gMCPResult", graphs=sequence, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph), adjPValues=adjPValuesV)
			if (verbose) {
				output <- paste(output, paste(explanation, collapse="\n"), sep="\n")
				if (!callFromGUI) cat(output,"\n")
				attr(result, "output") <- output
			}
			return(result)
		}
	} else {
		stop(paste("Specified test \"",test,"\" is not known.",sep=""))
	}
}

createGMCPCall <- function(graph, pvalues, test, correlation, alpha=0.05, 
		approxEps=TRUE, eps=10^(-3), ..., useC=FALSE, 
		verbose=FALSE, keepWeights=TRUE, adjPValues=TRUE) {	
	command <- paste(dputMatrix(graph@m, name="m", indent=11, rowNames=TRUE), sep="")
	command <- paste(command, "weights <- ",dput2(unname(graph@weights)),"\n", sep="")
	command <- paste(command, "graph <- new(\"graphMCP\", m=m, weights=weights)\n", sep="")
	command <- paste(command, "pvalues <- ",dput2(unname(pvalues)),"\n", sep="")
	if (!missing(correlation)) {
		command <- paste(command, dputMatrix(correlation, name="cr", indent=12),"\n", sep="")
	}
	command <- paste(command, "gMCP(graph, pvalues", sep="")
	if (!missing(test)) {
		command <- paste(command, ", test=\"",test,"\"", sep="")
	}
	if (!missing(correlation)) {
		command <- paste(command, ", correlation=cr", sep="")
	}
	command <- paste(command, ", alpha=",alpha, sep="")
	command <- paste(command, ")\n", sep="")
	return(command)
}

#TODO: Set rejected.
dputGraph <- function(g, name="graph") {
	s <- dputMatrix(g@m, name="m", indent=11, rowNames=TRUE)
	s <- paste(s, "weights <- ",dput2(unname(g@weights)),"\n", sep="")
	s <- paste(s, name, " <- new(\"graphMCP\", m=m, weights=weights)\n", sep="")
	return(s)
}

dputMatrix <- function(m, name, indent=6, rowNames=FALSE) {
	s <- "rbind("
	if (!missing(name)) s <- paste(name,"<- rbind(") 
	for (i in 1:(dim(m)[1])) {
		nameLater <- FALSE
		if (any(make.names(row.names(m))!=row.names(m))) {
			rowNames <- FALSE
			nameLater <- TRUE
		}
		rName <- ifelse(rowNames, paste(row.names(m)[i],"=",sep=""), "")
		s <- paste(s, 
			ifelse(i==1,"",paste(rep(" ",indent),collapse="")),
			rName,
			dput2(unname(m[i,])),
			ifelse(i==dim(m)[1],")\n",",\n"),
			sep="")	    
	}
	if (nameLater) {
		if (missing(name)) {
			warning("Can set row names if no name for matrix is given.")
			return(s)
		}
		s <- paste(s, 
				"row.names(",name,") <- ", dput2(row.names(m)), "\n", sep="")
	}
	return(s)
}

dput2 <- function(x) {
	paste(capture.output(dput(x)), collapse=" ")
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
		# if (verbose) cat(paste("We will update the graph with node \"",node,"\".\n",sep=""))
		graph <- rejectNode(graph, node, verbose)
		J <- J[J!=node]
		sequence <- c(sequence, graph)
	}	
	return(new("gMCPResult", graphs=sequence, pvalues=pvalues, adjPValues=adjPValues))
}

rejectNode <- function(graph, node, verbose=FALSE, keepWeights=TRUE) {
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
	if (!all(m[node,]==0) || !keepWeights) graph@weights[node] <- 0	
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
