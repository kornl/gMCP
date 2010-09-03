\name{gMCP-package}
\alias{gMCP-package}
\alias{gMCP}
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
Version: \tab 0.1\cr
Date: \tab 2010-08-03\cr
License: \tab GPL (>= 2)\cr
LazyLoad: \tab yes\cr
}

1. Creating a object of \code{\link{graphSRMTP}} which is a subclass of \code{\link[graph:graphNEL-class]{graphNEL}} from package \code{graph} that contains some additional information.
This can be done directly via the \code{\link{new}} method or a graphical user interface by calling \code{\link{graphGUI}}.

2. Calling \code{\link{srmtp}} or srmtpGUI. This can create also pdf reports.

}
\author{
Kornelius Rohmeyer 

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
 \item Apache POI  under Apache License 2.0, \url{http://poi.apache.org/}, Copyright 2001-2007 The Apache Software Foundation 
 \item JGoodies Forms  under BSD License, \url{http://www.jgoodies.com/freeware/forms/}, Copyright JGoodies Karsten Lentzsch 
 \item JGoodies Looks  under BSD License, \url{http://www.jgoodies.com/downloads/libraries.html}, Copyright (c) 2001-2008 JGoodies Karsten Lentzsch 
 \item jog4j  under Apache License 2.0, \url{http://logging.apache.org/log4j/}, Copyright 2007 The Apache Software Foundation 
 \item iText 2.1.2  under Lesser General Public License (LGPL), \url{http://itextpdf.com/}, Copyright by Bruno Lowagie 
 \item SwingWorker  under LGPL, \url{https://swingworker.dev.java.net/}, Copyright (c) 2005 Sun Microsystems 
 \item Commons Lang  under the Apache License, Version 1.1, 2002, \url{http://commons.apache.org/lang/}, Copyright 2001-2008 The Apache Software Foundation 
 \item Commons Logging  under the Apache License, Version 2.0, January 2004, \url{http://commons.apache.org/logging/}, Copyright 2001-2007 The Apache Software Foundation 
 \item Commons Validator  under the Apache License, Version 2.0, January 2004, \url{http://commons.apache.org/validator/}, Copyright 2001-2006 The Apache Software Foundation
 \item Filters  under Apache License, Version 2.0, January 2004, \url{http://www.jhlabs.com/ip/filters/index.html}, Copyright by Jerry Huxtable 
 \item Java Help  under Java Help License, \url{http://java.sun.com/javase/technologies/desktop/javahelp/}, Copyright 2003-2007 Sun Microsystems 
 \item JXLayer  under BSD License, \url{https://jxlayer.dev.java.net/}, Copyright 2006-2009, Alexander Potochkin 
 \item Java Mail  under Java Mail License, \url{http://java.sun.com/products/javamail/downloads/index.html}, Copyright (c) 2009 Sun Microsystems, Inc. 
 \item SwingX  under LGPL, \url{https://swingx.dev.java.net/}, Copyright (c) 2005-2006 Sun Microsystems, Inc. 
 \item afcommons  under BSD License, \url{http://www.algorithm-forge.com/afcommons/}, Copyright (c) 2007-2010 by Kornelius Rohmeyer and Bernd Bischl 
 \item SGTK  under BSD License, \url{http://www.algorithm-forge.com/rjavaclient/}, Copyright (c) 2007-2010 by Kornelius Rohmeyer and Bernd Bischl
 } 
}
\keyword{ package }
\keyword{ graphs }
\keyword{ htest }
%\seealso{
%\code{\link[mutoss:Mutoss]{Mutoss}}
%}
\examples{
g5 <- createBonferroniHolmGraph(5)
\dontrun{graphGUI("g5")}
srmtp(g5, pvalues=c(0.1,0.2,0.4,0.4,0.4))
}
