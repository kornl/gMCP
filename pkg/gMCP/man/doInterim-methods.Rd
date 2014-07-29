\docType{methods}
\name{doInterim}
\alias{doInterim}
\alias{doInterim,graphMCP-method}
\title{EXPERIMENTAL: Evaluate conditional errors at interim for a pre-planned
graphical procedure}
\arguments{
  \item{object}{A graph of class \code{\link{graphMCP}}}

  \item{z1}{A numeric vector giving the observed interim
  z-scores.}

  \item{v}{Timings of the interim analyses as proportions
  of the preplanned measurements (see Details).}

  \item{alpha}{A numeric specifying the level of family
  wise error rate control.}

  \item{gSB}{group sequential boundaries function (see
  details)}

  \item{cur.stage}{number of the stage after which the
  interim analysis is performed}
}
\value{
  An object of class \code{agMCPInterim}, more specifically
  a list with elements

  \item{Aj}{a matrix of PCEs for all elementary hypotheses
  in each intersection hypothesis}

  \item{BJ}{a numeric vector giving sum of PCEs per
  intersection hypothesis}

  \item{preplanned}{Pre planned test represented by an
  object of class \code{\link{graphMCP}}}
}
\description{
  Computes partial conditional errors (PCE) for a
  pre-planned graphical procedure given information
  fractions and first stage z-scores. If a function giving
  group sequential boundaries is specified for each
  elementary hypotheses PCEs are computed for the
  corresponding group sequential trial - Implementation of
  adaptive procedures is still in an early stage and may
  change in the near future
}
\details{
  At the moment partial conditional error rates are only
  computed assuming that the trial is already at at the
  last preplanned interim analysis before the final
  analysis. E.g. if a three stage group sequential trial is
  planned we assume that the adaptive interim analysis is
  performed after the second stage. Early stopping because
  an interim test statistic crosses an early rejection
  boundary at some previous stage can be implemented by
  setting the corresponding z statistics to Inf.

  Group sequential boundaries have to be specified using a
  function of the form \code{function(w,v,alpha)} - where w
  is a vector of weights with length equal to the number of
  elementary hypotheses, \code{v} the timing of the interim
  analysis, and \code{alpha} the overall alpha level -
  which returns a matrix where each column corresponds to
  an elementary hypotheses and rows give rejection
  boundaries corresponding the preplanned number of stages;
  the last row corresponds to the final rejection boundary.
  See \code{link{agMCPldbounds}}.

  If \code{v} is a scalar the same interim timing will be
  used for all elementary hypotheses. If \code{v} is a
  vector it will be interpreted as timings for the interim
  stages, assuming equal stage-wise timings across
  elementary hypotheses. If \code{v} is a matrix each row
  gives the timings of interim analyses for a particular
  hypothesis. Note, that with differing interim timings
  additional distributional assumptions may be required to
  guarantee type I error control!

  For details see the given references.
}
\examples{
## Simple successive graph (Maurer et al. 2011)
## two treatments two hierarchically ordered endpoints
a <- .025
G <- simpleSuccessiveI()
## some z-scores:

p1=c(.1,.12,.21,.16)
z1 <- qnorm(1-p1)
p2=c(.04,1,.14,1)
z2 <- qnorm(1-p2)
v <- 1/2

intA <- doInterim(G,z1,v)

## select only the first treatment
fTest <- secondStageTest(intA,c(1,0,1,0),zWeights='reject')
fTest(z2)
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

  Frank Bretz, Martin Posch, Ekkehard Glimm, Florian
  Klinglmueller, Willi Maurer, Kornelius Rohmeyer (2011):
  Graphical approaches for multiple comparison procedures
  using weighted Bonferroni, Simes or parametric tests.
  Biometrical Journal 53 (6), pages 894-913, Wiley.
  \url{http://onlinelibrary.wiley.com/doi/10.1002/bimj.201000239/full}

  Posch M, Futschik A (2008): A Uniform Improvement of
  Bonferroni-Type Tests by Sequential Tests JASA 103/481,
  299-308

  Posch M, Maurer W, Bretz F (2010): Type I error rate
  control in adaptive designs for confirmatory clinical
  trials with treatment selection at interim Pharm Stat
  10/2, 96-104
}
\seealso{
  \code{\link{graphMCP}}, \code{\link{secondStageTest}}
}
\keyword{graphs}
\keyword{htest}

