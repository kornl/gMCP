\name{srmtpResult-class}
\docType{class}
\alias{srmtpResult-class}
\alias{srmtpResult}
\alias{print,srmtpResult-method}
\alias{plot,srmtpResult-method}
\alias{getAlpha,srmtpResult-method}
\alias{getRejected,srmtpResult-method}
\title{Class srmtpResult}
\description{A srmtpResult object describes an evaluated sequentially rejective multiple test procedure.}
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