\name{matrix2graph}
\alias{matrix2graph}
\alias{graph2matrix}
\title{
	Matrix2Graph and Graph2Matrix
}
\description{
	Creates a graph of class \code{\link{graphMCP}} from a given adjacency matrix or vice versa.
}
\usage{
	matrix2graph(m, alpha=rep(0,dim(m)[1])) 
	graph2matrix(graph) 
}

\arguments{
  \item{m}{
	An adjacency matrix.
  }
  \item{alpha}{
  	A numeric for the initial allocation of the significance level alpha to the individual hypotheses.
  }  
  \item{graph}{
	A graph of class \code{\link{graphMCP}}.
  }
}
\details{
	The hypotheses names are the row names or if these are \code{NULL}, 
	the column names or if these are also \code{NULL} of type H1, H2, H3, ...
	
	If the diagonal of the matrix is unequal zero, the values are ignored and a warning is given.
}
\value{
	A graph of class \code{\link{graphMCP}} with the given adjacency matrix for matrix2graph.
	The adjacency matrix of a \code{\link{graphMCP}} graph for graph2matrix.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphMCP}}, \code{\link{graph2matrix}}
}
\examples{

# Bonferroni-Holm:
m <- matrix(rep(1/3, 16), nrow=4)
diag(m) <- c(0, 0, 0, 0)
graph <- matrix2graph(m)
print(graph)
graph2matrix(graph)

}
\keyword{ print }
\keyword{ graphs }

