\name{srmtp}
\alias{srmtp}

\title{
	A graphical approach to sequentially rejective multiple test procedures
}
\description{
	Performs a sequentially rejective multiple test procedure on a graph given unadjusted p-values.
}
\usage{
	srmtp(graph, pvalues, verbose=FALSE)
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphSRMTP}}.
  }
  \item{pvalues}{
  	A numeric vector specifying the p-values for the sequentially rejective MTP.
  } 
  \item{verbose}{logical scalar.  If \code{TRUE} verbose output is generated during
          sequentially rejection steps.}  
}
\details{
	For details see the given references.
}
\value{
An object of class \code{srmtpResult}, more specifically a list with elements
\item{comp1 }{Description of 'comp1'}
\item{comp2 }{Description of 'comp2'}
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

srmtp(g, pvalues=c(0.1, 0.2, 0.4, 0.4, 0.7))


}
\keyword{ htest }
\keyword{ graphs }

