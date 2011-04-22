\name{Example Graphs}
\alias{createBonferroniHolmGraph}
\alias{createGraphFromBretzEtAl}
\alias{createGraph2FromBretzEtAl}
\alias{createGraphForParallelGatekeeping}
\alias{createGraphForImprovedParallelGatekeeping}
\alias{createGraphFromHommelEtAl}
\alias{createGraphFromHungEtWang}
\title{
	Functions that create different example graphs
}
\description{ 
	Functions that creates example graphs, e.g. graphs that represents a Bonferroni-Holm adjustment, parallel gatekeeping or special procedures from selected papers.
	
	We are providing functions and not the resulting graphs directly because this way you have additional examples:
	You can look at the function body with \code{\link{body}} and see how the graph is built.
}
\usage{
	createBonferroniHolmGraph(n)
	createGraphFromBretzEtAl()
	createGraph2FromBretzEtAl()
	createGraphFromHommelEtAl()
	createGraphForParallelGatekeeping()
	createGraphForImprovedParallelGatekeeping()
	createGraphFromHungEtWang()
}

\arguments{
  \item{n}{
	Number of hypotheses.
  }
}
\details{
  \describe{
	\item{\code{createBonferroniHolmGraph}}{
		Returns a graph that represents a Bonferroni-Holm adjustment.
		The result is a complete graph, where all nodes have the same weights and each edge weight is \eqn{\frac{1}{n-1}}{1/(n-1)}.
	}
	\item{\code{createGraphFromBretzEtAl}}{
		Example graph in figure 2 from Bretz et al. See references (Bretz et al. 2009).
	}
	\item{\code{createGraph2FromBretzEtAl}}{
		Example graph in figure 6 from Bretz et al. See references (Bretz et al. 2009).
	}
	\item{\code{createGraphFromBretzEtAl}}{
		Example graph from Hommel et al. See references (Hommel et al. 2007).
	}
	\item{\code{createGraphForParallelGatekeeping}}{
		Example graph for parallel gatekeeping. See references (Dmitrienko et al. 2003).
	}
	\item{\code{createGraphForImprovedParallelGatekeeping}}{
		Example graph for improved parallel gatekeeping. See references (Hommel et al. 2007).
	}
	\item{\code{createGraphFromHungEtWang}}{
	    Example graph from Hung et Wang. See references (Hung et Wang 2010).
	}
  }	
}
\value{
	A graph of class \code{\link{graphMCP}} that represents a sequentially rejective multiple test procedure.
}
\references{
Holm, S. (1979). A simple sequentally rejective multiple test procedure. Scandinavian Journal of Statistics 6, 65-70.

Dmitrienko, A., Offen, W., Westfall, P.H. (2003). Gatekeeping strategies for clinical trials that do not require all primary effects to be significant. Statistics in Medicine. 22, 2387-2400.

Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch: A graphical approach to sequentially rejective multiple test procedures.
Statistics in Medicine 2009 vol. 28 issue 4 page 586-604. \url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}

Hommel, G., Bretz, F. und Maurer, W. (2007). Powerful short-cuts for multiple testing procedures with special reference to gatekeeping strategies. Statistics in Medicine, 26(22), 4063-4073.

Hung H.M.J., Wang S.-J. (2010). Challenges to multiple testing in clinical trials. Biometrical Journal 52, 747-756.
}
\author{
Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\examples{

g <- createBonferroniHolmGraph(5)

\dontrun{
# If Rgraphviz is installed, we can take a look at the graph:
library(Rgraphviz)
renderGraph(layoutGraph(g))
}

gMCP(g, pvalues=c(0.1, 0.2, 0.4, 0.4, 0.7))

}
\keyword{ misc }
\keyword{ graphs }

