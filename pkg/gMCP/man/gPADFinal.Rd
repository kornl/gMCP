\name{gPADFinal-class}
\docType{class}
\alias{gPADFinal-class}
\alias{gPADFinal}
\alias{print,gPADFinal-method}
\alias{plot,gPADFinal-method}
\alias{getWeights,gPADFinal-method}
\alias{getRejected,gPADFinal-method}
\title{Class gPADFinal}
\description{A gPADFinal object describes holds information about the
  final analysis of an adaptive graph based trial testing multiple hypotheses.}
\section{Slots}{
  \describe{
    \item{\code{fweights}:}{\code{numeric} matrix of partial conditional
      error allocation ratios (proportion of the sum of partial
      conditional error (BJ) that each elementary hypotheses receives)} 
    \item{\code{BJ}:}{A \code{numeric} specifying the sum of PCEs per
      intersection hypothesis.}
    \item{\code{z2}:}{The \code{numeric} vector of z-scores observed at
      the final analysis.}
    \item{\code{final}:}{Object of class \code{\link{graphMCP}}
      specifying the graphical weighting procedure applied in the second
      stage.}
    \item{\code{alpha}:}{A \code{numeric} giving the alpha level of the
      pre-planned test}
    \item{\code{rejected}}{A \code{logical} vector specifying which
      hypotheses can be rejected at the final analysis}
    \item{\code{frb}}{A \code{matrix} giving the final rejection boundaries
      applied in the final closed testing procedure}
  }
}
\author{Florian Klinglmueller \email{float@lefant.net}}
\seealso{
\code{\link{gMCP}}, \code{\link{doInterim}}, \code{\link{secondStageTest}}
}
\keyword{ graphs }