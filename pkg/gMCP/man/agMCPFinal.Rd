\name{agMCPFinal-class}
\docType{class}
\alias{agMCPFinal-class}
\alias{agMCPFinal}
\alias{print,agMCPFinal-method}
\alias{plot,agMCPFinal-method}
\alias{getWeights,agMCPFinal-method}
\alias{getRejected,agMCPFinal-method}
\title{Class agMCPFinal}
\description{A \code{\link{agMCPFinal}} object describes the result of the analysis form an adaptive graph based multiple comparison procedure.}
\section{Slots}{
  \describe{
\item{\code{fweights}:}{Matrix of weights applied in the final closed test procedure}
\item{\code{BJ}:}{Vector of sums of partial conditional errors from computed at the last interim analysis}
\item{\code{z2}:}{z-scores from the last stage observed at the final analysis}
\item{\code{final}:}{Object of class \code{\link{graphMCP}} specifying the test applied at the final stage}
\item{\code{alpha}:}{Prespecified alpha level at which FWER is controlled.}
\item{\code{rejected}:}{Vector indicating which elementary hypotheses were rejected at the final analysis}
\item{\code{frb}:}{Matrix with rejection boundaries applied at the final analysis.}
  }
}
\author{Florian Klinglmueller \email{float@lefant.net}}
\seealso{
\code{\link{gMCP}}, \code{\link{doFinal}}, \code{\link{secondStageTest}}
}
\keyword{ graphs }