\name{generatePvals}
\alias{generatePvals}
%- Also NEED an '\alias' for EACH other topic documented here.
\title{
generatePvals
}
\description{
compute adjusted p-values either for the closed test defined by the
graph or for each elementary hypotheses within each intersection
hypotheses  
}
\usage{
generatePvals(g, w, cr, p , adjusted = TRUE , 
              hint = generateWeights(g, w), exhaust=FALSE, alternatives)
}
%- maybe also 'usage' for other objects documented here.
\arguments{
  \item{g}{
    graph defined as a matrix, each element defines how much of the
    local alpha reserved for the hypothesis corresponding to its row
    index is passed on to the hypothesis corresponding to its column
    index 
  }
  \item{w}{
    vector of weights, defines how much of the overall alpha is
    initially reserved for each elementary hypothesis
  }
  
  \item{cr}{ correlation matrix if p-values arise from
    one-sided tests with multivariate normal distributed test statistics
    for which the correlation is partially known. Unknown values can be
    set to NA. (See details for more information) }
  
  \item{p}{
    vector of observed unadjusted p-values, that belong to
    test-statistics with a joint multivariate normal null distribution
    with (partially) known correlation matrix \code{cr}
  }
  \item{adjusted}{
    logical, if TRUE (default) adjusted p-values for the closed test are
    returned, else a matrix of p-values adjusted only for each
    intersection hypothesis is returned
  }
  
  \item{hint}{
    if intersection hypotheses weights have already been computed
    (output of \code{\link{generateWeights}}) can be passed here
    otherwise will be computed during execution
  }
  \item{exhaust}{
    if \code{FALSE} (default) the p-values are additionally adjusted for the
    case that non-exhaustive weights are specified. (See details)
  }
  \item{alternatives}{ A character vector specifying the alternative
     hypotheses.  Each element must be one of \code{"less"} (default),
     \code{"two.sided"} or \code{"greater"}.  The vector will be
     recycled, so if all alternatives of the elemental hypotheses are
     for example \code{"less"}, it suffices to specify
     \code{alternative="less"} instead of \code{alternative=c("less",
       "less", ..., "less")}.  }
 }

 \details{

    At the moment parametric tests in \code{gMCP} assume that the test
    statistics follow a multivariate normal distribution with known
    variances and correlation matrix \code{cr}. If unadjusted
    p-values are considered it is assumed that under the global null 
    hypothesis the corresponding vector of standard normal quantiles  
    \eqn{(\Phi^{-1}(1-p_1),...,\Phi^{-1}(1-p_m))} follows an
    according multivariate normal distribution, where \eqn{\Phi^{-1}}
    denotes the inverse of the standard normal distribution function.

     Partial knowledge of the correlation matrix is supported. This is
     for example the case if two treatments are compared to a common
     control based on two endpoints. In that case the correlation
     between the treatment-control comparisons is known (e.g.: 1/2 for
     balanced group sizes), whereas the correlation between endpoints
     may be unknown. The correlation matrix has to be passed as a
     numeric matrix with elements of the form: \eqn{cr[i,i] = 1} for
     diagonal elements, \eqn{cr[i,j] = \rho_{ij}}, where \eqn{\rho_{ij}}
     is the known value of the correlation between
     \eqn{\Phi^{-1}(1-p_i)} and \eqn{\Phi^{-1}(1-p_j)} or \code{NA} if
     the corresponding correlation is unknown. For example cr[1,2]=0
     indicates that the first and second test statistic are
     uncorrelated, whereas cr[2,3] = NA means that the true correlation
     between statistics two and three is unknown and may take values
     between -1 and 1. The correlation has to be specified for complete
     blocks (ie.: if cor(i,j), and cor(i,j') for i!=j!=j' are specified
     then cor(j,j') has to be specified as well) otherwise the
     corresponding intersection null hypotheses tests are not uniquely
     defined and an error is returned.
  
     The parametric tests in (Bretz et al. (2011)) are defined such that
     the tests of intersection null hypotheses always exhaust the full
     alpha level even if the sum of weights is strictly smaller than
     one. This has the consequence that certain test procedures that do
     not test each intersection null hypothesis at the full level alpha
     may not be implemented (e.g., a single step Dunnett test). If
     \code{exhaust} is set to \code{FALSE} (default) the parametric
     tests are performed at a reduced level alpha of sum(w) * alpha and
     p-values adjusted accordingly such that test procedures with
     non-exhaustive weighting strategies may be implemented. If set to
     \code{TRUE} the tests are performed as defined in Equation (3) of
     (Bretz et al. (2011)).  }

\value{ If adjusted is set to true
     returns a vector of adjusted p-values. Any elementary null
     hypothesis is rejected if its corresponding adjusted p-value is
     below the predetermined alpha level. For adjusted set to false a
     matrix with p-values adjusted only within each intersection
     hypotheses is returned.  The intersection corresponding to each
     line is given by conversion of the line number into binary (eg. 13
     is binary 1101 and corresponds to (H1,H2,H4)). If any adjusted
     p-value within a given line falls below alpha, then the
     corresponding intersection hypotheses can be rejected.  }
\references{
     Bretz F, Maurer W, Brannath W, Posch M; (2008) - A graphical approach to sequentially rejective multiple testing procedures. - Stat Med - 28/4, 586-604 Bretz F, Posch M, Glimm E,
     Klinglmueller F, Maurer W, Rohmeyer K; (2011) - Graphical approaches for multiple endpoint problems using weighted Bonferroni, Simes or parametric tests - to appear }
\author{Florian Klinglmueller}

\examples{
## Define some graph as matrix
g <- matrix(c(0,0,1,0, 0,0,0,1, 0,1,0,0, 1,0,0,0), nrow = 4,byrow=TRUE)
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
## p-values as Section 3 of Bretz et al. (2011),
p <- c(0.0121,0.0337,0.0084,0.0160)

## Boundaries for correlated test statistics at alpha level .05:
generatePvals(g,w,c,p)
}
% Add one or more standard keywords, see file 'KEYWORDS' in the
% R documentation directory.
\keyword{ htest }
\keyword{ htest }
