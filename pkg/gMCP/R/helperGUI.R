getEdges <- function(graph){
	fromL <- c()
	toL <- c()
	weightL <- numeric(0)
	labelx <- numeric(0)
	labely <- numeric(0)
	curveL <- logical(0)
	weightStrL <- character(0)
	for (node in nodes(graph)) {
		edgeL <- edgeWeights(graph)[[node]]	
		if (length(edgeL)!=0) {
			for (i in 1:length(edgeL)) {
				# Label: Are these silent "tries" really tested? Or did I just forgot documentating it?
				weight <- try(edgeData(graph, node, names(edgeL[i]), "weight"), silent = TRUE)
				p <- unlist(edgeData(graph, node, names(edgeL[i]), "epsilon"))
				attributes(p) <- NULL # Always do this when using all.equal	
				weight <- ifelse(length(p)>0 && !isTRUE(all.equal(p, rep(0,length(p)))), NaN, edgeL[i])
				weightStr <- getWeightStr(graph, node, names(edgeL[i]))
				x <- try(unlist(edgeData(graph, node, names(edgeL[i]), "labelX")), silent = TRUE)
				if (class(x)!="try-error") {
					labelx <- c(labelx, x)
				} else {
					labelx <- c(labelx, -100)
				}
				y <- try(unlist(edgeData(graph, node, names(edgeL[i]), "labelY")), silent = TRUE)
				if (class(y)!="try-error") {
					labely <- c(labely, y)
				} else {
					labely <- c(labely, -100)
				}								
				fromL <- c(fromL, node)
				toL <- c(toL, names(edgeL[i]))
				weightL <- c(weightL, weight)
				curve <- node%in%unlist(edges(graph, names(edgeL[i])))
				curveL <- c(curveL, curve)
				weightStrL <- c(weightStrL, weightStr)
			}
		}
	}
	return(list(from=fromL, to=toL, weight=weightL, labelx=labelx, labely=labely, curve=curveL, weightStr=weightStrL))
}

arrangeNodes <- function(graph) {
	if (length(nodeRenderInfo(graph))==0) {
		n <- length(nodes(graph))
		v <- (1:n)/n*2*pi
		nodeX <- 300 + 250*sin(v)
		nodeY <- 300 + 250*cos(v)
		names(nodeX) <- nodes(graph)
		names(nodeY) <- nodes(graph)
		nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)
	}
	return(graph)	
}

# I guess I simply don't understand how the graph package is supposed to be used.
# Or they have a bug. I have contacted them but got no response. Therefore I still use this stupid work-around:
stupidWorkAround <- function(graph) {
	if (length(graph@edgeData@data)>0) {
		for (i in 1:length(graph@edgeData@data)) {
			if (length(graph@edgeData@data[[i]])>0) {
				for (j in 1:length(graph@edgeData@data[[i]])){
					graph@edgeData@data[[i]][[j]] <- unname(graph@edgeData@data[[i]][[j]])
				}
			}
		}
	}
	return(graph)
}

getAllQuadraticMatrices <- function(envir=globalenv(), n="all") {
	objects <- ls(envir)
	matrices <- c()
	for (obj in objects) {
		candidate <- get(obj, envir=envir)
		if (is.matrix(candidate) && dim(candidate)[1] == dim(candidate)[2]) {
			if (n=="all" || dim(candidate)[1]==n) {
				matrices <- c(matrices, obj)
			}
		}
	}
	if (length(matrices)==0) return("No quadratic matrices found.")
	return(matrices)
}

getAllGraphs <- function(envir=globalenv()) {
	objects <- ls(envir)
	graphs <- c()
	for (obj in objects) {
		candidate <- get(obj, envir=envir)
		if ("graphMCP" %in% class(candidate)) {
			graphs <- c(graphs, obj)
		}
	}
	if (length(graphs)==0) return("No graphMCP objects found.")
	return(graphs)
}

getObjectInfo <- function(object) {
	return(paste(capture.output(print(object)), collapse="\n"))
}

gMCPVersion <- function() {
	x <- try(as.character(packageVersion("gMCP")), silent=TRUE)
	if (class(x)!="try-error") {
		return(x)
	} else {
		return("unknown")
	}
}