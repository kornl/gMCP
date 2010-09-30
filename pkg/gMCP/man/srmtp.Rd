\name{srmtp}
\alias{srmtp}
\alias{adjPValues}

\title{
	A graphical approach to sequentially rejective multiple test procedures
}
\description{
	Performs a sequentially rejective multiple test procedure on a graph given unadjusted p-values.
}
\usage{
	srmtp(graph, pvalues, verbose=FALSE)
	adjPValues(graph, pvalues, verbose=FALSE)
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphSRMTP}}.
  }
  \item{pvalues}{
  	A numeric vector specifying the p-values for the sequentially rejective MTP.
  } 
  \item{verbose}{Logical scalar.  If \code{TRUE} verbose output is generated during
          sequentially rejection steps.}  
}
\details{
	For details see the given references.
}
\value{
An object of class \code{srmtpResult}, more specifically a list with elements
\item{graphs }{ list of graphs}				
\item{pvalues }{ p-values}
\item{adjPValues }{ adjusted p-values}
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

%% ~Make other sections like Warning with \section{Warning }{....} ~

\seealso{
\code{\link{graphSRMTP}}
}
\examples{

g <- createBonferroniHolmGraph(5)

srmtp(g, pvalues=c(0.01, 0.02, 0.04, 0.04, 0.7))


}
\keyword{ htest }
\keyword{ graphs }

