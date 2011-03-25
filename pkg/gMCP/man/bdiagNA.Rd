\name{bdiagNA}
\alias{bdiagNA}
\title{
	Create a Block Diagonal Matrix with NA outside the diagonal
}
\description{
	Build a block diagonal matrix given several building block matrices, but with NA values outside the diagonal.
}
\usage{
	bdiagNA(...) 
}

\arguments{
  \item{...}{
	individual matrices or a \code{list} of matrices.
  }
}
\details{
	This function is usefull to build the correlation matrices, when only partial knowledge of the correlation exists.
}
\value{
	A block diagonal matrix with NA values outside the diagonal.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphMCP}}, \code{\link{graph2matrix}}
}
\examples{

bdiagNA(diag(3), matrix(1/2,nr=3,nc=3), diag(2))

}

