\name{rqmvnorm}
\alias{rqmvnorm}
\title{ Random sample from the multivariate normal distribution }
\description{
  Draw a quasi or pseudo random sample from the MVN distribution. For
  details on the implemented lattice rule for quasi-random numbers see
  Cools et al. (2006).
}
\usage{
rqmvnorm(n, mean = rep(0, nrow(sigma)), sigma = diag(length(mean)),
         seed = NULL, type = c("quasirandom", "pseudorandom"))
}
\arguments{
  \item{n}{ Number of samples, when type = "quasirandom" is used this
    number is rounded up to the next power of 2 (e.g. 1000 to 1024=2^10) 
    and at least 1024.}
  \item{mean}{ Mean vector }
  \item{sigma}{ Covariance matrix }
  \item{seed}{ If spcecified a seed for the Sobol sequence is set.  }
  \item{type}{ What type of random numbers to use. \code{quasirandom}
    uses a randomized Lattice rule, and should be more efficient than
    \code{pseudorandom} that uses ordinary (pseudo) random numbers. }
}
\value{
  Matrix of simulated values
}
\references{
Cools, R., Kuo, F. Y., and Nuyens, D. (2006) Constructing embedded
lattice rules for multivariate integration. SIAM Journal of Scientific
Computing, 28, 2162-2188.
}
\author{ Bjoern Bornkamp. We thank Dr. Frances Kuo for the permission to use the generating vectors
 (order 2 lattice rule) obtained from her website \url{http://web.maths.unsw.edu.au/~fkuo/lattice/}.}
\examples{
sims <- rqmvnorm(100, mean = 1:2, sigma = diag(2))
plot(sims)

}
\keyword{ distribution }
