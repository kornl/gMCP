\name{graphGUI}
\alias{graphGUI}

\title{
	Graphical User Interface for the Creation of SRMTP Graphs
}
\description{
	Starts a graphical user interface for the creation of SRMTP graphs.
}
\usage{
	graphGUI(createdGraph="createdGraph")
}

\arguments{
  \item{graph}{
	Variable name for the graph, given as a character string.
  }
}
\details{
	See the HTML help in the GUI for further details.
}
\value{
	The function itself returns NULL. But with the GUI a graph can be created or edited that is available in R under the specified variable name.
}
\author{
Kornelius Rohmeyer <rohmeyer@small-projects.de>
}

\examples{

\dontrun{graphGUI()}

}
\keyword{ misc }
\keyword{ graphs }

