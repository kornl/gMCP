\name{graphGUI}
\alias{graphGUI}

\title{
	Graphical User Interface for the Creation of SRMTP Graphs
}
\description{
	Starts a graphical user interface for the creation of SRMTP graphs.
}
\usage{
	graphGUI(graph="createdGraph", debug=FALSE)
}

\arguments{
  \item{graph}{
	Variable name for the graph, given as a character string.
	If it is not a syntactically valid name,
	\code{\link{make.names}} is called to change it to a valid one.
  }
  \item{debug}{
	Logical. If \code{TRUE} debug output is printed to the R console.
  }
}
\details{
	See the HTML help in the GUI for further details.
}
\value{
	The function itself returns NULL. But with the GUI a graph can be created or edited that is available in R under the specified variable name.
}
\author{
	Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\examples{

\dontrun{
graphGUI()
}

}
\keyword{ misc }
\keyword{ graphs }

