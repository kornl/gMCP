\name{extractPower}
\alias{extractPower}
\title{ Calculate power values }
\description{
  Calculates local power values, expected number of rejections, the
  power to reject at least one hypothesis and the power to reject all hypotheses.
}
\usage{
extractPower(x, f=list())
}
\arguments{
  \item{x}{ A matrix containing the rejected hypothesis, as produces by
    the graphTest function. }
  \item{f}{ List of user defined power functions. If one is interested
    in the power to reject hypotheses 1 and 3 one could specify
    \code{function(x) {x[1] && x[3]}}
  }
}
\value{
  A list containg at least the following three elements
  \item{LocPower }{A numeric giving the local powers for the hypotheses}
  \item{ExpNrRej }{The expected number of rejections}
  \item{PowAtlst1 }{The power to reject at least one hypothesis}
  \item{RejectAll }{The power to reject all hypotheses}
  Further it contains an element for each element in the parameter \code{f}.
}
\author{ Bjoern Bornkamp, Kornelius Rohmeyer }

\keyword{ htest }

