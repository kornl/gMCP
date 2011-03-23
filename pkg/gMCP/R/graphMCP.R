## Graph representation in gMCP
setClass("graphMCP",
		contains="graphNEL",
		#representation(alpha="numeric"),
		validity=function(object) validGraph(object))


setMethod("initialize", "graphMCP",
		function(.Object, nodes=character(0), edgeL, alpha) {
			.Object <- callNextMethod(.Object, nodes, edgeL, edgemode="directed")
			if (length(alpha)) {			
				checkValidAlpha(alpha)
			}
			defaultProps <- list(alpha=0, rejected=FALSE)
			nodeAttrData <- new("attrData", defaults=defaultProps)
			attrDataItem(nodeAttrData, x=nodes, attr="alpha") <- alpha
			.Object@nodeData <- nodeAttrData
			edgeDataDefaults(.Object, "labelX") <- -100
			edgeDataDefaults(.Object, "labelY") <- -100
			#.Object@alpha <- alpha
			validObject(.Object)
			return(.Object)
		})

setClass("gMCPResult",		
		representation(graphs="list",
				pvalues="numeric",
				rejected="logical",
				adjPValues="numeric")
)

setMethod("print", "gMCPResult",
		function(x, ...) {
			# callNextMethod(x, ...)
			cat("gMCP-Result\n")
			cat("\nP-values:\n")
			print(x@pvalues)
			cat("\nInitial graph:\n")
			print(x@graphs[[1]])
			if (length(x@graphs)==1) {
				cat("No hypotheses could be rejected.")
				return()
			}
			cat("\nFinal graph after", length(x@graphs)-1 ,"steps:\n")
			print(x@graphs[[length(x@graphs)]])
		})

setMethod("plot", "gMCPResult",
		function(x, y, ...) {
			# TODO Show visualization of graph			
		})

setGeneric("getWeights", function(object, node, ...) standardGeneric("getWeights"))

setMethod("getWeights", c("graphMCP"),
		function(object, node, ...) {
			alpha <- unlist(nodeData(object, nodes(object), "alpha"))
			names(alpha) <- nodes(object)
			if (!missing(node)) {
				return(alpha[node])
			}
			return(alpha)
		})

setGeneric("setAlpha", function(object, node, alpha, ...) standardGeneric("setAlpha"))

setMethod("setAlpha", c("graphMCP"),
		function(object, node, alpha, ...) {
			if (missing(node)) {
				node <- nodes(object)
			}
			nodeData(object, nodes(object), "alpha") <- alpha			
			return(object)
		})

setMethod("getWeights", c("gMCPResult"),
		function(object, node, ...) {
			graph <- object@graphs[[length(object@graphs)]]
			alpha <- unlist(nodeData(graph, nodes(graph), "alpha"))
			names(alpha) <- nodes(graph)
			if (!missing(node)) {
				return(alpha[node])
			}
			return(alpha)
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

setGeneric("getX", function(graph, node) standardGeneric("getX"))

setMethod("getX", c("graphMCP"), function(graph, node) {
			x <- nodeRenderInfo(graph)$nodeX
			names(x) <- nodes(graph)
			if (!missing(node)) {
				return(x[node])
			}
			return(x)
		})

setGeneric("getY", function(graph, node) standardGeneric("getY"))

setMethod("getY", c("graphMCP"), function(graph, node) {
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
			#	cat(paste(node, " (",ifelse(unlist(nodeData(x, node, "rejected")),"rejected","not rejected"),", alpha=",format(unlist(nodeData(x, node, "alpha")), digits=4 ,drop0trailing=TRUE),")\n", sep=""))	
			#}
			#cat(paste("alpha=",paste(format(getWeights(x), digits=4 ,drop0trailing=TRUE),collapse="+"),"=",sum(getWeights(x)),"\n", sep=""))			
		})

setMethod("show", "graphMCP",
		function(object) {
			#callNextMethod(object)
			cat("A graphMCP graph\n")
			cat(paste("Overall alpha: ",sum(getWeights(object)),"\n", sep=""))
			for (node in nodes(object)) {
				cat(paste(node, " (",ifelse(unlist(nodeData(object, node, "rejected")),"rejected","not rejected"),", alpha=",format(unlist(nodeData(object, node, "alpha")), digits=4 ,drop0trailing=TRUE),")\n", sep=""))	
			}
			if (length(unlist(edges(object)))==0) {
				cat("No edges.\n")
			} else {
				cat("Edges:\n")
				cat(paste(paste(rep(names(edges(object)), unlist(lapply(edges(object),length))),
										" -(",format(unlist(edgeWeights(object)), digits=4 ,drop0trailing=TRUE),")-> ",
										unlist(edges(object)),sep=""),collapse="\n"))
			}
			#cat(paste("\nalpha=",paste(format(getWeights(object), digits=4 ,drop0trailing=TRUE),collapse="+"),"=",sum(getWeights(object)),"\n", sep=""))
			cat("\n")
		}
)

setMethod("plot", "graphMCP",
		function(x, y, ...) {
			# TODO Show visualization of graph			
		})

setGeneric("simConfint", function(object, pvalues, confint, alternative=c("less", "greater"), estimates, df) standardGeneric("simConfint"))

setMethod("simConfint", c("graphMCP"), function(object, pvalues, confint, alternative=c("less", "greater"), estimates, df) {
			result <- gMCP(object, pvalues)
			if (all(getRejected(result))) {
				alpha <- getWeights(object)				
			} else {
				alpha <- getWeights(result)				
			}
			if (class(confint)=="function") {
				m <- mapply(confint, nodes(object), alpha)					
				rownames(m) <- c("lower bound", "upper bound")
				return(t(m))
			} else if (confint=="t") {
				dist <- function(x) {qt(x, df)}
			} else if (confint=="normal") {
				dist <- qnorm
			} else {
				stop("Parameter confint has to be a function or \"t\" or \"normal\".")
			}
			var <- 1
			if (alternative=="greater") {			
				stderr <- estimates/dist(1-pvalues)
				dput(estimates)
				dput(alpha)
				dput(stderr)
				dput(dist(alpha))
				lb <- estimates+dist(alpha)*stderr				
				lb <- ifelse(getRejected(result), max(0,lb), lb) 
				ub <- rep(Inf,length(lb))
			} else if (alternative=="less") {			
				stderr <- estimates/qt(pvalues)
				lb <- rep(-Inf,length(lb))				 
				ub <- estimates+dist(1-alpha)*stderr
				ub <- ifelse(getRejected(result), min(0,ub), ub)
			} else {
				stop("Specify alternative as \"less\" or \"greater\".")
			}
			m <- matrix(c(lb, ub), ncol=2)
			colnames(m) <- c("lower bound", "upper bound")
			return(m)
		})

