\name{placeNodes}
\alias{placeNodes}

\title{
	Placement the nodes 
}
\description{
	Places the nodes of a graph according to a specified layout. 
}
\usage{
	graphAnalysis(graph, nrow, ncol, byrow = FALSE)
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphMCP}}.
  }
  \item{nrow}{The desired number of rows.}
  \item{ncol}{The desired number of columns.}
  \item{byrow}{Logical whether the graph is filled by
          rows (otherwise by columns).}
}
\details{
	 If one of \code{nrow} or \code{ncol} is not given, an attempt is made to
     infer it from the number of nodes of the \code{graph} and the other parameter.
     If neither is given, the graph is placed as a circle.
}
\value{
	The graph with nodes placed according to the specified layout.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphMCP}}
}
\examples{

g <- createBonferroniHolmGraph(6)

g <- placeNodes(g, nrow=2)

\dontrun{
graphGUI(g)

}

}
\keyword{ graphs }

