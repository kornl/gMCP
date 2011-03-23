\name{graphMCP-class}
\docType{class}
\alias{graphMCP-class}
\alias{graphMCP}
\alias{print,graphMCP-method}
\alias{plot,graphMCP-method}
\alias{getWeights}
\alias{getWeights,graphMCP-method}
\alias{setAlpha}
\alias{setAlpha,graphMCP-method}
\alias{getRejected}
\alias{getRejected,graphMCP-method}
\alias{getXCoordinates}
\alias{getXCoordinates,graphMCP-method}
\alias{getYCoordinates}
\alias{getYCoordinates,graphMCP-method}
\alias{addEdge}
\alias{addEdge,graphMCP-method}
\title{Class graphMCP}
\description{A graphMCP object describes a sequentially rejective multiple test procedure.}
\section{Slots}{  
  \describe{
    %\item{\code{alpha}:}{A numeric for the initial allocation of the significance level alpha to the individual hypotheses.}
    \item{\code{nodes}:}{Object of class \code{vector}}
    \item{\code{edgeL}:}{Object of class \code{list}. The \code{edgeL}
      must be the same length as \code{nodes}. The elements of this
      vector correspond to the same element in \code{nodes}. The
      elements are themselves lists. If the node has any edges then this
      list will have an element named \code{edges}.}        
    \item{\code{edgeData}:}{An \code{attrData} instance for edge attributes.}
    \item{\code{nodeData}:}{An \code{attrData} instance for node attributes.}
    \item{\code{graphData}:}{A \code{list} for graph-level
      attributes. Only mandatory list item is \code{edgemode} which
      indicates whether edges are \code{"directed"} or
      \code{"undirected"}}
    \item{\code{renderInfo}:}{A \code{list} of graph rendering information.}
  }
}
\section{Extends}{
Class \code{graphNEL}, directly.
Class \code{graph}, by class \code{graphNEL}, distance 2.
}
\section{Methods}{
  \describe{
    \item{getWeights}{\code{signature(object = "graphMCP")}: A method for
      getting the allocation of the significance level alpha.
      If a second optional argument \code{node} is specified, only for these nodes the alpha level will be returned.}
    \item{setAlpha}{\code{signature(object = "graphMCP")}: A method for
      setting the allocation of the significance level alpha.
      If a second optional argument \code{node} is specified, only for these nodes the alpha level will be set.}
    \item{getRejected}{\code{signature(object = "graphMCP")}: A method for
      getting the information whether the hypotheses are marked in the graph as already rejected.
      If a second optional argument \code{node} is specified, only for these nodes the boolean vector will be returned.}
    \item{getXCoordinates}{\code{signature(object = "graphMCP")}: A method for
      getting the x coordinates of the graph.
      If a second optional argument \code{node} is specified, only for these nodes the x coordinates will be returned.}
    \item{getYCoordinates}{\code{signature(object = "graphMCP")}: A method for
      getting the y coordinates of the graph
      If a second optional argument \code{node} is specified, only for these nodes the x coordinates will be returned.}
    \item{addEdge}{\code{signature(from="character", to="character", graph="graphNEL", weights="numeric")}: A method for
      adding new edges with the given weights.}
  }
}
\author{Kornelius Rohmeyer \email{rohmeyer@small-projects.de}}
\seealso{
\code{\link[graph:graphNEL-class]{graphNEL}}
}
\examples{

hnodes <- c("H11", "H21", "H31", "H12", "H22", "H32")
alpha <- c(1/3, 1/3, 1/3, 0, 0, 0)
edges <- list()
edges[["H11"]] <- list(edges=c("H21","H12"), weights=c(1/2, 1/2))
edges[["H21"]] <- list(edges=c("H11","H31","H22"), weights=c(1/3, 1/3, 1/3))
edges[["H31"]] <- list(edges=c("H21","H32"), weights=c(1/2, 1/2))
edges[["H12"]] <- list(edges="H21", weights=1)
edges[["H22"]] <- list(edges=c("H11","H31"), weights=c(1/2, 1/2))
edges[["H32"]] <- list(edges="H21", weights=1)
graph <- new("graphMCP", nodes=hnodes, edgeL=edges, alpha=alpha)

nodeX <- c(H11=10, H21=20, H31=30, H12=10, H22=20, H32=30)
nodeY <- c(H11=20, H21=20, H31=20, H12=10, H22=10, H32=10)
nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)	

getWeights(graph)

getRejected(graph)

pvalues <- c(0.1, 0.008, 0.005, 0.15, 0.04, 0.006)
result <- gMCP(graph, pvalues)

getWeights(result@graphs[[4]])
getRejected(result@graphs[[4]])

}
\keyword{ graphs }