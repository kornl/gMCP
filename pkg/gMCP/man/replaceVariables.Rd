\name{replaceVariables}
\alias{replaceVariables}

\title{
	Replaces variables in a general graph with specified numeric values 
}
\description{
	Given a list of variables and real values a general graph is processed and each variable replaced with the specified numeric value.
}
\usage{
	replaceVariables(graph, variables=list(), ask=TRUE)
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphMCP}} or a character matrix.
  }
  \item{variables}{
    A named list with the specified real values, for example \code{list(a=0.5, b=0.8, "tau"=0.5)}.
  	If \code{ask=TRUE} and this list is missing at all or single variables are missing from the list, 
  	the user is asked for the values (if the session is not interactive an error is thrown).
  }
  \item{ask}{
    If \code{FALSE} all variables that are not specified are not replaced. 
  }
}
\value{
	A graph or a matrix with variables replaced by the specified numeric values.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
\code{\link{graphMCP}}
}
\examples{

graph <- HungEtWang2010()
\dontrun{
replaceVariables(graph)
}
replaceVariables(graph, list("tau"=0.5,"omega"=0.5, "nu"=0.5))
}
\keyword{ print }
\keyword{ graphs }

