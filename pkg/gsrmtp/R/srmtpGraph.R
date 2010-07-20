## Graph representation in GSRMTP 
setClass("graphSRMTP",
		contains="graphNEL",
		representation(alpha="numeric"),
		validity=function(object) validGraph(object))

setMethod("initialize", "graphSRMTP",
		function(.Object, nodes=character(0), edgeL, alpha) {
			.Object <- callNextMethod(.Object, nodes, edgeL, edgemode="directed")
			if (length(alpha)) {			
				checkValidAlpha(alpha)
			}
			.Object@alpha <- alpha
			validObject(.Object)
			return(.Object)
		})

checkValidAlpha <- function(alpha) {
	if(any(0 > alpha | alpha > 1) | !(all.equal(sum(alpha), 1)==TRUE)) {
		stop("invalid alpha: alphas must be between 0 and 1 and sum up to 1")
	}
}





