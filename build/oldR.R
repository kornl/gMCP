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