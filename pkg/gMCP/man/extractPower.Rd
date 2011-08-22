\name{extractPower}
\alias{extractPower}
\title{ Calculate power values }
\description{
  Calculates local power values, expected number of rejections, the
  power to reject at least one hypothesis and the power to reject all hypotheses.
}
\usage{
extractPower(x)
}
\arguments{
  \item{x}{ A matrix containing the rejected hypothesis, as produces by
    the graphTest function. }
}
\value{
  A list containg three elements
  \item{LocPower }{A numeric giving the local powers for the hypotheses}
  \item{ExpNrRej }{The expected number of rejections}
  \item{PowAtlst1 }{The power to reject at least one hypothesis}
  \item{RejectAll }{The power to reject all hypotheses}
}
\author{ Bjoern Bornkamp, Kornelius Rohmeyer }

\keyword{ htest }

