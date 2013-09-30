\name{extractPower}
\alias{extractPower}
\title{Calculate power values}
\usage{
  extractPower(x, f = list())
}
\arguments{
  \item{x}{A matrix containing the rejected hypothesis, as
  produces by the graphTest function.}

  \item{f}{List of user defined power functions. If one is
  interested in the power to reject hypotheses 1 and 3 one
  could specify \code{function(x) {x[1] && x[3]}}}
}
\value{
  A list containg at least the following four elements and
  an element for each element in the parameter \code{f}.
  \itemize{ \item LocPower A numeric giving the local
  powers for the hypotheses \item ExpNrRej The expected
  number of rejections \item PowAtlst1 The power to reject
  at least one hypothesis \item RejectAll The power to
  reject all hypotheses }
}
\description{
  Calculates local power values, expected number of
  rejections, the power to reject at least one hypothesis
  and the power to reject all hypotheses.
}
\keyword{htest}

