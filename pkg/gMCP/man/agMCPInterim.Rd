\name{agMCPInterim-class}
\docType{class}
\alias{agMCPInterim-class}
\alias{agMCPInterim}
\alias{print,agMCPInterim-method}
\alias{plot,agMCPInterim-method}
\alias{getWeights,agMCPInterim-method}
\alias{getRejected,agMCPInterim-method}
\title{Class agMCPInterim}
\description{A agMCPInterim object describes an object holding interim
  information for an adaptive procedure that is based on a preplanned
  graphical procedure.}
\section{Slots}{
  \describe{
    \item{\code{Aj}:}{Object of class \code{numeric}. Giving partial
      conditional errors (PCEs) for all elementary hypotheses in each
      intersection hypothesis }        
    \item{\code{BJ}:}{A \code{numeric} specifying the sum of PCEs per
      intersection hypothesis.}
    \item{\code{z1}:}{The \code{numeric} vector of first stage
      z-scores.}
    \item{\code{v}:}{A \code{numeric} specifying the proportion of
      measurements collected up to interim}
    \item{\code{preplanned}:}{Object of class \code{\link{graphMCP}}
      specifying the preplanned graphical procedure.}
    \item{\code{alpha}:}{A \code{numeric} giving the alpha level of the
      pre-planned test}
    \item{\code{rejected}}{A \code{logical} vector specifying whether
      any elementary hypotheses has been rejected at interim}
    \item{\code{erb}}{A \code{matrix} giving early rejection boundaries
      for group sequential trials}
  }
}
\author{Florian Klinglmueller \email{float@lefant.net}}
\seealso{
\code{\link{gMCP}}, \code{\link{doInterim}}, \code{\link{secondStageTest}}
}
\keyword{ graphs }