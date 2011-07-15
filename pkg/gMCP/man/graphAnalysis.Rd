\name{graphAnalysis}
\alias{graphAnalysis}

\title{
	Analysis of a gMCP-Graph
}
\description{
	Creates LaTeX code that represents the given graph.
}
\usage{
	graphAnalysis(graph, file = "")
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphMCP}}.
  }
  \item{file}{ 
  	A connection, or a character string naming the file to print
    to.  If \code{""} (the default), the analysis is printed to the standard
    output connection (the console unless redirected by \code{sink}).
    If it is \code{"|cmd"}, the output is piped to the command given
    by \code{cmd}, by opening a pipe connection [taken from the manual page
    of \code{cat}, which is called in this function].
  }
}
\details{
	In the moment it is only tested whether each node is accessible from
	each other node. Further analysis will be added in future versions.
}
\value{
	A character string that contains the printed analysis.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphMCP}}
}
\examples{

g <- BonferroniHolm(5)

graphAnalysis(g)

}
\keyword{ graphs }

