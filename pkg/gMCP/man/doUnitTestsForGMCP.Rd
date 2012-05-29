\name{doUnitTestsForGMCP}
\alias{doUnitTestsForGMCP}

\title{
	Run the R unit test suite for gMCP
}
\description{
	Runs the R unit test suite for gMCP and prints the results.
}
\usage{
	doUnitTestsForGMCP(extended=FALSE)
}

\arguments{
  \item{extended}{
	If \code{TRUE} an extended version of the R unit test suite for gMCP will be used.
	The run will take significantly longer time and only cover further cases of the same
	kind that also tested in the non-extended version.
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

