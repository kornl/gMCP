\name{doFinal}
\alias{doFinal}
\title{Perform the final analysis in an adaptive graph based trial}
\usage{
  doFinal(interim, z2, select, matchCE = TRUE,
    zWeights = "reject", G2 = interim@preplanned)
}
\arguments{
  \item{interim}{An object of class
  \code{\link{agMCPInterim}}.}

  \item{z2}{Final-stage z-scores}

  \item{select}{A logical vector giving specifying which
  hypotheses are carried forward to the second stage}

  \item{matchCE}{Logical specifying whether second stage
  weights should be computed proportional to corresponding
  PCEs}

  \item{zWeights}{Either "reject","accept", or "strict"
  giving the rule what should be done in cases where none
  of the selected hypotheses has positive second stage
  weight.}

  \item{G2}{An object of class \code{\link{graphMCP}}
  laying down the rule to compute second stage weights.
  Defaults to pre-planned graph.}
}
\value{
  A \code{\link{agMCPInterim}} object containing the
  results of the final analysis. Most importantly:

  \item{z1,}{z-scores observed at the final analysis}

  \item{preplanned,}{final stage graphical procedure}

  \item{rejected}{logical vector specifying which
  hypothesis can be rejcted}

  \item{erb}{final rejection boundaries}
}
\description{
  Based on a pre-planned graphical multiple comparison
  procedure, construct a valid multiple level alpha test
  that conserves the family wise error in the strong sense
  regardless of any trial adaptations during an unblinded
  interim analysis. - Implementation of adaptive procedures
  is still in an early stage and may change in the near
  future
}
\details{
  For details see the given references.
}
\examples{
## Simple successive graph (Maurer et al. 2011)
## two treatments two hierarchically ordered endpoints
G <- simpleSuccessiveI()

## some z-scores:
z1 <- c(3.4,3,2.5,2)
## O'Brien Fleming boundaries for the primary,
## Pocock boundaries for the secondary  hypotheses.
obfpoc <- agMCPldbounds(c(1,1,2,2))

## Interim analysis
v <- c(1/2,1/3,1/2,1/3)
intA <- doInterim(G,z1,v,gSB=obfpoc)
p2=c(.04,1,.14,1)
z2 <- qnorm(1-p2)

## select only the first treatment
fTest <- secondStageTest(intA,c(1,0,1,0))
}
\author{
  Florian Klinglmueller \email{float@lefant.net}
}
\references{
  Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch:
  A graphical approach to sequentially rejective multiple
  test procedures. Statistics in Medicine 2009 vol. 28
  issue 4 page 586-604.
  \url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}

  Bretz F., Posch M., Glimm E., Klinglmueller F., Maurer
  W., Rohmeyer K. (2011): Graphical approaches for multiple
  endpoint problems using weighted Bonferroni, Simes or
  parametric tests - to appear.

  Posch M, Futschik A (2008): A Uniform Improvement of
  Bonferroni-Type Tests by Sequential Tests JASA 103/481,
  299-308

  Posch M, Maurer W, Bretz F (2010): Type I error rate
  control in adaptive designs for confirmatory clinical
  trials with treatment selection at interim Pharm Stat
  10/2, 96-104
}
\seealso{
  \code{\link{graphMCP}}, \code{\link{doInterim}}
}
\keyword{graphs}
\keyword{htest}

