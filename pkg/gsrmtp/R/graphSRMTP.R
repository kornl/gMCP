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
			defaultProps <- list(alpha=0, rejected="false")
			nodeAttrData <- new("attrData", defaults=defaultProps)
			attrDataItem(nodeAttrData, x=nodes, attr="alpha") <- alpha
			.Object@nodeData <- nodeAttrData
			#.Object@alpha <- alpha
			validObject(.Object)
			return(.Object)
		})

getAlpha <- function(graph) {
	alpha <- unlist(nodeData(graph, nodes(graph), "alpha"))
	names(alpha) <- nodes(graph)
	return(alpha)
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
			cat(paste("alpha=",sum(getAlpha(x)),"\n", sep=""))			
		})

setMethod("plot", "graphSRMTP",
		function(x, y, ...) {
			# TODO Show visualization of graph			
		})


