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
			#.Object@alpha <- alpha
			validObject(.Object)
			return(.Object)
		})

getAlpha <- function(graph, node) {
	alpha <- unlist(nodeData(graph, nodes(graph), "alpha"))
	names(alpha) <- nodes(graph)
	if (!missing(node)) {
		return(alpha[node])
	}
	return(alpha)
}

getRejected <- function(graph) {
	rejected <- unlist(nodeData(graph, nodes(graph), "rejected"))
	names(rejected) <- nodes(graph)
	return(rejected)
}

isRejected <- function(graph, node) {
	rejected <- getRejected(graph)
	return(rejected[node])
}

checkValidAlpha <- function(alpha) {
	if(any(0 > alpha | alpha > 1)) {
		stop("invalid alpha: alphas must be between 0 and 1")
	}
	if(sum(alpha) >= 1) {
		stop("invalid alpha: the sum of all alphas must be less than 1")
	}
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
			callNextMethod(object)
			cat(paste("Overall alpha: ",sum(getAlpha(object)),"\n", sep=""))
			for (node in nodes(object)) {
				cat(paste(node, " (",ifelse(unlist(nodeData(object, node, "rejected")),"rejected","not rejected"),", alpha=",format(unlist(nodeData(object, node, "alpha")), digits=4 ,drop0trailing=TRUE),")\n", sep=""))	
			}
			cat("Edges:\n")
			cat(paste(paste(rep(names(edges(object)), unlist(lapply(edges(object),length))),
									" -(",format(unlist(edgeWeights(object)), digits=4 ,drop0trailing=TRUE),")-> ",
									unlist(edges(object)),sep=""),collapse="\n"))
			#cat(paste("\nalpha=",paste(format(getAlpha(object), digits=4 ,drop0trailing=TRUE),collapse="+"),"=",sum(getAlpha(object)),"\n", sep=""))
			cat("\n")
		}
)


setMethod("plot", "graphSRMTP",
		function(x, y, ...) {
			# TODO Show visualization of graph			
		})


