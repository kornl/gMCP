\name{CommonJavaJars-package}
\alias{CommonJavaJars-package}
\docType{package}
\title{
Useful libraries for building a Java based GUI under R
}
\description{
Useful libraries for building a Java based GUI under R
}
\details{
\tabular{ll}{
Package: \tab CommonJavaJars\cr
Type: \tab Package\cr
%Version: \tab 0.1\cr
%Date: \tab 2010-08-03\cr
License: \tab GPL-2\cr
LazyLoad: \tab yes\cr
}

Since the CommonJavaJars package has relatively big library files (~ 7MByte)
and should rarely be updated to save space in the CRAN archive,
the more dynamic accompanying guide can be found at \url{http://algorithm-forge.com/JavaGUI/}.

}
\author{
Kornelius Rohmeyer (author only of two libraries and of the R code for loading them), see the copyright note for the authors of the other java libraries.

Maintainer: Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}
\note{
We provide the following Java libraries:
\itemize{
 \item Apache Commons Logging under the Apache License, Version 2.0, January 2004, \url{http://commons.apache.org/logging/}, Copyright 2001-2007 The Apache Software Foundation 
 \item Apache jog4j under Apache License 2.0, \url{http://logging.apache.org/log4j/}, Copyright 2007 The Apache Software Foundation
 \item Apache Commons Lang under  Apache License 2.0, \url{http://commons.apache.org/lang/}, Copyright 2001-2011 The Apache Software Foundation
 \item Apache Commons Collections under the Apache License, Version 2.0, January 2004, \url{http://commons.apache.org/collections/}, Copyright 2001-2008 The Apache Software Foundation
 \item Apache Commons Validator under the Apache License, Version 2.0, January 2004, \url{http://commons.apache.org/validator/}, Copyright 2001-2010 The Apache Software Foundation
 \item Apache Commons IO under the Apache License, Version 2.2 (last compatible with JDK 1.5), 2004, \url{http://commons.apache.org/io/}, Copyright 2002-2012 The Apache Software Foundation
 \item JLaTeXMath under GPL >= 2.0, \url{http://forge.scilab.org/index.php/p/jlatexmath/}, Copyright 2004-2007, 2009 Calixte, Coolsaet, Cleemput, Vermeulen and Universiteit Gent
 \item iText 2.1.4 under LGPL, \url{http://itextpdf.com/}, Copyright by Bruno Lowagie 
 \item SwingWorker under LGPL, \url{https://swingworker.dev.java.net/}, Copyright (c) 2005 Sun Microsystems 
 \item JXLayer under BSD License, \url{https://jxlayer.dev.java.net/}, Copyright 2006-2009, Alexander Potochkin 
 \item JGoodies Forms and Commons under BSD License, \url{http://www.jgoodies.com/freeware/forms/}, Copyright JGoodies Karsten Lentzsch  
 \item JSON Processing dual licensed under CDDL 1.1 and GPL 2 with Classpath Exception, \url{https://jsonp.java.net/}, Copyright (c) 2011-2013 Oracle
 \item AFCommons under the LGPL >= 2.1, \url{https://github.com/kornl/afcommons/}, Copyright (c) 2007-2014 by Kornelius Rohmeyer, Bernd Bischl, Santhosh Kumar T (ComponentTitledBorder class), Jerry Huxtable (filter code), Sebastian Haufe (JListDnD class), Romain Guy (Star class), Oracle (CloseTabPanel)
 \item JHLIR under BSD License, \url{http://jhlir.r-forge.r-project.org/}, Copyright (c) 2008-2010 by Bernd Bischl and Kornelius Rohmeyer  
 \item JRIEngine and REngine under LGPL-2.1, \url{http://www.rforge.net/rJava/}, Copyright 2010 by Simon Urbanek. Please use these two jar files only if package rJava does not provide them. 
 } 
}
\keyword{ package }

\examples{

\dontrun{
showGuideForJavaGUIs()

}
}
