\name{graphMCP-class}
\docType{class}
\alias{graphMCP-class}
\alias{graphMCP}
\alias{print,graphMCP-method}
\alias{plot,graphMCP-method}
\alias{getWeights}
\alias{getWeights,graphMCP-method}
\alias{setWeights}
\alias{setWeights,graphMCP-method}
\alias{getRejected}
\alias{getRejected,graphMCP-method}
\alias{getXCoordinates}
\alias{getXCoordinates,graphMCP-method}
\alias{getYCoordinates}
\alias{getYCoordinates,graphMCP-method}
\alias{setEdge}
\alias{setEdge,character,character,graphMCP,character-method}
\alias{setEdge,character,character,graphMCP,numeric-method}
\alias{nodes}
\alias{nodes,graphMCP-method}
\alias{edgeAttr}
\alias{edgeAttr<-}
\alias{edgeAttr,graphMCP,character,character,character-method}
\alias{edgeAttr<-,graphMCP,character,character,character-method}
\alias{nodeAttr}
\alias{nodeAttr<-}
\alias{nodeAttr,graphMCP,character,character-method}
\alias{nodeAttr<-,graphMCP,character,character-method}
\title{Class graphMCP}
\description{A graphMCP object describes a sequentially rejective multiple test procedure.}
\section{Slots}{  
  \describe{    
    \item{\code{m}:}{A matrix.}
    \item{\code{weights}:}{A numeric.}        
    \item{\code{edgeAttr}:}{A list for edge attributes.}
    \item{\code{nodeAttr}:}{A list for node attributes.}
  }
}
\section{Extends}{
Class \code{graphNEL}, directly.
Class \code{graph}, by class \code{graphNEL}, distance 2.
}
\section{Methods}{
  \describe{
    \item{getWeights}{\code{signature(object = "graphMCP")}: A method for getting the weights.
      If a third optional argument \code{node} is specified, only for these nodes the weight will be returned.}
    \item{setWeights}{\code{signature(object = "graphMCP")}: A method for setting the weights.
      If a third optional argument \code{node} is specified, only for these nodes the weight will be set.}
    \item{getRejected}{\code{signature(object = "graphMCP")}: 
      A method for getting the information whether the hypotheses are marked in the graph as already rejected.
      If a second optional argument \code{node} is specified, only for these nodes the boolean vector will be returned.}
    \item{getXCoordinates}{\code{signature(object = "graphMCP")}: 
      A method for getting the x coordinates of the graph.
      If a second optional argument \code{node} is specified, only for these nodes the x coordinates will be returned.}
    \item{getYCoordinates}{\code{signature(object = "graphMCP")}: 
      A method for getting the y coordinates of the graph
      If a second optional argument \code{node} is specified, only for these nodes the x coordinates will be returned.}
    \item{setEdge}{\code{signature(from="character", to="character", graph="graphNEL", weights="numeric")}: 
      A method for adding new edges with the given weights.}
    \item{setEdge}{\code{signature(from="character", to="character", graph="graphMCP", weights="character")}: 
      A method for adding new edges with the given weights.}
  }
}
\author{Kornelius Rohmeyer \email{rohmeyer@small-projects.de}}
\seealso{
\code{\link[graph:graphNEL-class]{graphNEL}}
}
\examples{

m <- rbind(H11=c(0,   0.5, 0,   0.5, 0,   0  ),
			H21=c(1/3, 0,   1/3, 0,   1/3, 0  ),
			H31=c(0,   0.5, 0,   0,   0,   0.5),
			H12=c(0,   1,   0,   0,   0,   0  ),
			H22=c(0.5, 0,   0.5, 0,   0,   0  ),
			H32=c(0,   1,   0,   0,   0,   0  ))	

weights <- c(1/3, 1/3, 1/3, 0, 0, 0)

# Graph creation
graph <- new("graphMCP", m=m, weights=weights)

# Visualization settings
nodeX <- rep(c(100, 300, 500), 2)
nodeY <- rep(c(100, 300), each=3)
graph@nodeAttr$X <- nodeX
graph@nodeAttr$Y <- nodeY	

getWeights(graph)

getRejected(graph)

pvalues <- c(0.1, 0.008, 0.005, 0.15, 0.04, 0.006)
result <- gMCP(graph, pvalues)

getWeights(result@graphs[[4]])
getRejected(result@graphs[[4]])

}
\keyword{ graphs }