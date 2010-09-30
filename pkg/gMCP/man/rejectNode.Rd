\name{rejectNode}
\alias{rejectNode}

\title{
	A graphical approach to sequentially rejective multiple test procedures
}
\description{
	Performs a sequentially rejective multiple test procedure on a graph given unadjusted p-values.
}
\usage{
	rejectNode(graph, node, verbose=FALSE) 
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphMCP}}.
  }
  \item{node}{
  	A character string specifying the node to reject.
  } 
  \item{verbose}{Logical scalar.  If \code{TRUE} verbose output is generated during
          sequentially rejection steps.}  
}
\details{
	For details see the given references.
}
\value{
An updated graph of class \code{\link{graphMCP}}.
}
\references{
Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch:
A graphical approach to sequentially rejective multiple test procedures.
Statistics in Medicine 2009 vol. 28 issue 4 page 586-604. 
\url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}
}
\author{
Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphMCP}}
}
\examples{

g <- createBonferroniHolmGraph(5)

rejectNode(g, "H1")


}
\keyword{ htest }
\keyword{ graphs }

