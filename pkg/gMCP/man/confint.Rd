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
	simConfint(object, pvalues, confint, alternative=c("less", "greater"), estimates, df)
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
}
\details{
	For details see the given references.
}
\value{
A matrix (or vector) with columns giving lower and upper
confidence limits for each parameter. These will be labeled as
lower and upper bound.
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

g <- createBonferroniHolmGraph(3)

# Estimates:
est <- c("H1"=0.9101444, "H2"=0.4485153, "H3"=1.4568271)
# Sample standard deviations:
ssd <- c("H1"=1.2, "H2"=0.8, "H3"=1.7)
pvalues <- 1-pt(est/ssd*sqrt(10),df=9)


simConfint(g, pvalues, confint="t", alternative="greater", df=9, estimates=est)

simConfint(g, pvalues, 
		confint = function(node, alpha) {
			print(paste("Node ",node," has est=",est[node],", stderr=",ssd[node]/sqrt(10)," and alpha=",alpha,".",sep=""))
			c(est[node]+qt(alpha,df=9)*ssd[node]/sqrt(10), Inf)
		})

}
\keyword{ htest }
\keyword{ graphs }

