\name{gACT}
\alias{gACT}
\title{
  gACT
}
\description{
  perform multiple comparison procedure with
  correlated test statistics defined by a graph 
}
\usage{
gACT(z, g, w, c, al)
}
\arguments{
  \item{z}{
    vector of z-scores
  }
  \item{g}{
    graph defined as a matrix, each element defines how much of the
    local alpha reserved for the hypothesis corresponding to its row
    index is passed on to the hypothesis corresponding to its column
    index 
  }
  \item{w}{
    vector of weights, defines how much of the overall alpha is
    initially reserved for each elementary hypthosis
  }
  \item{c}{
    correlation matrix defining pairwise correlations between the
    elementary hypotheses. Unknown values can be set to NA.
  }
  \item{al}{
    overall alpha level at which the family error is controlled
  }
}
\details{
  Correlation structure needs to possess block structure (without
  overlapping blocks) otherwise multcomp will throw an exception as NA
  correlations are fed to the multivariate normal distribution
}
\value{
  Returns a boolean vector with elements false if the particular
  elementary hypothesis can not be rejected and true otherwise.
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

 ## Test function for further use:
 gACT(c(3,2,1,2),g,w,c,.05)
}
\keyword{ htest }

