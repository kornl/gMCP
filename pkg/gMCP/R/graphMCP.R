#' Class graphMCP
#' 
#' A graphMCP object describes a sequentially rejective multiple test
#' procedure.
#' 
#' 
#' @name graphMCP-class
#' @aliases graphMCP-class graphMCP print,graphMCP-method plot,graphMCP-method
#' getWeights getWeights,graphMCP-method getMatrix getMatrix,graphMCP-method
#' setWeights setWeights,graphMCP-method setRejected<-
#' setRejected<-,graphMCP-method getRejected getRejected,graphMCP-method
#' getXCoordinates getXCoordinates,graphMCP-method getYCoordinates
#' getYCoordinates,graphMCP-method setEdge
#' setEdge,character,character,graphMCP,character-method
#' setEdge,character,character,graphMCP,numeric-method getNodes
#' getNodes,graphMCP-method edgeAttr edgeAttr<-
#' edgeAttr,graphMCP,character,character,character-method
#' edgeAttr<-,graphMCP,character,character,character-method nodeAttr nodeAttr<-
#' nodeAttr,graphMCP,character,character-method
#' nodeAttr<-,graphMCP,character,character-method
#' @docType class
#' @section Slots: \describe{ \item{list("m")}{An adjacency matrix. Can be
#' either \code{numerical} or \code{character} depending whether the matrix
#' contains variables or not. Row and column names will be the names of the
#' nodes.}\item{:}{An adjacency matrix. Can be either \code{numerical} or
#' \code{character} depending whether the matrix contains variables or not. Row
#' and column names will be the names of the nodes.} \item{list("weights")}{A
#' numeric.}\item{:}{A numeric.} \item{list("edgeAttr")}{A list for edge
#' attributes.}\item{:}{A list for edge attributes.} \item{list("nodeAttr")}{A
#' list for node attributes.}\item{:}{A list for node attributes.} }
#' @author Kornelius Rohmeyer \email{rohmeyer@@small-projects.de}
#' @keywords graphs
#' @examples
#' 
#' 
#' m <- rbind(H11=c(0,   0.5, 0,   0.5, 0,   0  ),
#'   		H21=c(1/3, 0,   1/3, 0,   1/3, 0  ),
#' 			H31=c(0,   0.5, 0,   0,   0,   0.5),
#' 			H12=c(0,   1,   0,   0,   0,   0  ),
#' 			H22=c(0.5, 0,   0.5, 0,   0,   0  ),
#' 			H32=c(0,   1,   0,   0,   0,   0  ))	
#' 
#' weights <- c(1/3, 1/3, 1/3, 0, 0, 0)
#' 
#' # Graph creation
#' graph <- new("graphMCP", m=m, weights=weights)
#' 
#' # Visualization settings
#' nodeX <- rep(c(100, 300, 500), 2)
#' nodeY <- rep(c(100, 300), each=3)
#' graph@nodeAttr$X <- nodeX
#' graph@nodeAttr$Y <- nodeY	
#' 
#' getWeights(graph)
#' 
#' getRejected(graph)
#' 
#' pvalues <- c(0.1, 0.008, 0.005, 0.15, 0.04, 0.006)
#' result <- gMCP(graph, pvalues)
#' 
#' getWeights(result@graphs[[4]])
#' getRejected(result@graphs[[4]])
#' 
#' 
setClass("graphMCP",	
		representation(m="matrix", 
				weights="numeric", 
				nodeAttr="list", 
				edgeAttr="list"),
		validity=function(object) validWeightedGraph(object))

setMethod("initialize", "graphMCP",
		function(.Object, m, weights, nodeAttr=list(), edgeAttr=list()) {			
			if (length(weights)) {			
				checkValidWeights(weights)
			}			
			colnames(m) <- rownames(m)
			.Object@m <- m
			names(weights) <- rownames(m)
			.Object@weights <- weights
			.Object@nodeAttr <- nodeAttr
			.Object@edgeAttr <- edgeAttr
			if(is.null(.Object@nodeAttr$rejected)) {
				.Object@nodeAttr$rejected <- rep(FALSE, dim(m)[1])
				names(.Object@nodeAttr$rejected) <- rownames(m)
			}
			validObject(.Object)
			return(.Object)
		})

validWeightedGraph <- function(object) {
	# if (sum(object@weights)>1)
	return(TRUE)
}

#' Class gMCPResult
#' 
#' A gMCPResult object describes an evaluated sequentially rejective multiple
#' test procedure.
#' 
#' 
#' @name gMCPResult-class
#' @aliases gMCPResult-class gMCPResult print,gMCPResult-method
#' plot,gMCPResult-method getWeights,gMCPResult-method
#' getRejected,gMCPResult-method
#' @docType class
#' @section Slots: \describe{ \item{list("graphs")}{Object of class
#' \code{list}. }\item{:}{Object of class \code{list}. } \item{list("alpha")}{A
#' \code{numeric} specifying the maximal type I error rate.}\item{:}{A
#' \code{numeric} specifying the maximal type I error rate.}
#' \item{list("pvalues")}{The \code{numeric} vector of pvalues.}\item{:}{The
#' \code{numeric} vector of pvalues.} \item{list("rejected")}{The
#' \code{logical} vector of rejected null hypotheses.}\item{:}{The
#' \code{logical} vector of rejected null hypotheses.}
#' \item{list("adjPValues")}{The \code{numeric} vector of adjusted
#' pvalues.}\item{:}{The \code{numeric} vector of adjusted pvalues.} }
#' @author Kornelius Rohmeyer \email{rohmeyer@@small-projects.de}
#' @seealso \code{\link{gMCP}}
#' @keywords graphs
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
			#for (node in getNodes(x)) {
			#	cat(paste(node, " (",ifelse(unlist(nodeAttr(x, node, "rejected")),"rejected","not rejected"),", alpha=",format(unlist(nodeAttr(x, node, "nodeWeight")), digits=4 ,drop0trailing=TRUE),")\n", sep=""))	
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

setGeneric("getNodes", function(object, ...) standardGeneric("getNodes"))

setMethod("getNodes", c("graphMCP"),
		function(object, ...) {			
			return(rownames(object@m))
		})

setGeneric("getMatrix", function(object, ...) standardGeneric("getMatrix"))

setMethod("getMatrix", c("graphMCP"),
		function(object, ...) {
			m <- object@m
			return(m)
		})

setGeneric("getWeights", function(object, node, ...) standardGeneric("getWeights"))

setMethod("getWeights", c("graphMCP"),
		function(object, node, ...) {
			weights <- object@weights
			names(weights) <- getNodes(object)
			if (!missing(node)) {
				return(weights[node])
			}
			return(weights)
		})

setGeneric("setWeights", function(object, weights, node, ...) standardGeneric("setWeights"))

setMethod("setWeights", c("graphMCP"),
		function(object, weights, node, ...) {
			if (missing(node)) {
				node <- getNodes(object)
			}
			object@weights[node] <- weights			
			return(object)
		})

setMethod("getWeights", c("gMCPResult"),
		function(object, node, ...) {
			graph <- object@graphs[[length(object@graphs)]]			
			return(getWeights(graph, node))
		})

setGeneric("setEdge", function(from, to, graph, weights) standardGeneric("setEdge"))

setMethod("setEdge", signature=signature(from="character", to="character",
				graph="graphMCP", weights="character"),
		function(from, to, graph, weights) {
			graph@m[from, to] <- weights
			graph
		})

setMethod("setEdge", signature=signature(from="character", to="character",
				graph="graphMCP", weights="numeric"),
		function(from, to, graph, weights) {
			graph@m[from, to] <- weights
			graph
		})

setGeneric("edgeAttr", function(self, from, to, attr) standardGeneric("edgeAttr"))
setGeneric("edgeAttr<-", function(self, from, to, attr, value) standardGeneric("edgeAttr<-"))

setMethod("edgeAttr", signature(self="graphMCP", from="character", to="character",
				attr="character"),
		function(self, from, to, attr) {
			self@edgeAttr[[attr]][from, to]
		})

setReplaceMethod("edgeAttr",
		signature(self="graphMCP", from="character", to="character", attr="character", value="ANY"),
		function(self, from, to, attr, value) {
			if (is.null(self@edgeAttr[[attr]])) self@edgeAttr[[attr]] <- matrix(NA, nrow=dim(self@m)[1], ncol=dim(self@m)[2])			
			rownames(self@edgeAttr[[attr]]) <- colnames(self@edgeAttr[[attr]]) <- getNodes(self)
			self@edgeAttr[[attr]][from, to] <- value		
			self
		})

setGeneric("nodeAttr", function(self, n, attr) standardGeneric("nodeAttr"))
setGeneric("nodeAttr<-", function(self, n, attr, value) standardGeneric("nodeAttr<-"))

setMethod("nodeAttr", signature(self="graphMCP", n="character", attr="character"),
		function(self, n, attr) {
			self@nodeAttr[[attr]][n]
		})

setReplaceMethod("nodeAttr",
		signature(self="graphMCP", n="character", attr="character", value="ANY"),
		function(self, n, attr, value) {
			if (is.null(self@nodeAttr[[attr]])) self@nodeAttr[[attr]] <- logical(length=length(getNodes(self)))
			self@nodeAttr[[attr]][n] <- value			
			self
		})

setGeneric("getRejected", function(object, node, ...) standardGeneric("getRejected"))

setMethod("getRejected", c("graphMCP"), function(object, node, ...) {
			rejected <- object@nodeAttr$rejected
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

setGeneric("setRejected", function(object, node, value) standardGeneric("setRejected"))
setGeneric("setRejected<-", function(object, node, value) standardGeneric("setRejected<-"))

setMethod("setRejected", c("graphMCP"),
		function(object, node, value) {
			if (missing(node)) {
				node <- getNodes(object)
			}
			object@nodeAttr$rejected[node] <- value			
			return(object)
		})

setReplaceMethod("setRejected", c("graphMCP"),
		function(object, node, value) {
			if (missing(node)) {
				node <- getNodes(object)
			}
			object@nodeAttr$rejected[node] <- value			
			return(object)
		})

setGeneric("getXCoordinates", function(graph, node) standardGeneric("getXCoordinates"))

setMethod("getXCoordinates", c("graphMCP"), function(graph, node) {
			x <- graph@nodeAttr$X
			if (is.null(x)) return(x)
			names(x) <- getNodes(graph)
			if (!missing(node)) {
				return(x[node])
			}
			return(x)
		})

setGeneric("getYCoordinates", function(graph, node) standardGeneric("getYCoordinates"))

setMethod("getYCoordinates", c("graphMCP"), function(graph, node) {
			y <- graph@nodeAttr$Y
			if (is.null(y)) return(y)
			names(y) <- getNodes(graph)
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
			#for (node in getNodes(x)) {
			#	cat(paste(node, " (",ifelse(unlist(nodeAttr(x, node, "rejected")),"rejected","not rejected"),", alpha=",format(unlist(nodeAttr(x, node, "nodeWeight")), digits=4 ,drop0trailing=TRUE),")\n", sep=""))	
			#}
			#cat(paste("alpha=",paste(format(getWeights(x), digits=4 ,drop0trailing=TRUE),collapse="+"),"=",sum(getWeights(x)),"\n", sep=""))			
		})

setMethod("show", "graphMCP",
		function(object) {
			#callNextMethod(object)
			nn <- getNodes(object)
			cat("A graphMCP graph\n")
			if (!isTRUE(all.equal(sum(getWeights(object)),1))) {
				cat(paste("Sum of weight: ",sum(getWeights(object)),"\n", sep=""))
			}			
			for (node in getNodes(object)) {
				cat(paste(node, " (",ifelse(nodeAttr(object, node, "rejected"),"rejected, ",""),"weight=",format(object@weights[node], digits=4, drop0trailing=TRUE),")\n", sep=""))
			}
			printEdge <- FALSE;
			for (i in getNodes(object)) {
				for (j in getNodes(object)) {
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
	weight <- graph@m[from,to]	
	if (LaTeX) {
		if (is.numeric(weight)) {
			return(getLaTeXFraction(weight))
		}
		# TODO / Bonus: Parse fractions in polynomials
		return(weight)
	}	
	if (is.numeric(weight)) {
		return(getFractionString(weight))
	}
	return(as.character(weight))	
}

getFractionString <- function(x, eps=1e-07) {
	xStr <- as.character(fractions(x))
	xStr <- ifelse(abs(sapply(xStr, function(x) {eval(parse(text=x))})-x)>eps, as.character(x), xStr)
	return(xStr)
}

setMethod("plot", "graphMCP",
		function(x, y, ...) {
			# TODO Show visualization of graph			
		})

setGeneric("simConfint", function(object, pvalues, confint, alternative=c("less", "greater"), estimates, df, alpha=0.05, mu=0) standardGeneric("simConfint"))


#' Simultaneous confidence intervals for sequentially rejective multiple test
#' procedures
#' 
#' Calculates simultaneous confidence intervals for sequentially rejective
#' multiple test procedures.
#' 
#' For details see the given references.
#' 
#' @aliases simConfint simConfint,graphMCP-method
#' @param object A graph of class \code{\link{graphMCP}}.
#' @param pvalues A numeric vector specifying the p-values for the sequentially
#' rejective MTP.
#' @param confint One of the following: A character string "normal", "t" or a
#' function that calculates the confidence intervals.  If confintF=="t" the
#' parameter "df" must be specified. If confint is a function it must be of
#' signature \code{("character", "numeric")}, where the first parameter is the
#' hypothesis name and the second the marginal confidence level. (See examples)
#' @param alternative A character string specifying the alternative hypothesis,
#' must be "greater" or "less".
#' @param estimates Point estimates for the parameters of interest.
#' @param df Degree of freedom as numeric.
#' @param alpha The overall alpha level as numeric scalar.
#' @param mu The numerical parameter vector under null hypothesis.
#' @return A matrix with columns giving lower confidence limits, point
#' estimates and upper confidence limits for each parameter. These will be
#' labeled as "lower bound", "estimate" and "upper bound". %(1-level)/2 and 1 -
#' (1-level)/2 in \% (by default 2.5\% and 97.5\%).
#' @author Kornelius Rohmeyer \email{rohmeyer@@small-projects.de}
#' @seealso \code{\link{graphMCP}}
#' @references Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch: A
#' graphical approach to sequentially rejective multiple test procedures.
#' Statistics in Medicine 2009 vol. 28 issue 4 page 586-604.
#' \url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}
#' @keywords htest graphs
#' @examples
#' 
#' 
#' est <- c("H1"=0.860382, "H2"=0.9161474, "H3"=0.9732953)
#' # Sample standard deviations:
#' ssd <- c("H1"=0.8759528, "H2"=1.291310, "H3"=0.8570892)
#' 
#' pval <- c(0.01260, 0.05154, 0.02124)/2
#' 
#' simConfint(BonferroniHolm(3), pvalues=pval, 
#'   	confint=function(node, alpha) {
#' 			c(est[node]-qt(1-alpha,df=9)*ssd[node]/sqrt(10), Inf)
#' 		}, estimates=est, alpha=0.025, mu=0, alternative="greater")
#' 
#' # Note that the sample standard deviations in the following call
#' # will be calculated from the pvalues and estimates.
#' ci <- simConfint(BonferroniHolm(3), pvalues=pval, 
#' 		confint="t", df=9, estimates=est, alpha=0.025, alternative="greater")
#' ci
#' 	
#' plotSimCI(ci)
#' 
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
				m <- mapply(f, getNodes(object), alpha, getRejected(result))	
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


setClass("gPADInterim",
		representation(Aj="matrix",
				BJ="numeric",
				z1="numeric",
				v="numeric",
				preplanned="graphMCP",
				alpha="numeric"),
		validity=function(object) validPartialCEs(object))

setMethod("print", "gPADInterim",
		function(x, ...) {
			callNextMethod(x, ...)
			
		})


setMethod("show","gPADInterim",
		function(object) {
			cat("Pre-planned graphical MCP at level:",object@alpha,"\n")
			show(object@preplanned)
			n <- length(object@z1)
			cat("Proportion of pre-planned measurements\n collected up to interim:\n")
			v <- object@v
                        if(length(v) == 1) v <- rep(v,n)
			names(v) <- paste('H',1:n,sep='')
                        print(v)
			cat("Z-scores computed at interim\n")
			z1 <- object@z1
			names(z1) <- paste('H',1:n,sep='')
			print(z1)
			cat("\n Interim PCE's by intersection\n")
			tab <- round(cbind(object@Aj,object@BJ),3)
			rownames(tab) <- to.intersection(1:nrow(tab))
			colnames(tab) <- c(paste('A(',1:n,')',sep=''),'BJ')
			print(tab)
		}
)

############################## Entangled graphs #################################

#' Class entangledMCP
#' 
#' A entangledMCP object describes ... TODO
#' 
#' 
#' @name entangledMCP-class
#' @aliases entangledMCP-class entangledMCP print,entangledMCP-method
#' getWeights,entangledMCP-method getMatrices getMatrices,entangledMCP-method
#' getRejected,entangledMCP-method getXCoordinates,entangledMCP-method
#' getYCoordinates,entangledMCP-method getNodes,entangledMCP-method
#' @docType class
#' @section Slots: \describe{ \item{list("subgraphs")}{A list of graphs of
#' class graphMCP.}\item{:}{A list of graphs of class graphMCP.}
#' \item{list("weights")}{A numeric.}\item{:}{A numeric.}
#' \item{list("graphAttr")}{A list for graph attributes like color,
#' etc.}\item{:}{A list for graph attributes like color, etc.} }
#' @author Kornelius Rohmeyer \email{rohmeyer@@small-projects.de}
#' @seealso \code{\link[gMCP:graphMCP-class]{graphMCP}}
#' @keywords graphs
#' @examples
#' 
#' 
#' g1 <- BonferroniHolm(2)
#' g2 <- BonferroniHolm(2)
#' 
#' graph <- new("entangledMCP", subgraphs=list(g1,g2), weights=c(0.5,0.5))
#' 
#' getMatrices(graph)
#' getWeights(graph)
#' 
setClass("entangledMCP",	
		representation(subgraphs="list", 
				weights="numeric",
				graphAttr="list"),
		validity=function(object) validEntangledGraph(object))


validEntangledGraph <- function(graph) {
	if (!all("graphMCP" == lapply(graph@subgraphs, class))) stop("Subgraphs need to be of class 'graphMCP'.")
	return(TRUE)
}

setGeneric("getMatrices", function(object, ...) standardGeneric("getMatrices"))

setMethod("getMatrices", c("entangledMCP"),
		function(object, ...) {
			result <- list()
			for (g in object@subgraphs) {
				result[[length(result)+1]] <- g@m
			}
			return(result)
		})

setMethod("getWeights", c("entangledMCP"),
		function(object, node, ...) {
			result <- c()
			for (g in object@subgraphs) {
				result <- rbind(result, getWeights(g, node, ...))
			}
			return(result)
		})

setMethod("getNodes", c("entangledMCP"),
		function(object, ...) {			
			return(getNodes(object@subgraphs[[1]]))
		})

setMethod("getXCoordinates", c("entangledMCP"), function(graph, node) {			
			return(getXCoordinates(graph@subgraphs[[1]], node))
		})

setMethod("getYCoordinates", c("entangledMCP"), function(graph, node) {
			return(getYCoordinates(graph@subgraphs[[1]], node))
		})

setMethod("getRejected", c("entangledMCP"), function(object, node, ...) {
			return(getRejected(object@subgraphs[[1]], node))
		})

