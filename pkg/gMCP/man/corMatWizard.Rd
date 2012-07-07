\name{corMatWizard}
\alias{corMatWizard}

\title{
	Graphical User Interface for the creation of correlation matrices
}
\description{
	Starts a graphical user interface for the correlation matrices.
}
\usage{
	corMatWizard(n=dim(matrix)[1], matrix=diag(n), names=paste("H",1:n))
}

\arguments{
  \item{n}{
	Square root of the dimension of the \eqn{n\times n}{nxn}-Matrix.
  }
  \item{matrix}{
	Matrix of dimension \eqn{n\times n}{nxn} to start with.
  }
  \item{grid}{
  	Names 
  }
}
\details{

}
\value{
	The function itself returns NULL. But with the dialog a symmetric matrix of dimension n times n can be created or edited that will be available in R under the specified variable name after saving.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\examples{

\dontrun{
corMatWizard(5)
corMatWizard(5)}

}
\keyword{ misc }
\keyword{ graphs }

