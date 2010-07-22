\name{graphSRMTP-class}
\docType{class}
\alias{graphSRMTP-class}
\alias{graphSRMTP}
\title{Class graphSRMTP}
\description{A graphSRMTP object describes a sequentially rejective multiple test procedure.}
\section{Slots}{
  Only slot alpha is new in class graphSRMTP. The remaining slots are inherited from \code{\link[graph:graphNEL-class]{graphNEL}} and \code{\link[graph:graph-class]{graph}}
  \describe{
    \item{\code{alpha}:}{A numeric for the initial levels of alpha.}
    \item{\code{nodes}:}{Object of class \code{vector}}
    \item{\code{edgeL}:}{Object of class \code{list}. The \code{edgeL}
      must be the same length as \code{nodes}. The elements of this
      vector correspond to the same element in \code{nodes}. The
      elements are themselves lists. If the node has any edges then this
      list will have an element named \code{edges}.}        
    \item{\code{edgeData}:}{An \code{attrData} instance for edge attributes.}
    \item{\code{nodeData}:}{An \code{attrData} instance for node attributes.}
    \item{\code{graphData}:}{A \code{list} for graph-level
      attributes. Only mandatory list item is \code{edgemode} which
      indicates whether edges are \code{"directed"} or
      \code{"undirected"}}
    \item{\code{renderInfo}:}{A \code{list} of graph rendering information.}
  }
}
\author{Kornelius Rohmeyer \email{rohmeyer@small-projects.de}}
\seealso{
\code{\link[graph:graphNEL-class]{graphNEL}}
}
\keyword{ graphs }