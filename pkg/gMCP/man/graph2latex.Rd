\name{graph2latex}
\alias{graph2latex}

\title{
	Graph2LaTeX
}
\description{
	Creates LaTeX code that represents the given graph.
}
\usage{
	graph2latex(graph, package="TikZ", scale=1, alpha=0.05, pvalues,
	fontsize=c("tiny","scriptsize", "footnotesize", "small",
		"normalsize", "large", "Large", "LARGE", "huge", "Huge"), 
		nodeTikZ, labelTikZ="near start,above,fill=blue!20",
		tikzEnv=TRUE, offset=c(0,0))
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphMCP}}.
  }
  \item{package}{
  	A character string specifying the LaTeX package that should be used.
  	Up to now only \code{TikZ} is available.
  }   
  \item{scale}{
  	A numeric scalar specifying a possible scaling of the graph. Note that this does not effect the fontsize of the graph.
  	(Coordinates are interpreted in big points - 72 bp = 1 inch).
  }
  \item{alpha}{
  	An optional numeric argument to specify the type I error rate. 
  }
  \item{pvalues}{
  	If the optional numeric argument pvalues is given, nodes that can be rejected, will be marked. 
  }
  \item{fontsize}{
  	An optional character vector specifying the fontsize for the graph, must be one of \code{"tiny"}, \code{"scriptsize"}, 
  	\code{"footnotesize"}, \code{"small"}, \code{"normalsize"}, \code{"large"}, \code{"Large"}, \code{"LARGE"}, 
  	\code{"huge"} or \code{"Huge"}.
  }  
  \item{nodeTikZ}{
  	A character string with additional arguments for the TikZ \code{node} command like for example \code{nodeTikZ="minimum size=2cm"}.
  }
  \item{labelTikZ}{
  	A character string with arguments for the TikZ \code{node} command within an edge.
  }
  \item{tikzEnv}{
  	Logical whether the LaTeX code should be wrapped in a TikZ environment.
  }
  \item{offset}{
  	A numeric of length 2 specifying the x and y offset in the TikZ environment.
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
\code{\link{graphMCP}}
}
\examples{

g <- createBonferroniHolmGraph(5)

graph2latex(g)

}
\keyword{ print }
\keyword{ graphs }

