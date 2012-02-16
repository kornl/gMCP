getEdges <- function(graph){
	fromL <- c()
	toL <- c()
	weightL <- numeric(0)
	labelx <- numeric(0)
	labely <- numeric(0)
	curveL <- logical(0)
	weightStrL <- character(0)
	for (from in getNodes(graph)) {
		for (to in getNodes(graph)) {
			options(warn=-1)
			if (is.na(as.numeric(graph@m[from, to])) || as.numeric(graph@m[from, to])!=0) {	
				options(warn=0)			
				x <- try(unlist(edgeAttr(graph, from, to, "labelX")), silent = TRUE)
				if (class(x)!="try-error" && !is.null(x) && !is.na(x)) {
					labelx <- c(labelx, x)
				} else {
					labelx <- c(labelx, -100)
				}
				y <- try(unlist(edgeAttr(graph, from, to, "labelY")), silent = TRUE)
				if (class(y)!="try-error" && !is.null(y) && !is.na(y)) {
					labely <- c(labely, y)
				} else {
					labely <- c(labely, -100)
				}
				
				fromL <- c(fromL, from)
				toL <- c(toL, to)
				options(warn=-1)
				weightL <- c(weightL, as.numeric(graph@m[from, to]))				
				curve <- (is.na(as.numeric(graph@m[to, from])) || as.numeric(graph@m[to, from])!=0)
				options(warn=0)
				curveL <- c(curveL, curve)
				weightStrL <- c(weightStrL, graph@m[from, to])
			}
		}
	}
	return(list(from=fromL, to=toL, weight=weightL, labelx=labelx, labely=labely, curve=curveL, weightStr=as.character(weightStrL)))
}

checkPSD <- function(m) {
	if (!all(!is.na(M))) {
		return("Matrix contains NA values.")	
	}
	ev <- eigen(m, symmetric=TRUE, only.values=TRUE)
	# We use the same check as mvtnorm to minimize problems:
	if (!all(ev$values >= -sqrt(.Machine$double.eps) * abs(ev$values[1]))) {
		return(paste("Matrix is not positive semidefinite, lowest eigen value is:",min(ev$values)))
	}
	return("")
}

placeNodes <- function(graph, nrow, ncol, byrow = TRUE, force = FALSE) {
	# Only place nodes if  no placement data exists or parameter force is set to TRUE
	if (is.null(graph@nodeAttr$X) || force) {
		n <- length(getNodes(graph))
		if (missing(nrow) && missing(ncol)) {		
			v <- (1:n)/n*2*pi
			nodeX <- 300 + 250*sin(v)
			nodeY <- 300 + 250*cos(v)			
		} else {
			if (missing(nrow)) {
				nrow <- ceiling(length(getNodes(graph))/ncol)
			}
			if (missing(ncol)) {
				ncol <- ceiling(length(getNodes(graph))/nrow)
			}
			# if nrow*ncol<n increase the number of rows
			if (nrow*ncol<n) {
				nrow <- ceiling(length(getNodes(graph))/ncol)
			}
			if (byrow) {
				nodeX <- rep(((1:ncol)-1)*200+100, nrow)
				nodeY <- rep(((1:nrow)-1)*200+100, each = ncol)
			} else {
				nodeX <- rep(((1:ncol)-1)*200+100, each = nrow)
				nodeY <- rep(((1:nrow)-1)*200+100, ncol)
			}
		}
		graph@nodeAttr$X <- nodeX[1:n]
		graph@nodeAttr$Y <- nodeY[1:n]
		for (i in getNodes(graph)) {
			for (j in getNodes(graph)) {
				if (graph@m[i,j]!=0) {
					edgeAttr(graph, i, j, "labelX") <- -100
					edgeAttr(graph, i, j, "labelY") <- -100
				}
			}
		}		
	}	
	return(graph)	
}

# I guess I simply don't understand how the graph package is supposed to be used.
# Or they have a bug. I have contacted them but got no response. Therefore I still use this stupid work-around:
stupidWorkAround <- function(graph) {
	if (length(graph@edgeAttr@data)>0) {
		for (i in 1:length(graph@edgeAttr@data)) {
			if (length(graph@edgeAttr@data[[i]])>0) {
				for (j in 1:length(graph@edgeAttr@data[[i]])){
					graph@edgeAttr@data[[i]][[j]] <- unname(graph@edgeAttr@data[[i]][[j]])
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
	if (length(matrices)==0) {
		if (n=="all") {
			return("No quadratic matrices found.")
		} else {
			return(paste("No ",n,"x",n,"-matrices found.", sep=""))
		}
	}
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