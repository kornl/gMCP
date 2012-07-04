\name{corMatWizard}
\alias{corMatWizard}

\title{
	Graphical User Interface for the Creation of SRMTP Graphs
}
\description{
	Starts a graphical user interface for the creation of SRMTP graphs.
}
\usage{
	corMatWizard(dim=n, matrix=diag(n), names=paste("H",1:n))
}

\arguments{
  \item{graph}{

  }
  \item{pvalues}{

  }
  \item{grid}{
  
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
corMatWizard(5)}

}
\keyword{ misc }
\keyword{ graphs }

