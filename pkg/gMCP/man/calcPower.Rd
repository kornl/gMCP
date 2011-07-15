\name{calcPower}
\alias{calcPower}
\title{ Calculate power values }
\description{
  Given the distribution under the alternative (assumed to be
  multivariate normal), this function calculates
  the power to reject at least one hypothesis, the local power for the
  hypotheses as well as the expected number of rejections.
}
\usage{
  calcPower(weights, alpha, G, mean = rep(0, nrow(sigma)),
            sigma = diag(length(mean)),corr = NULL,
            nSim = 10000, seed = 4711,
            type = c("quasirandom", "pseudorandom"))
}
\arguments{
  \item{weights}{ Initial weight levels for the test procedure, see graphTest function. }
  \item{alpha}{ Overall alpha level of the procedure, see graphTest function. }
  \item{G}{ Matrix determining the graph underlying the test
    procedure. Note that the diagonal need to contain only 0s, while the
    rows need to sum to 1. When multiple graphs should be used this
    needs to be a list containing the different graphs as elements. }
 \item{mean}{ Mean under the alternative }
  \item{sigma, corr}{ Covariance or correlation matrix under the
    alternative. }
  \item{seed}{ Seed for pseudo random numbers and the random shift of
    the lattice rule. }
  \item{type}{ What type of random numbers to use. \code{quasirandom}
    uses a randomized Lattice rule, and should be more efficient than
    \code{pseudorandom} that uses ordinary (pseudo) random numbers. }
  \item{nSim}{ Monte Carlo sample size. If type = "quasirandom" this
    number is rounded up to the next power of 2, e.g. 1000 is rounded up
    to 1024=2^10. }
  
}

\value{
  A list containg three elements
  \item{LocPower }{A numeric giving the local powers for the hypotheses}
  \item{ExpRejections }{The expected number of rejections}
  \item{PowerAtlst1 }{The power to reject at least one hypothesis}
}
\author{ Bjoern Bornkamp }
\references{

  Bretz, F., Maurer, W., Brannath, W. and Posch, M. (2009) A graphical
  approach to sequentially rejective multiple test
  procedures. Statistics in Medicine, 28, 586--604

  Bretz, F., Maurer, W. and Hommel, G. (2010) Test and power
  considerations for multiple endpoint analyses using sequentially
  rejective graphical procedures, to appear in Statistics in Medicine
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
## alternative (mvn distribution)
corMat <- rbind(c(1, 0.5, 0.5, 0.5/2),
                c(0.5,1,0.5/2,0.5),
                c(0.5,0.5/2,1,0.5),
                c(0.5/2,0.5,0.5,1))
theta <- c(3, 0, 0, 0)
calcPower(weights, alpha=0.025, G, theta, corMat, seed = 4711, nSim = 100000)


## now reproduce all 14 simulation scenarios
## different graphs
weights1 <- c(rep(1/2, 12), 1, 1)
weights2 <- c(rep(1/2, 12), 0, 0)
eps <- 0.01
gam1 <- c(rep(0.5, 10), 1-eps, 0, 0, 0)
gam2 <- gam1
## different multivariate normal alternatives
rho <- c(rep(0.5, 8), 0, 0.99, rep(0.5,4))
th1 <- c(0, 3, 3, 3, 2, 1, rep(3, 7), 0)
th2 <- c(rep(0, 6), 3, 3, 3, 3, 0, 0, 0, 3)
th3 <- c(0, 0, 3, 3, 3, 3, 0, 2, 2, 2, 3, 3, 3, 3)
th4 <- c(0,0,0,3,3,3,0,2,2,2,0,0,0,0)

## function that calculates power values for one scenario
simfunc <- function(nSim, a1, a2, g1, g2, rh, t1, t2, t3, t4, Gr){
  al <- c(a1, a2, 0, 0)
  G <- rbind(c(0, g1, 1-g1, 0), c(g2, 0, 0, 1-g2), c(0, 1, 0, 0), c(1, 0, 0, 0))
  corMat <- rbind(c(1, 0.5, rh, rh/2), c(0.5,1,rh/2,rh), c(rh,rh/2,1,0.5), c(rh/2,rh,0.5,1))
  mean <- c(t1, t2, t3, t4)
  calcPower(al, alpha=0.025, G, mean, corr = corMat, seed = 4711, nSim = nSim)
}

## calculate power for all 14 scenarios
outList <- list()
for(i in 1:14){
  outList[[i]] <- simfunc(10000, weights1[i], weights2[i], gam1[i], gam2[i], rho[i], th1[i], th2[i], th3[i], th4[i])
}

## summarize data as in Stat Med paper Table I 
atlst1 <- as.numeric(lapply(outList, function(x) x$PowAtlst1))
locpow <- do.call("rbind", lapply(outList, function(x) x$LocalPower))

round(cbind(atlst1, locpow), 5)
}
\keyword{ htest }

