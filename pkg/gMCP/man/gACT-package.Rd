\name{gACT-package}
\alias{gACT-package}
\docType{package}
\title{
gACT - Graphical Approach for Correlated Test-statistics - provides functions
for the construction of graph-based multiple comparison procedures with
partially known correlation structures.
}
\description{
  gACT provides functions to construct, perform and evaluate graph based
  multiple testing procedures (MCPs) whith partial knowledge of the correlation
  structure between hypotheses.
}
\details{
\tabular{ll}{
Package: \tab gACT\cr
Type: \tab Package\cr
Version: \tab 0.1\cr
Date: \tab 2011-02-15\cr
License: \tab GPL 2\cr
LazyLoad: \tab yes\cr
}
The graph based MCPs addressed in this package are fully defined by an
initial weights vector, a matrix laying down the p-value reallocation
strategy and a quasi correlation matrix detailing correlations between
hypotheses where known (with unknown pairwise correlations set to NA).

We provide function \code{\link{generateWeights}} which computes for each
intersection hypotheses in the closure the weights for
the corresponding elementary hypetheses. These are identical to the
bounds of the conventional weighted Bonferroni procedure defined by the
graph.

We provide function \code{\link{generateBounds}} which computes rejection
boundaries for each elementary hypothesis within each intersection
hypothesis of the closure. These are computed in a way if the full
correlation matrix is known for more than two hypotheses in the
intersection, the mulitvariate normal distribution is used to determine
appropriate boundaries.

Finally we provide \code{\link{generateTest}}, which for a given vector of
z-scores computes the decision of the closed testing procedure which
rejects only those hypotheses for which all intersection hypotheses that
it is a member of can be rejected.
}
\author{
Florian Klinglmueller

Maintainer: Florian Klinglmueller <florian.klinglmueller@meduniwien.ac.at>
~~ The author and/or maintainer of the package ~~
}
\references{
  Bretz F, Maurer W, Brannath W, Posch M; (2008) - A graphical approach to
sequentially rejective multiple testing procedures. -  Stat Med - 28/4,
586-604
Bretz F, Posch M, Glimm E, Klinglmueller F, Maurer W, Rohmeyer K; (2011) - Graphical approaches for multiple endpoint problems using weighted Bonferroni, Simes or parametric tests - to appear
}
\keyword{ htest }
\seealso{
  \pkg{multcomp}
  \pkg{gMCP}
}
\examples{
 ## Define some graph as matrix
 g <- matrix(c(0,0,1,0,
               0,0,0,1,
               0,1,0,0,
               1,0,0,0), nrow = 4,byrow=TRUE)
 ## Choose weights
 w <- c(.5,.5,0,0)
 ## Some correlation (upper and lower first diagonal 1/2)
 c <- diag(4)
 c[1:2,3:4] <- NA
 c[3:4,1:2] <- NA
 c[1,2] <- 1/2
 c[2,1] <- 1/2
 c[3,4] <- 1/2
 c[4,3] <- 1/2

 ## Weights of conventional gMCP test:
 generateWeights(g,w)
 ## Boundaries for correlated test statistics at alpha level .05:
 generateBounds(g,w,c,.05)
 ## Test function for further use:
 myTest <- generateTest(g,w,c,.05)
 myTest(c(3,2,1,2))
}
