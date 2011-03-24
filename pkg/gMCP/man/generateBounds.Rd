\name{generateBounds}
\alias{generateBounds}
%- Also NEED an '\alias' for EACH other topic documented here.
\title{
generateBounds
}
\description{
compute rejection bounds for z-scores of each elementary hypotheses
within each intersection hypotheses 
}
\usage{
generateBounds(g, w, cr, al = 0.05, hint = generateWeights(g, w))
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
  \item{cr}{
    correlation matrix defining pairwise correlations between the
    elementary hypotheses. Unknown values can be set to NA. (See details
    for more information)
}
  \item{al}{
    overall alpha level at which the family error is controlled
}
  \item{hint}{
    if intersection hypotheses weights have already been computed
    (output of \code{\link{generateWeights}}) can be passed here
    otherwise will be computed during execution
}
}
\details{
  The correlation has to be passed as a numeric matrix with elements of
  the form: cr[i,i] = 1, cr[i,j] = cor(i,j) if known or cr[i,j] = NA if
  unknown. For example cr[1,2]=0 would indicate that the first and
  second test statistics are uncorrelated, whereas cr[2,3] = NA would
  mean that statistics two and three might have correlation between -1
  and 1. The correlation has to be specified for complete blocks (ie.: if
  cor(i,j), and cor(i,j') for i!=j!=j' are known than cor(j,j') has to
  be known as well) otherwise multcomp will throw an exception as NA
  correlations are fed to the multivariate normal distribution
}
\value{
  Returns a matrix of rejection bounds. Each row corresponds to an intersection
  hypothesis. The intersection corresponding to each line is given by
  conversion of the line number into binary (eg. 13 is binary 1101 and 
  corresponds to (H1,H2,H4)) 
}
\references{
  Bretz F, Maurer W, Brannath W, Posch M; (2008) - A graphical approach to
sequentially rejective multiple testing procedures. -  Stat Med - 28/4, 586-604
Bretz F, Posch M, Glimm E, Klinglmueller F, Maurer W, Rohmeyer K; (2011) - Graphical approaches for multiple endpoint problems using weighted Bonferroni, Simes or parametric tests - to appear
}
\author{
Florian Klinglmueller
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

 ## Boundaries for correlated test statistics at alpha level .05:
 generateBounds(g,w,c,.05)
}
% Add one or more standard keywords, see file 'KEYWORDS' in the
% R documentation directory.
\keyword{ htest }
\keyword{ htest }
