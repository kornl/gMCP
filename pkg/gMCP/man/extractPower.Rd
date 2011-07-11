\name{extractPower}
\alias{extractPower}
\title{ Calculate power values }
\description{
  Calculates local power values, expected number of rejections and the
  power to reject at least one hypothesis.
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
}
\author{ Bjoern Bornkamp }

\keyword{ htest }

