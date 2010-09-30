\name{simConfint}
\alias{simConfint}
\alias{simConfint,graphSRMTP-method}

\title{
	Simultaneous confidence intervals for sequentially rejective multiple test procedures
}
\description{
	Calculates simultaneous confidence intervals for sequentially rejective multiple test procedures.
}
\usage{
	simConfint(object, pvalues, confintF)
}

\arguments{
  \item{object}{
	A graph of class \code{\link{graphSRMTP}}.
  }  
  \item{pvalues}{
  	A numeric vector specifying the p-values for the sequentially rejective MTP.
  } 
  \item{confintF}{
  	A function that calculates the confidence intervals.
  	Must be of signature \code{("character", "numeric")},
  	where the first parameter is the hypothesis name and the second the marginal confidence level. (See examples)
  } 
    
}
\details{
	For details see the given references.
}
\value{
A matrix (or vector) with columns giving lower and upper
confidence limits for each parameter. These will be labelled as
(1-level)/2 and 1 - (1-level)/2 in \% (by default 2.5\% and 97.5\%).
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
\code{\link{graphSRMTP}}
}
\examples{

g <- createBonferroniHolmGraph(5)

simConfint(g, pvalues=c(0.01, 0.02, 0.04, 0.04, 0.7), function(node, alpha) {return(1:2)})

}
\keyword{ htest }
\keyword{ graphs }

