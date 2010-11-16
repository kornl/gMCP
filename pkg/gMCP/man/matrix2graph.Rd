\name{matrix2graph}
\alias{matrix2graph}

\title{
	Matrix2Graph
}
\description{
	Creates a graph of class \code{\link{graphMCP}} from a given adjacency matrix.
}
\usage{
	matrix2graph <- function(m, alpha=rep(0,dim(m)[1])) 
}

\arguments{
  \item{m}{
	An adjacency matrix.
  }
  \item{alpha}{
  	A numeric for the initial allocation of the significance level alpha to the individual hypotheses.
  }  
}
\details{
	The hypotheses names are the row names or if these are \code{NULL}, 
	the column names or if these are also \code{NULL}   
}
\value{
	A graph of class \code{\link{graphMCP}} with the given adjacency matrix.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphMCP}}, \code{\link{graph2matrix}}
}
\examples{

# Bonferroni-Holm:
m <- matrix(rep(1/3, 9), nrow=3)
matrix2graph(m)

}
\keyword{ print }
\keyword{ graphs }

