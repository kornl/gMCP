\name{joinGraphs}
\alias{joinGraphs}

\title{
	Joins two graphMCP objects
}
\description{
	Creates a new graphMCP object by joining two given graphMCP objects.
}
\usage{
	joinGraphs(graph1, graph2, xOffset=0, yOffset=200)
}

\arguments{
  \item{graph1}{
	A graph of class \code{\link{graphMCP}}.
  }
  \item{graph2}{
	A graph of class \code{\link{graphMCP}}.
  }
  \item{xOffset}{
	A numeric specifying an offset (on the x-axis) for placing the nodes and edge labels of the second graph.
  }
  \item{yOffset}{
	A numeric specifying an offset (on the y-axis) for placing the nodes and edge labels of the second graph.
  }
}
\details{
	If \code{graph1} and \code{graph2} have duplicates in the node names,
	the nodes of the second graph will be renamed.
	
	If and only if the sum of the weights of graph1 and graph2 exceeds 1,
	the weights are scaled so that the sum equals 1.
	
	A description attribute of either graph will be discarded.  
}
\value{
	A graphMCP object that represents a graph that consists of the two given graphs.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphMCP}}
}
\examples{

g1 <- BonferroniHolmGraph(2)
g2 <- BonferroniHolmGraph(3)

joinGraphs(g1, g2)

}
\keyword{ graphs }

