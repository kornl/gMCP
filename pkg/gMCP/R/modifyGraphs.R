joinGraphs <- function(graph1, graph2, xOffset=0, yOffset=200) {
	m1 <- graph2matrix(graph1)
	m2 <- graph2matrix(graph2)
	m <- bdiagNA(m1,m2)
	m[is.na(m)] <- 0
	nNames <- c(getNodes(graph1), getNodes(graph2))
	d <- duplicated(nNames)
	if(any(d)) {
		warning(paste(c("The two graphs have the following identical nodes: ", paste(nNames[d], collapse=", "), ". The nodes of the second graph will be renamed."), sep=""))
		nodes2 <- getNodes(graph2)
		i <- 1
		for (x in nNames[d]) {
			while (any(nNames==paste("H",i, sep=""))) {
				i <- i + 1
			}
			nodes2[nodes2==x] <- paste("H",i, sep="")
			i <- i + 1
		}
		nNames <- c(getNodes(graph1), nodes2)
	}
	rownames(m) <- nNames
	colnames(m) <- nNames
	graph <- matrix2graph(m)	
	weights <- c(getWeights(graph1), getWeights(graph2))
	if (sum(weights)>1) {
		weights <- weights / sum(weights)
	}
	graph <- setWeights(graph, weights=weights)
	nodeX <- c(getXCoordinates(graph1), getXCoordinates(graph2) + xOffset) 
	nodeY <- c(getYCoordinates(graph1), getYCoordinates(graph2) + yOffset) 
	names(nodeX) <- nNames
	names(nodeY) <- nNames
	graph@nodeAttr$X <- nodeX
	graph@nodeAttr$Y <- nodeY
	return(graph)
}

subGraph <- function(graph, subset) {
	if (is.logical(subset)) {
		subset <- getNodes(graph)[subset]	
	}
	m <- graph@m
	w <- getWeights(graph)
	rejected <- getRejected(graph)
	if (is.character(subset)) {
		subGraph <- matrix2graph(m[subset,subset], w)
		setRejected(subGraph, getNodes(subGraph)) <- rejected[subset]
		return(subGraph)
	} else {
		stop("The parameter subset must be either a logical or character vector.")
	}
}