\name{substituteEps}
\alias{substituteEps}

\title{
	Substitute Epsilon
}
\description{
	Substitute Epsilon with a given value.
}
\usage{
	substituteEps(graph, eps=10^(-3))
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphMCP}} or class \code{\link{entangledMCP}}.
  }
  \item{eps}{
  	A numeric scalar specifying a value for epsilon edges.
  }
}
\details{
	For details see the given references.
}
\value{
	A graph where all epsilons have been replaced with the given value.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphMCP}}, \code{\link{entangledMCP}}
}
\examples{

graph <- improvedParallelGatekeeping()
graph
substituteEps(graph, eps=0.01)

}
\keyword{ print }
\keyword{ graphs }

