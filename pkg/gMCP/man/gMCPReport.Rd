\name{gMCPReport}
\alias{gMCPReport}

\title{
	Automatic Generation of gMCP Reports
}
\description{
	Creates a LaTeX file with a gMCP Report.
}
\usage{
	gMCPReport(object, file="", ...)
}

\arguments{
  \item{object}{
	A graph of class \code{\link{graphMCP}} or an object of class \code{\link{gMCPResult}}.
  }
  \item{file}{ 
  	A connection, or a character string naming the file to print
    to.  If \code{""} (the default), the report is printed to the standard
    output connection (the console unless redirected by \code{sink}).
    If it is \code{"|cmd"}, the output is piped to the command given
    by \code{cmd}, by opening a pipe connection [taken from the manual page
    of \code{cat}, which is called in this function].
  }
  \item{...}{
  	Arguments to be passed to method \code{\link{graph2latex}} like \code{package} and \code{scale}.
  }   
}
\details{
	This function uses \code{cat} and \code{graph2latex}.
}
\value{
	None (invisible \code{NULL}).
}
\references{
	The TikZ and PGF Packages Manual for version 2.00,
	Till Tantau,
	\url{http://www.ctan.org/tex-archive/graphics/pgf/base/doc/generic/pgf/pgfmanual.pdf}
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\seealso{
	\code{\link{cat}}
	\code{\link{graph2latex}}
}
\examples{

g <- graphFromBretzEtAl2009()

result <- gMCP(g, pvalues=c(0.1, 0.008, 0.005, 0.15, 0.04, 0.006))

gMCPReport(result)

}
\keyword{ print }
\keyword{ IO }
\keyword{ file }
\keyword{ graphs }

