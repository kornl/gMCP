## Graph representation in GSRMTP 
setClass("graphSRMTP",
		contains="graphNEL",
		#representation(alpha="numeric"),
		validity=function(object) validGraph(object))


setMethod("initialize", "graphSRMTP",
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

setClass("srmtpResult",		
		representation(graphs="list",
				pvalues="numeric",
				adjPValues="numeric")
)

setMethod("print", "srmtpResult",
		function(x, ...) {
			# callNextMethod(x, ...)
			cat("SRMTP-Result\n")
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

setMethod("plot", "srmtpResult",
		function(x, y, ...) {
			# TODO Show visualization of graph			
		})

setGeneric("getAlpha", function(object, node, ...) standardGeneric("getAlpha"))

setMethod("getAlpha", c("graphSRMTP"),
		function(object, node, ...) {
			alpha <- unlist(nodeData(object, nodes(object), "alpha"))
			names(alpha) <- nodes(object)
			if (!missing(node)) {
				return(alpha[node])
			}
			return(alpha)
		})

setMethod("getAlpha", c("srmtpResult"),
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

setMethod("getRejected", c("graphSRMTP"), function(object, node, ...) {
			rejected <- unlist(nodeData(object, nodes(object), "rejected"))
			names(rejected) <- nodes(object)
			if (!missing(node)) {
				return(rejected[node])
			}
			return(rejected)
		})

setMethod("getRejected", c("srmtpResult"), function(object, node, ...) {
			object <-  object@graphs[[length(object@graphs)]]
			rejected <- unlist(nodeData(object, nodes(object), "rejected"))
			names(rejected) <- nodes(object)
			if (!missing(node)) {
				return(rejected[node])
			}
			return(rejected)
		})

setGeneric("getX", function(graph, node) standardGeneric("getX"))

setMethod("getX", c("graphSRMTP"), function(graph, node) {
			x <- nodeRenderInfo(graph)$nodeX
			names(x) <- nodes(graph)
			if (!missing(node)) {
				return(x[node])
			}
			return(x)
		})

setGeneric("getY", function(graph, node) standardGeneric("getY"))

setMethod("getY", c("graphSRMTP"), function(graph, node) {
			y <- nodeRenderInfo(graph)$nodeY
			names(y) <- nodes(graph)
			if (!missing(node)) {
				return(y[node])
			}
			return(y)
		})

canBeRejected <- function(graph, node, pvalues) {	
	return(getAlpha(graph)[[node]]>pvalues[[node]] | (all.equal(getAlpha(graph)[[node]],pvalues[[node]])[1]==TRUE));
}

setMethod("print", "graphSRMTP",
		function(x, ...) {
			callNextMethod(x, ...)
			#for (node in nodes(x)) {
			#	cat(paste(node, " (",ifelse(unlist(nodeData(x, node, "rejected")),"rejected","not rejected"),", alpha=",format(unlist(nodeData(x, node, "alpha")), digits=4 ,drop0trailing=TRUE),")\n", sep=""))	
			#}
			#cat(paste("alpha=",paste(format(getAlpha(x), digits=4 ,drop0trailing=TRUE),collapse="+"),"=",sum(getAlpha(x)),"\n", sep=""))			
		})

setMethod("show", "graphSRMTP",
		function(object) {
			#callNextMethod(object)
			cat("A graphSRMTP graph\n")
			cat(paste("Overall alpha: ",sum(getAlpha(object)),"\n", sep=""))
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
			#cat(paste("\nalpha=",paste(format(getAlpha(object), digits=4 ,drop0trailing=TRUE),collapse="+"),"=",sum(getAlpha(object)),"\n", sep=""))
			cat("\n")
		}
)


setMethod("plot", "graphSRMTP",
		function(x, y, ...) {
			# TODO Show visualization of graph			
		})

setGeneric("simConfint", function(object, pvalues, confintF) standardGeneric("simConfint"))

setMethod("simConfint", c("graphSRMTP"), function(object, pvalues, confintF) {			
			result <- srmtp(object, pvalues)
			if (all(getRejected(result))) {
				m <- mapply(confintF, nodes(object), getAlpha(object)) 
			} else {
				m <- mapply(confintF, nodes(object), getAlpha(result))				
			}
			alpha <- sum(getAlpha(result))*100
			rownames(m) <- paste(c(alpha/2,100-alpha/2),"%",sep="")# TODO These will be labelled as (1-level)/2 and 1 - (1-level)/2 in \% (by default 2.5\% and 97.5%)
			return(t(m))
		})

