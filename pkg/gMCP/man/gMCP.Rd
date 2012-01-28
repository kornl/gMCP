\name{gMCP}
\alias{gMCP}

\title{
	A graphical approach to sequentially rejective multiple test procedures
}
\description{
	Performs a sequentially rejective multiple test procedure on a graph given unadjusted p-values.
}
\usage{
	gMCP(graph, pvalues, test, correlation, alpha=0.05, 
	     approxEps=TRUE, eps=10^(-3), ..., useC=FALSE, verbose=FALSE, keepWeights=TRUE)
}

\arguments{
  \item{graph}{
	A graph of class \code{\link{graphMCP}}.
  }
  \item{pvalues}{
  	A numeric vector specifying the p-values for the sequentially rejective MTP.
  } 
  \item{test}{
  	A test function. Will be supported in future versions.
  } 
  \item{correlation}{
  	Correlation matrix, if the tests are correlated. 
  	Also valid are strings like \code{"Dunnett", "Tukey", "Sequen", "AVE", "Changepoint", "Williams", "Marcus", "McDermott", "UmbrellaWilliams", "GrandMean"}.
  	In this case please add a numeric parameter \code{samplesize} that specifies the sample size of each group. 
  } 
  \item{alpha}{
  	A numeric specifying the maximal allowed type one error rate.
  }
  \item{approxEps}{
  	A boolean specifying whether epsilon values should be substituted with the value given in the paramenter \code{eps}.
  }   
  \item{eps}{
  	A numeric scalar specifying a value for epsilon edges.
  }
  \item{...}{
  	Test specific arguments can be given here.
  }   
  \item{useC}{
    Logical scalar. If \code{TRUE} neither adjusted p-values nor intermediate graphs are returned,
  	but the calculation is sped up by using code written in C. THIS CODE IS NOT FOR PRODUCTIVE USE YET!
  	If approxEps is \code{FALSE} and the graph contains epsilon edges, a warning is thrown and \code{useC} will be ignored.
  }
  \item{verbose}{
    Logical scalar. If \code{TRUE} verbose output is generated during
    sequentially rejection steps.
  }
  \item{keepWeights}{
    Logical scalar. If \code{FALSE} the weight of a node without outgoing edges is set to 0 if it is removed.
    Otherwise it keeps its weight.
  }  
}
\details{
	For details see the given references.
}
\value{
An object of class \code{gMCPResult}, more specifically a list with elements
\item{graphs }{ list of graphs}				
\item{pvalues }{ p-values}
\item{rejected }{ logical whether hyptheses could be rejected}
\item{adjPValues }{ adjusted p-values}
}
\references{
Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch:
A graphical approach to sequentially rejective multiple test procedures.
Statistics in Medicine 2009 vol. 28 issue 4 page 586-604. 
\url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}

Bretz F., Posch M., Glimm E., Klinglmueller F., Maurer W., Rohmeyer K. (2011):
Graphical approaches for multiple endpoint problems using weighted Bonferroni, Simes or parametric tests - to appear.

Strassburger K., Bretz F.:
Compatible simultaneous lower confidence bounds for the Holm procedure and other
Bonferroni based closed tests. Statistics in Medicine 2008; 27:4914-4927.

Hommel G., Bretz F., Maurer W.:
Powerful short-cuts for multiple testing procedures with special reference to 
gatekeeping strategies. Statistics in Medicine 2007; 26:4063-4073.

Guilbaud O.: 
Simultaneous confidence regions corresponding to Holm's stepdown procedure and 
other closed-testing procedures. Biometrical Journal 2008; 50:678-692.
}
\author{
Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

%% ~Make other sections like Warning with \section{Warning }{....} ~

\seealso{
\code{\link{graphMCP}}
\code{\link[multcomp:contrMat]{graphNEL}}
}
\examples{

g <- BonferroniHolm(5)

gMCP(g, pvalues=c(0.01, 0.02, 0.04, 0.04, 0.7))


}
\keyword{ htest }
\keyword{ graphs }

