\name{subGraph}
\alias{subGraph}

\title{
	Get a subgraph
}
\description{
	Given a set of nodes and a graph this function creates the subgraph containing only the specified nodes.
}
\usage{
	subGraph(graph, subset)
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphMCP}}.
  }
  \item{subset}{
  	A logical or character vector specifying the nodes in the subgraph.
  }
}
\value{
	A subgraph containing only the specified nodes.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphMCP}}
}
\examples{

graph <- improvedParallelGatekeeping()
subGraph(graph, c(TRUE, FALSE, TRUE, FALSE))
subGraph(graph, c("H1", "H3"))

}
\keyword{ print }
\keyword{ graphs }

