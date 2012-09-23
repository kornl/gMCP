\name{loadJars}
\alias{loadJars}

\title{
	Registers all Java classes from the specified jar files from this package.
}
\description{
	Registers all Java classes from the specified jar files from this package.
}
\usage{
	loadJars(jars, java="J5")
}
\arguments{
  \item{jars}{
	A character vector containing the names of the jar files to load.
	Since the version numbers of the jar files in CommonJavaJars can change,
	best practice is to specify only an unambiguous part of the name, if there
	are no reasons not to use the latest version. See the example section.
  }
  \item{java}{
	This character string specifies the Java version all jar files should be compatible with.
	For example JGoodies Forms requires Java 6 in version 1.6.0.
	The default \code{java="J5"} will therefore load version 1.2.0 of JGoodies Forms.
  }
}
\details{
	Registers all Java classes from the specified jar files from this package.
}
\value{
	The function returns the full names (sometimes including the version number) 
	of the loaded jar files. (Invisible character vector.) 
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\examples{

\dontrun{
jars <- c("commons-collections", "commons-lang", "commons-logging", 
          "commons-validator", "forms",	"iText", "jhlir.jar", 
          "jlatexmath", "jxlayer", "log4j", "mysql-connector-java", 
		  "swing-worker")
		  
fullNames <- loadJars(jars)

fullNames
}
}
\keyword{ misc }


