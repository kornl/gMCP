\name{createGraphGUI}
\alias{createGraphGUI}

\title{
	Graphical User Interface for the Creation of SRMTP Graphs
}
\description{
	Starts a graphical user interface for the creation of SRMTP graphs.
}
\usage{
	createGraphGUI(createdGraph="createdGraph")
}

\arguments{
  \item{createdGraph}{
	Variable name for the graph, given as a character string.
  }
}
\details{
	The result is a complete graph, the alpha is split equally between all hypotheses and each edge weight is \eqn{\frac{1}{n-1}}{1/(n-1)}.
}
\value{
	The function itself returns NULL. But with the GUI a graph can be created that is available in R under the specified variable name.
}
\author{
Kornelius Rohmeyer <rohmeyer@small-projects.de>
}

\examples{

\dontrun{createGraphGUI()}

}
\keyword{ misc }

