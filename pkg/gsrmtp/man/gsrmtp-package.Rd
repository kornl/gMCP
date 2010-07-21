\name{gsrmtp-package}
\alias{gsrmtp-package}
\alias{gsrmtp}
\docType{package}
\title{
A graphical approach to sequentially rejective multiple test procedures
}
\description{
This package provides functions and graphical user interfaces for graphical described sequentially rejective multiple test procedures.
}
\details{
\tabular{ll}{
Package: \tab gsrmtp\cr
Type: \tab Package\cr
Version: \tab 0.1\cr
Date: \tab 2010-07-21\cr
License: \tab GPL (>= 2)\cr
LazyLoad: \tab yes\cr
}

1. Creating a object of \code{\link{graphSRMTP}} which is a subclass of \code{\link[graph:graphNEL-class]{graphNEL}} from package \code{graph} that contains some additional information.
This can be done directly via the \code{\link{new}} method or a graphical user interface by calling \code{\link{graphGUI}}.

2. Calling \code{\link{srmtp}} or srmtpGUI. This can create also pdf reports.

}
\author{
Kornelius Rohmeyer 

Maintainer: Kornelius Rohmeyer <rohmeyer@small-projects.de>
}
\references{
Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch:
A graphical approach to sequentially rejective multiple test procedures.
Statistics in Medicine 2009 vol. 28 issue 4 page 586-604. 
\url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}
}
\keyword{ package }
\keyword{ graphs }
\keyword{ htest }
\seealso{
\code{\link[mutoss:Mutoss]{Mutoss}}
}
\examples{
g5 <- createBonferroniHolmGraph(5)
\dontrun{graphGUI("g5")}
srmtp(g5, pvalues=c(0.1,0.2,0.4,0.4,0.4))
}
