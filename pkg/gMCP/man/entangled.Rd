\name{entangledMCP-class}
\docType{class}
\alias{entangledMCP-class}
\alias{entangledMCP}
\alias{print,entangledMCP-method}
\alias{getWeights,entangledMCP-method}
\alias{getMatrices}
\alias{getMatrices,entangledMCP-method}
\title{Class entangledMCP}
\description{A entangledMCP object describes ... TODO}
\section{Slots}{  
  \describe{    
    \item{\code{graphs}:}{A list of graphs of class graphMCP.}
    \item{\code{weights}:}{A numeric.}
    \item{\code{graphAttr}:}{A list for graph attributes like color, etc.}
  }
}
\section{Methods}{
  \describe{
    \item{print}{\code{signature(object = "entangledMCP")}: A method for printing the data of the entangled graph to the R console.}
    \item{getMatrices}{\code{signature(object = "entangledMCP")}: A method for getting the list of adjacency matrices of the entangled graph.}
    \item{getWeights}{\code{signature(object = "entangledMCP")}: A method for getting the matrix of weights of the entangled graph.}
  }
}
\author{Kornelius Rohmeyer \email{rohmeyer@small-projects.de}}
\seealso{
\code{\link[gMCP:graphMCP-class]{graphMCP}}
}
\examples{

g1 <- BonferroniHolm(2)
g2 <- BonferroniHolm(2)

graph <- new("entangledMCP", graphs=list(g1,g2), weights=c(0.5,0.5))

getMatrices(graph)
getWeights(graph)

}
\keyword{ graphs }