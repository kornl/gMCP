\name{graphGUI}
\alias{graphGUI}

\title{
	Graphical User Interface for the Creation of SRMTP Graphs
}
\description{
	Starts a graphical user interface for the creation of SRMTP graphs.
}
\usage{
	graphGUI(graph="createdGraph", pvalues=numeric(0), grid=10, debug=FALSE)
}

\arguments{
  \item{graph}{
  	Either a variable name for the graph, given as a character string. (If it is not a syntactically valid name, \code{\link{make.names}} is called to change it to a valid one.)
  	Or an object of class \code{\link{graphMCP}}.
  }
  \item{pvalues}{
	Numeric value that optionally specifies the p-values. 
  }
  \item{grid}{
	Positive integer that sets the grid size for easier placement of nodes.
	(Grid size 1 disables the grid.)
  }
  \item{debug}{
	Logical. If \code{TRUE} debug output is printed to the R console.
  }
}
\details{
	See the vignette of this package for further details, since describing a GUI interface is better done with a lot of nice pictures.
}
\value{
	The function itself returns NULL. But with the GUI a graph can be created or edited that will be available in R under the specified variable name after saving.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\examples{

\dontrun{
graphGUI()
pvalues <- c(9.7, 1.5, 0.5, 0.6, 0.4, 0.8, 4)/100 
graphGUI(createGraphFromHommelEtAl(), pvalues=pvalues))}

}
\keyword{ misc }
\keyword{ graphs }

