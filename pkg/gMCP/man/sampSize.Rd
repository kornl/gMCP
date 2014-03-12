\name{sampSize}
\alias{sampSize}
\title{Function for sample size calculation}
\usage{
  sampSize(upperN, lowerN = floor(upperN/2), targFunc,
    target, tol = 0.001, alRatio,
    Ntype = c("arm", "total"), verbose = FALSE)
}
\arguments{
  \item{upperN}{\code{targFunc(upperN)} should be bigger
  than target (otherwise upperN is doubled until this is
  the case).}

  \item{lowerN}{\code{targFunc(lowerN)} should be smaller
  than target (otherwise lowerN is halfed until this is the
  case).}

  \item{targFunc}{The target (power) function that should
  be monotonic in \code{n}.}

  \item{target}{The target value. The function searches the
  \code{n} with \code{abs(targFunc(n)-target)<tol}.}

  \item{tol}{Tolerance: The function searches the \code{n}
  with \code{abs(targFunc(n)-target)<tol}.}

  \item{alRatio}{Allocation ratio.}

  \item{Ntype}{Either \code{"arm"} or \code{"total"}.}

  \item{verbose}{Logical, whether verbose output should be
  printed.}
}
\description{
  Function for sample size calculation
}
\details{
  For details see the manual and examples.
}
\examples{
f <- function(x){1/100*log(x)}
gMCP:::sampSize(upperN=1000, targFunc=f, target=0.8, verbose=TRUE, alRatio=1)
gMCP:::sampSize(lowerN=1, upperN=1000, targFunc=f, target=0.8, verbose=TRUE, alRatio=1)
}

