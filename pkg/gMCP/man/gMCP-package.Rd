\name{gMCP-package}
\alias{gMCP-package}
\docType{package}
\title{
A graphical approach to sequentially rejective multiple test procedures
}
\description{
This package provides functions and graphical user interfaces for graphical described sequentially rejective multiple test procedures.
}
\details{
\tabular{ll}{
Package: \tab gMCP\cr
Type: \tab Package\cr
%Version: \tab 0.1\cr
%Date: \tab 2010-08-03\cr
License: \tab GPL (>= 2)\cr
LazyLoad: \tab yes\cr
}

The package gMCP helps with the following steps of performing a sequentially rejective multiple test procedure:

1. Creating a object of \code{\link{graphMCP}} (which is a subclass of \code{\link[graph:graphNEL-class]{graphNEL}} from package \code{graph} that contains some additional information)
that represents a sequentially rejective multiple test procedure.
This can be done directly via the \code{\link{new}} method or a graphical user interface by calling \code{\link{graphGUI}}.

2. Calling \code{\link{gMCP}} or \code{\link{graphGUI}}.

3. Exporting the results (optional with all sequential steps) as LaTeX or PDF report.

}
\author{
Kornelius Rohmeyer, R code for correlated tests from Florian Klinglmueller

Maintainer: Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}
\references{
Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch:
A graphical approach to sequentially rejective multiple test procedures.
Statistics in Medicine 2009 vol. 28 issue 4 page 586-604. 
\url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}
}
\note{
We use the following Java libraries:
\itemize{
 \item Apache Commons Logging under the Apache License, Version 2.0, January 2004, \url{http://commons.apache.org/logging/}, Copyright 2001-2007 The Apache Software Foundation 
 \item Apache jog4j under Apache License 2.0, \url{http://logging.apache.org/log4j/}, Copyright 2007 The Apache Software Foundation
 \item Apache Commons Lang under  Apache License 2.0, \url{http://commons.apache.org/lang/}, Copyright 2001-2011 The Apache Software Foundation
 \item JLaTeXMath under GPL >= 2.0, \url{http://forge.scilab.org/index.php/p/jlatexmath/}, Copyright 2004-2007, 2009 Calixte, Coolsaet, Cleemput, Vermeulen and Universiteit Gent
 \item JRI under Lesser General Public License (LGPL) 2.1, \url{http://www.rforge.net/rJava/}, Copyright 2004-2007 Simon Urbanek 
 \item iText 2.1.4 under LGPL, \url{http://itextpdf.com/}, Copyright by Bruno Lowagie 
 \item SwingWorker under LGPL, \url{https://swingworker.dev.java.net/}, Copyright (c) 2005 Sun Microsystems 
 \item JXLayer under BSD License, \url{https://jxlayer.dev.java.net/}, Copyright 2006-2009, Alexander Potochkin 
 \item JGoodies Forms under BSD License, \url{http://www.jgoodies.com/freeware/forms/}, Copyright JGoodies Karsten Lentzsch  
 \item AFCommons under BSD License, \url{http://www.algorithm-forge.com/afcommons/}, Copyright (c) 2007-2010 by Kornelius Rohmeyer and Bernd Bischl
 \item JHLIR under BSD License, \url{http://jhlir.r-forge.r-project.org/}, Copyright (c) 2008-2010 by Bernd Bischl and Kornelius Rohmeyer  
 \item SGTK under BSD License, \url{http://www.algorithm-forge.com/rjavaclient/}, Copyright (c) 2007-2010 by Bernd Bischl and Kornelius Rohmeyer 
 } 
}
\keyword{ package }
\keyword{ graphs }
\keyword{ htest }
%\seealso{
%\code{\link[mutoss:Mutoss]{Mutoss}}
%}
\examples{
g5 <- BonferroniHolm(5)
\dontrun{
graphGUI("g5")}
gMCP(g5, pvalues=c(0.1,0.2,0.4,0.4,0.4))
}
