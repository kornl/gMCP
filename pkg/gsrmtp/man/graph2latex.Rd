\name{graph2latex}
\alias{graph2latex}

\title{
	Graph2LaTeX
}
\description{
	Creates LaTeX code that represents the given graph.
}
\usage{
	graph2latex(graph, package="TikZ", scale=0.7, pvalues)
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphSRMTP}}.
  }
  \item{package}{
  	A character string specifying the LaTeX package that should be used.
  	Up to now only \code{TikZ} is available.
  }   
  \item{scale}{
  	A numeric scalar specifying a possible scaling of the graph.
  }
  \item{pvalues}{
  	If the optional numeric argument pvalues is given, nodes that can be rejected, will be marked. 
  }
}
\details{
	For details see the given references.
}
\value{
	A character string that contains LaTeX code representing the given graph.
}
\references{
	The TikZ and PGF Packages Manual for version 2.00,
	Till Tantau,
	\url{http://www.ctan.org/tex-archive/graphics/pgf/base/doc/generic/pgf/pgfmanual.pdf}
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphSRMTP}}
}
\examples{

g <- createBonferroniHolmGraph(5)

graph2latex(g)

}
\keyword{ print }
\keyword{ graphs }

