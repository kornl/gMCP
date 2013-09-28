\name{calcPower}
\alias{calcPower}
\title{Calculate power values}
\usage{
  calcPower(weights, alpha, G, mean = rep(0, nrow(sigma)),
    sigma = diag(length(mean)), cr = NULL, nSim = 10000,
    type = c("quasirandom", "pseudorandom"), f = list(),
    test)
}
\arguments{
  \item{weights}{Initial weight levels for the test
  procedure, see graphTest function.}

  \item{alpha}{Overall alpha level of the procedure, see
  graphTest function.}

  \item{G}{Matrix determining the graph underlying the test
  procedure. Note that the diagonal need to contain only
  0s, while the rows need to sum to 1. When multiple graphs
  should be used this needs to be a list containing the
  different graphs as elements.}

  \item{mean}{Mean under the alternative}

  \item{sigma}{Covariance matrix under the alternative.}

  \item{cr}{Correlation matrix that should be used for the
  parametric test. If \code{cr==NULL} the Bonferroni based
  test procedure is used. Can contain NAs.}

  \item{type}{What type of random numbers to use.
  \code{quasirandom} uses a randomized Lattice rule, and
  should be more efficient than \code{pseudorandom} that
  uses ordinary (pseudo) random numbers.}

  \item{nSim}{Monte Carlo sample size. If type =
  "quasirandom" this number is rounded up to the next power
  of 2, e.g. 1000 is rounded up to
  \eqn{1024=2^10}{1024=2^10} and at least 1024.}

  \item{f}{List of user defined power functions (or just a
  single power function).  If one is interested in the
  power to reject hypotheses 1 and 3 one could specify:
  \cr\code{f=function(x) {x[1] && x[3]}}.\cr If the power
  of rejecting hypotheses 1 and 2 is also of interest one
  would use a (optionally named) list: \cr
  \code{f=list(power1and3=function(x) {x[1] && x[3]},
  power1and2=function(x) {x[1] && x[2]})}.  If the list has
  no names, the functions will be referenced to as "func1",
  "func2", etc. in the output.}

  \item{test}{In the parametric case there is more than one
  way to handle subgraphs with less than the full alpha. If
  the parameter \code{test} is missing, the tests are
  performed as described by Bretz et al. (2011), i.e. tests
  of intersection null hypotheses always exhaust the full
  alpha level even if the sum of weights is strictly
  smaller than one. If \code{test="simple-parametric"} the
  tests are performed as defined in Equation (3) of Bretz
  et al. (2011).}
}
\value{
  A list containg three elements
}
\description{
  Given the distribution under the alternative (assumed to
  be multivariate normal), this function calculates the
  power to reject at least one hypothesis, the local power
  for the hypotheses as well as the expected number of
  rejections.
}
\examples{
## reproduce example from Stat Med paper (Bretz et al. 2010, Table I)
## first only consider line 2 of Table I
## significance levels
weights <- c(1/2, 1/2, 0, 0)
## graph
G <- rbind(c(0, 0.5, 0.5, 0),
           c(0.5, 0, 0, 0.5),
           c(0, 1, 0, 0),
           c(1, 0, 0, 0))
## or equivalent:
G <- simpleSuccessiveII()
}
\author{
  Bjoern Bornkamp
}
\references{
  Bretz, F., Maurer, W., Brannath, W. and Posch, M. (2009)
  A graphical approach to sequentially rejective multiple
  test procedures. Statistics in Medicine, 28, 586--604

  Bretz, F., Maurer, W. and Hommel, G. (2010) Test and
  power considerations for multiple endpoint analyses using
  sequentially rejective graphical procedures, to appear in
  Statistics in Medicine
}
\keyword{htest}

