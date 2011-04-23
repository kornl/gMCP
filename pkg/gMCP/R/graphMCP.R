## Graph representation in gMCP
setClass("graphMCP",
		contains="graphNEL",
		#representation(alpha="numeric"),
		validity=function(object) validGraph(object))


setMethod("initialize", "graphMCP",
		function(.Object, nodes=character(0), edgeL, weights) {
			.Object <- callNextMethod(.Object, nodes, edgeL, edgemode="directed")
			if (length(weights)) {			
				checkValidWeights(weights)
			}
			defaultProps <- list(nodeWeight=0, rejected=FALSE)
			nodeAttrData <- new("attrData", defaults=defaultProps)
			attrDataItem(nodeAttrData, x=nodes, attr="nodeWeight") <- weights
			.Object@nodeData <- nodeAttrData
			edgeDataDefaults(.Object, "labelX") <- -100
			edgeDataDefaults(.Object, "labelY") <- -100
			edgeDataDefaults(.Object, "epsilon") <- list(0)
			edgeDataDefaults(.Object, "variableWeight") <- ""
			validObject(.Object)
			return(.Object)
		})

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

setGeneric("getWeights", function(object, node, ...) standardGeneric("getWeights"))

setMethod("getWeights", c("graphMCP"),
		function(object, node, ...) {
			alpha <- unlist(nodeData(object, nodes(object), "nodeWeight"))
			names(alpha) <- nodes(object)
			if (!missing(node)) {
				return(alpha[node])
			}
			return(alpha)
		})

setGeneric("setWeights", function(object, weights, node, ...) standardGeneric("setWeights"))

setMethod("setWeights", c("graphMCP"),
		function(object, weights, node, ...) {
			if (missing(node)) {
				node <- nodes(object)
			}
			nodeData(object, nodes(object), "nodeWeight") <- weights			
			return(object)
		})

setMethod("getWeights", c("gMCPResult"),
		function(object, node, ...) {
			graph <- object@graphs[[length(object@graphs)]]
			alpha <- unlist(nodeData(graph, nodes(graph), "nodeWeight"))
			names(alpha) <- nodes(graph)
			if (!missing(node)) {
				return(alpha[node])
			}
			return(alpha)
		})

setMethod("addEdge", signature=signature(from="character", to="character",
				graph="graphMCP", weights="character"),
		function(from, to, graph, weights) {
			p <- parseEpsPolynom(weights)
			graph <- addEdge(from, to, graph, p[1])
			if (length(p)>1) {
				edgeData(graph, from=from, to=to, attr="epsilon") <- list(p[2:length(p)])	
			}			
			graph
		})

setMethod("addEdge", signature=signature(from="character", to="character",
				graph="graphNEL", weights="numeric"),
		function(from, to, graph, weights) {
			graph <- addEdge(from, to, graph)
			if (!("weight" %in% names(edgeDataDefaults(graph))))
				edgeDataDefaults(graph, attr="weight") <- 1:1
			edgeData(graph, from=from, to=to, attr="weight") <- weights
			edgeData(graph, from=from, to=to, attr="epsilon") <- list(0)
			graph
		})

setGeneric("getRejected", function(object, node, ...) standardGeneric("getRejected"))

setMethod("getRejected", c("graphMCP"), function(object, node, ...) {
			rejected <- unlist(nodeData(object, nodes(object), "rejected"))
			names(rejected) <- nodes(object)
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

setGeneric("getXCoordinates", function(graph, node) standardGeneric("getXCoordinates"))

setMethod("getXCoordinates", c("graphMCP"), function(graph, node) {
			x <- nodeRenderInfo(graph)$nodeX
			names(x) <- nodes(graph)
			if (!missing(node)) {
				return(x[node])
			}
			return(x)
		})

setGeneric("getYCoordinates", function(graph, node) standardGeneric("getYCoordinates"))

setMethod("getYCoordinates", c("graphMCP"), function(graph, node) {
			y <- nodeRenderInfo(graph)$nodeY
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
			cat("A graphMCP graph\n")
			if (!isTRUE(all.equal(sum(getWeights(object)),1))) {
				cat(paste("Sum of weight: ",sum(getWeights(object)),"\n", sep=""))
			}
			for (node in nodes(object)) {
				cat(paste(node, " (",ifelse(unlist(nodeData(object, node, "rejected")),"rejected","not rejected"),", weight=",format(unlist(nodeData(object, node, "nodeWeight")), digits=4, drop0trailing=TRUE),")\n", sep=""))	
			}
			if (length(unlist(edges(object)))==0) {
				cat("No edges.\n")
			} else {
				cat("Edges:\n")
				from <- rep(names(edges(object)), unlist(lapply(edges(object),length)))
				to <- unlist(edges(object))
				for (i in 1:length(from)) {
					cat(paste(from[i], " -(", getWeightStr(object, from[i], to[i]),")-> ", to[i], "\n"))	
				}
				
			}
			#cat(paste("\nalpha=",paste(format(getWeights(object), digits=4 ,drop0trailing=TRUE),collapse="+"),"=",sum(getWeights(object)),"\n", sep=""))
			cat("\n")
		}
)

getWeightStr <- function(graph, from, to, LaTeX=FALSE) {	
	weight <- unlist(edgeData(graph, from, to, "weight"))
	p <- unlist(edgeData(graph, from, to, "epsilon"))
	attributes(p) <- NULL # Always do this when using all.equal	
	pStr <- ""
	if (LaTeX) {
		frac <- getLaTeXFraction
		e <- "\\epsilon"
	} else {
		frac <- function(x) {as.character(fractions(x))}
		e <- "\\epsilon"
	}
	for (i in 1:length(p)) {
		if (!isTRUE(all.equal(p[i], 0))) {
			if (p[i]>=0) {
				pStr <- paste(pStr, "+", sep="")
			}
			if (!isTRUE(all.equal(p[i], 1))) {
				if (!isTRUE(all.equal(p[i], -1))) {
					pStr <- paste(pStr, frac(p[i]), "*", e, sep="")
				} else {
					pStr <- paste(pStr, "-", e, sep="")
				}
			} else {
				pStr <- paste(pStr, e, sep="")
			}
			if (i>1) {
				pStr <- paste(pStr, "^", i, sep="")
			}
		}
	}
	if (is.nan(weight)) {
		return(paste(unlist(edgeData(graph, from, to, "variableWeight")), pStr, sep=""))
	}
	if (weight==0 && pStr!="") { # Remove the first "+" and just return the epsilon part:
		return(substring(pStr, 2))
	}
	return(paste(frac(weight), pStr, sep=""))	
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
