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
