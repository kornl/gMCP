\name{unitTestsGMCP}
\alias{unitTestsGMCP}

\title{
	Run the R unit (and optional the JUnit) test suite for gMCP
}
\description{
	Runs the R unit (and optional the JUnit) test suite for gMCP and prints the results.
}
\usage{
	unitTestsGMCP(extended=FALSE, java=FALSE, junitLibrary)
}

\arguments{
  \item{extended}{
	If \code{TRUE} an extended version of the R unit test suite for gMCP will be used.
	The run will take significantly longer time and only cover further cases of the same
	kind that also tested in the non-extended version.
  }
  \item{java}{
    If \code{TRUE} the GUI and its logic is tested with JUnit tests. You need 
    JUnit 4 classes in the classpath or specify the path to a JUnit 4 jar file
    via the parameter junitLibrary. 
  } 
  \item{junitLibrary}{
    A character String specifying the path to a JUnit 4 jar file to run the JUnit tests.
    You can download it from \url{http://www.junit.org/}.
  }
}
\value{
  None of interest so far - the function prints the results to the standard output.
  (Perhaps in future versions a value will be returned that can be processed by the GUI.)
}
\author{
  Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}
\examples{

\dontrun{
doUnitTestsForGMCP()

}

}

