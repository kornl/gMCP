\name{gMCPResult-class}
\docType{class}
\alias{gMCPResult-class}
\alias{gMCPResult}
\alias{print,gMCPResult-method}
\alias{plot,gMCPResult-method}
\alias{getAlpha,gMCPResult-method}
\alias{getRejected,gMCPResult-method}
\title{Class gMCPResult}
\description{A gMCPResult object describes an evaluated sequentially rejective multiple test procedure.}
\section{Slots}{
  \describe{
    \item{\code{graphs}:}{Object of class \code{list}. }        
    \item{\code{pvalues}:}{The \code{numeric} vector of pvalues. }
    \item{\code{adjPValues}:}{The \code{numeric} vector of adjusted pvalues. }    
  }
}
\author{Kornelius Rohmeyer \email{rohmeyer@small-projects.de}}
\seealso{
\code{\link{srmtp}}
}
\keyword{ graphs }