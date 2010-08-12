\name{createGsrmtpReport}
\alias{createGsrmtpReport}

\title{
	Automatic Generation of GRSMTP Reports
}
\description{
	Creates a LaTeX file with a GRSMTP Report.
}
\usage{
	createGsrmtpReport(srmtpResult, file, package="TikZ")
}

\arguments{
  \item{srmtpResult}{
	A graph of class \code{\link{srmtpResult}}.
  }
  \item{file}{ A connection, or a character string naming the file to print
    to.  If \code{""} (the default), the report is printed to the standard
    output connection, the console unless redirected by \code{sink}.
    If it is \code{"|cmd"}, the output is piped to the command given
    by \code{cmd}, by opening a pipe connection.
  }
  \item{package}{
  	A character string specifying the LaTeX package that should be used.
  	Up to now only \code{TikZ} is available.
  }   
}
\details{
	This function uses \code{cat} and \code{graph2latex}.
}
\value{
	None (invisible \code{NULL}).
}
\references{
	The Tik Z and PGF Packages Manual for version 2.00,
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

g <- createBonferroniHolmGraph(5)

result <- srmtp(g, pvalues=c(0.01, 0.02, 0.04, 0.4, 0.7))

createGsrmtpReport(result)

}
\keyword{ print }
\keyword{ IO }
\keyword{ file }
\keyword{ graphs }

