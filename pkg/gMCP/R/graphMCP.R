## Graph representation in gMCP
setClass("graphMCP",	
		representation(m="matrix", 
				weights="numeric", 
				nodeData="list", 
				edgeData="list"),
		validity=function(object) validWeightedGraph(object))

setMethod("initialize", "graphMCP",
		function(.Object, m, weights, nodeData=list(), edgeData=list()) {			
			if (length(weights)) {			
				checkValidWeights(weights)
			}			
			colnames(m) <- rownames(m)
			.Object@m <- m
			names(weights) <- rownames(m)
			.Object@weights <- weights
			.Object@nodeData <- nodeData
			.Object@edgeData <- edgeData
			if(is.null(.Object@nodeData$rejected)) {
				.Object@nodeData$rejected <- rep(FALSE, dim(m)[1])
				names(.Object@nodeData$rejected) <- rownames(m)
			}
			validObject(.Object)
			return(.Object)
		})

validWeightedGraph <- function(object) {
	# if (sum(object@weights)>1)
	return(TRUE)
}

setClass("gMCPResult",		
		representation(graphs="list",
				pvalues="numeric",
				alpha="numeric",
				rejected="logical",
				adjPValues="numeric")
)

setMethod("print", "gMCPResult",
		function(x, ...) {
			callNextMethod(x, ...)
			#for (node in nodes(x)) {
			#	cat(paste(node, " (",ifelse(unlist(nodeData(x, node, "rejected")),"rejected","not rejected"),", alpha=",format(unlist(nodeData(x, node, "nodeWeight")), digits=4 ,drop0trailing=TRUE),")\n", sep=""))	
			#}
			#cat(paste("alpha=",paste(format(getWeights(x), digits=4 ,drop0trailing=TRUE),collapse="+"),"=",sum(getWeights(x)),"\n", sep=""))			
		})

setMethod("show", "gMCPResult",
		function(object) {
			# callNextMethod(x, ...)
			cat("gMCP-Result\n")			
			cat("\nInitial graph:\n")
			print(object@graphs[[1]])
			cat("\nP-values:\n")
			print(object@pvalues)						
			if (length(object@adjPValues)>0) {
				cat("\nAdjusted p-values:\n")
				print(object@adjPValues)
			}
			cat(paste("\nAlpha:",object@alpha,"\n"))
			if (all(!object@rejected)) {
				cat("\nNo hypotheses could be rejected.\n")				
			} else {
				cat("\nHypothesis rejected:\n")
				print(object@rejected)
			}
			if (length(object@graphs)>1) {
				cat("\nFinal graph after", length(object@graphs)-1 ,"steps:\n")
				print(object@graphs[[length(object@graphs)]])
			}			
		})

setMethod("plot", "gMCPResult",
		function(x, y, ...) {
			# TODO Show visualization of graph			
		})

if (!require(graph)) {
	setGeneric("nodes", function(object, ...) standardGeneric("nodes"))
	setGeneric("addEdge", function(from, to, graph, weights) standardGeneric("addEdge"))
	setGeneric("edgeData", function(self, from, to, attr) standardGeneric("edgeData"))
	setGeneric("edgeData<-", function(self, from, to, attr, value) standardGeneric("edgeData<-"))
	setGeneric("nodeData", function(self, n, attr) standardGeneric("nodeData"))
	setGeneric("nodeData<-", function(self, n, attr, value) standardGeneric("nodeData<-"))
}

setMethod("nodes", c("graphMCP"),
		function(object, ...) {			
			return(rownames(object@m))
		})

setGeneric("getWeights", function(object, node, ...) standardGeneric("getWeights"))

setMethod("getWeights", c("graphMCP"),
		function(object, node, ...) {
			weights <- object@weights
			names(weights) <- nodes(object)
			if (!missing(node)) {
				return(weights[node])
			}
			return(weights)
		})

setGeneric("setWeights", function(object, weights, node, ...) standardGeneric("setWeights"))

setMethod("setWeights", c("graphMCP"),
		function(object, weights, node, ...) {
			if (missing(node)) {
				node <- nodes(object)
			}
			object@weights[node] <- weights			
			return(object)
		})

setMethod("getWeights", c("gMCPResult"),
		function(object, node, ...) {
			graph <- object@graphs[[length(object@graphs)]]			
			return(getWeights(graph, node))
		})

setMethod("addEdge", signature=signature(from="character", to="character",
				graph="graphMCP", weights="character"),
		function(from, to, graph, weights) {
			graph@m[from, to] <- weights
			graph
		})

setMethod("addEdge", signature=signature(from="character", to="character",
				graph="graphMCP", weights="numeric"),
		function(from, to, graph, weights) {
			graph@m[from, to] <- weights
			graph
		})

setMethod("edgeData", signature(self="graphMCP", from="character", to="character",
				attr="character"),
		function(self, from, to, attr) {
			self@edgeData[[attr]][from, to]
		})

setReplaceMethod("edgeData",
		signature(self="graphMCP", from="character", to="character", attr="character", value="ANY"),
		function(self, from, to, attr, value) {
			if (is.null(self@edgeData[[attr]])) self@edgeData[[attr]] <- matrix(NA, nrow=dim(self@m)[1], ncol=dim(self@m)[2])			
			rownames(self@edgeData[[attr]]) <- colnames(self@edgeData[[attr]]) <- nodes(self)
			self@edgeData[[attr]][from, to] <- value		
			self
		})

setMethod("nodeData", signature(self="graphMCP", n="character", attr="character"),
		function(self, n, attr) {
			self@nodeData[[attr]][n]
		})

setReplaceMethod("nodeData",
		signature(self="graphMCP", n="character", attr="character", value="ANY"),
		function(self, n, attr, value) {
			if (is.null(self@nodeData[[attr]])) self@nodeData[[attr]] <- logical(length=length(nodes(self)))
			self@nodeData[[attr]][n] <- value			
			self
		})

setGeneric("getRejected", function(object, node, ...) standardGeneric("getRejected"))

setMethod("getRejected", c("graphMCP"), function(object, node, ...) {
			rejected <- object@nodeData$rejected
			if (!missing(node)) {
				return(rejected[node])
			}
			return(rejected)
		})

setMethod("getRejected", c("gMCPResult"), function(object, node, ...) {			
			rejected <- object@rejected
			if (!missing(node)) {
				return(rejected[node])
			}
			return(rejected)
		})

setGeneric("setRejected", function(object, rejected, node, ...) standardGeneric("setRejected"))

setMethod("setRejected", c("graphMCP"),
		function(object, rejected, node, ...) {
			if (missing(node)) {
				node <- nodes(object)
			}
			object@nodeData$rejected[node] <- rejected			
			return(object)
		})

setGeneric("getXCoordinates", function(graph, node) standardGeneric("getXCoordinates"))

setMethod("getXCoordinates", c("graphMCP"), function(graph, node) {
			x <- graph@nodeData$X
			names(x) <- nodes(graph)
			if (!missing(node)) {
				return(x[node])
			}
			return(x)
		})

setGeneric("getYCoordinates", function(graph, node) standardGeneric("getYCoordinates"))

setMethod("getYCoordinates", c("graphMCP"), function(graph, node) {
			y <- graph@nodeData$Y
			names(y) <- nodes(graph)
			if (!missing(node)) {
				return(y[node])
			}
			return(y)
		})

canBeRejected <- function(graph, node, alpha, pvalues) {	
	return(getWeights(graph)[[node]]*alpha>pvalues[[node]] | (all.equal(getWeights(graph)[[node]]*alpha,pvalues[[node]])[1]==TRUE));
}

setMethod("print", "graphMCP",
		function(x, ...) {
			callNextMethod(x, ...)
			#for (node in nodes(x)) {
			#	cat(paste(node, " (",ifelse(unlist(nodeData(x, node, "rejected")),"rejected","not rejected"),", alpha=",format(unlist(nodeData(x, node, "nodeWeight")), digits=4 ,drop0trailing=TRUE),")\n", sep=""))	
			#}
			#cat(paste("alpha=",paste(format(getWeights(x), digits=4 ,drop0trailing=TRUE),collapse="+"),"=",sum(getWeights(x)),"\n", sep=""))			
		})

setMethod("show", "graphMCP",
		function(object) {
			#callNextMethod(object)
			nn <- nodes(object)
			cat("A graphMCP graph\n")
			if (!isTRUE(all.equal(sum(getWeights(object)),1))) {
				cat(paste("Sum of weight: ",sum(getWeights(object)),"\n", sep=""))
			}			
			for (node in nodes(object)) {
				cat(paste(node, " (",ifelse(nodeData(object, node, "rejected"),"rejected","not rejected"),", weight=",format(object@weights[node], digits=4, drop0trailing=TRUE),")\n", sep=""))
			}
			printEdge <- FALSE;
			for (i in nodes(object)) {
				for (j in nodes(object)) {
					if (object@m[i,j]!=0) {
						if (!printEdge) {
							cat("Edges:\n")
							printEdge <- TRUE
						}
						cat(paste(i, " -(", object@m[i,j], ")-> ", j, "\n"))
					}
				}
			}
			if (!printEdge) cat("No edges.\n")
			#cat(paste("\nalpha=",paste(format(getWeights(object), digits=4 ,drop0trailing=TRUE),collapse="+"),"=",sum(getWeights(object)),"\n", sep=""))
			cat("\n")
		}
)

getWeightStr <- function(graph, from, to, LaTeX=FALSE) {
	if (LaTeX) {
		#TODO
	}
	return(graph@m[from,to])	
}

setMethod("plot", "graphMCP",
		function(x, y, ...) {
			# TODO Show visualization of graph			
		})

setGeneric("simConfint", function(object, pvalues, confint, alternative=c("less", "greater"), estimates, df, alpha=0.05, mu=0) standardGeneric("simConfint"))

setMethod("simConfint", c("graphMCP"), function(object, pvalues, confint, alternative=c("less", "greater"), estimates, df, alpha=0.05, mu=0) {
			result <- gMCP(object, pvalues, alpha=alpha)
			if (all(getRejected(result))) {
				alpha <- getWeights(object)*alpha				
			} else {
				alpha <- getWeights(result)*alpha				
			}
			if (class(confint)=="function") {
				f <- function(node, alpha, rejected) {
					if (rejected && alternative=="less") return(c(-Inf, mu))
					if (rejected && alternative=="greater") return(c(mu, Inf))
					return(confint(node, alpha))
				}
				m <- mapply(f, nodes(object), alpha, getRejected(result))	
				m <- rbind(m[1,], estimates, m[2,])
				rownames(m) <- c("lower bound", "estimate", "upper bound")
				return(t(m))
			} else if (confint=="t") {
				dist <- function(x) {qt(p=x, df=df)}
			} else if (confint=="normal") {
				dist <- qnorm
			} else {
				stop('Parameter confint has to be a function or "t" or "normal".')
			}
			if (alternative=="greater") {			
				stderr <- abs(estimates/dist(1-pvalues))
				lb <- estimates+dist(alpha)*stderr				
				lb <- ifelse(getRejected(result), max(0,lb), lb) 
				ub <- rep(Inf,length(lb))
			} else if (alternative=="less") {			
				stderr <- abs(estimates/dist(pvalues))								 
				ub <- estimates+dist(1-alpha)*stderr				
				ub <- ifelse(getRejected(result), min(0,ub), ub)
				lb <- rep(-Inf,length(ub))
			} else {
				stop("Specify alternative as \"less\" or \"greater\".")
			}
			m <- matrix(c(lb, estimates, ub), ncol=3)
			colnames(m) <- c("lower bound", "estimate", "upper bound")
			return(m)
		})
