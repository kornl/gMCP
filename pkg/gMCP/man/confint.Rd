\name{simConfint}
\alias{simConfint}
\alias{simConfint,graphMCP-method}

\title{
	Simultaneous confidence intervals for sequentially rejective multiple test procedures
}
\description{
	Calculates simultaneous confidence intervals for sequentially rejective multiple test procedures.
}
\usage{
	simConfint(object, pvalues, confint, alternative=c("less", "greater"), estimates, df, alpha=0.05, mu=0)
}

\arguments{
  \item{object}{
	A graph of class \code{\link{graphMCP}}.
  }  
  \item{pvalues}{
  	A numeric vector specifying the p-values for the sequentially rejective MTP.
  } 
  \item{confint}{
  	One of the following:
  	A character string "normal", "t" or a function that calculates the confidence intervals.
  	If confintF=="t" the parameter "df" must be specified. If confint is a function it must be of signature \code{("character", "numeric")},
  	where the first parameter is the hypothesis name and the second the marginal confidence level. (See examples)
  }  
  \item{alternative}{
  	A character string specifying the alternative hypothesis,
          must be "greater" or "less".
  }
  \item{estimates}{
  	Point estimates for the parameters of interest.
  }  
  \item{df}{
  	Degree of freedom as numeric.
  }
  \item{alpha}{
  	The overall alpha level as numeric scalar.
  }
  \item{mu}{
  	The numerical parameter vector under null hypothesis.
  }
}
\details{
	For details see the given references.
}
\value{
  A matrix with columns giving lower confidence limits, point estimates and upper
  confidence limits for each parameter. These will be labeled as
  "lower bound", "estimate" and "upper bound".
%(1-level)/2 and 1 - (1-level)/2 in \% (by default 2.5\% and 97.5\%).
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
  \code{\link{graphMCP}}
}
\examples{

est <- c("H1"=0.860382, "H2"=0.9161474, "H3"=0.9732953)
# Sample standard deviations:
ssd <- c("H1"=0.8759528, "H2"=1.291310, "H3"=0.8570892)

pval <- c(0.01260, 0.05154, 0.02124)/2

simConfint(createBonferroniHolmGraph(3), pvalues=pval, 
		confint=function(node, alpha) {
			c(est[node]-qt(1-alpha,df=9)*ssd[node]/sqrt(10), Inf)
		}, estimates=est, alpha=0.025, mu=0, alternative="greater")

# Note that the sample standard deviations in the following call
# will be calculated from the pvalues and estimates.
ci <- simConfint(createBonferroniHolmGraph(3), pvalues=pval, 
		confint="t", df=9, estimates=est, alpha=0.025, alternative="greater")
ci
	
plotCII(ci[,2], li=ci[,1], ui=ci[,3])

}
\keyword{ htest }
\keyword{ graphs }

