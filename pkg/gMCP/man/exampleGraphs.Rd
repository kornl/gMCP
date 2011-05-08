\name{Example Graphs}
\alias{exampleGraph}
\alias{BonferroniHolmGraph}
\alias{graphFromBretzEtAl2011}
\alias{graph2FromBretzEtAl2011}
\alias{graphForParallelGatekeeping}
\alias{graphForImprovedParallelGatekeeping}
\alias{graphFromHommelEtAl2007}
\alias{graphFromHungEtWang2010}
\alias{graphFromMaurerEtAl1995}
\alias{cycleGraph}
\title{
	Functions that create different example graphs
}
\description{ 
	Functions that creates example graphs, e.g. graphs that represents a Bonferroni-Holm adjustment, parallel gatekeeping or special procedures from selected papers.
	
	We are providing functions and not the resulting graphs directly because this way you have additional examples:
	You can look at the function body with \code{\link{body}} and see how the graph is built.
}
\usage{
	BonferroniHolmGraph(n)
	graphForParallelGatekeeping()
	graphForImprovedParallelGatekeeping()
	graphFromBretzEtAl2011()
	graph2FromBretzEtAl2011()
	graphFromHungEtWang2010()
	graphFromHommelEtAl2007()
	graphFromMaurerEtAl1995()
	cycleGraph(nodes, weights)
}

\arguments{
  \item{n}{
	Number of hypotheses.
  }
  \item{nodes}{
  	Character vector of node names.
  }
  \item{weights}{
  	Numeric vector of node weights.
  }
}
\details{
  \describe{
	\item{\code{BonferroniHolmGraph}}{
		Returns a graph that represents a Bonferroni-Holm adjustment.
		The result is a complete graph, where all nodes have the same weights and each edge weight is \eqn{\frac{1}{n-1}}{1/(n-1)}.
	}
	\item{\code{graphFromBretzEtAl2011}}{
		Example graph in figure 2 from Bretz et al. See references (Bretz et al. 2011).
	}
	\item{\code{graph2FromBretzEtAl2011}}{
		Example graph in figure 6 from Bretz et al. See references (Bretz et al. 2011).
	}
	\item{\code{graphFromHommelEtAl2007}}{
		Example graph from Hommel et al. See references (Hommel et al. 2007).
	}
	\item{\code{graphForParallelGatekeeping}}{
		Example graph for parallel gatekeeping. See references (Dmitrienko et al. 2003).
	}
	\item{\code{graphForImprovedParallelGatekeeping}}{
		Example graph for improved parallel gatekeeping. See references (Hommel et al. 2007).
	}
	\item{\code{graphFromHungEtWang2010}}{
	    Example graph from Hung et Wang. See references (Hung et Wang 2010).
	}
	\item{\code{graphFromMaurerEtAl1995}}{
	    Example graph from Maurer et al. See references (Hung et al. 1995).
	}
	\item{\code{cycleGraph}}{
		Cycle graph. The weight \code{weights[i]} specifies the edge weight from node i to node i+1 for i=1,...,n-1 and weight[n] from node n to node 1. 
	}
  }	
}
\value{
	A graph of class \code{\link{graphMCP}} that represents a sequentially rejective multiple test procedure.
}
\references{
Holm, S. (1979). A simple sequentally rejective multiple test procedure. Scandinavian Journal of Statistics 6, 65-70.

Dmitrienko, A., Offen, W., Westfall, P.H. (2003). Gatekeeping strategies for clinical trials that do not require all primary effects to be significant. Statistics in Medicine. 22, 2387-2400.

Bretz, F., Maurer, W., Brannath, W., Posch, M.: A graphical approach to sequentially rejective multiple test procedures.
Statistics in Medicine 2009 vol. 28 issue 4 page 586-604. \url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}

Bretz, F., Maurer, W. and Hommel, G. (2011), Test and power considerations for multiple endpoint analyses using sequentially rejective graphical procedures. Statistics in Medicine, 30: n/a.

Hommel, G., Bretz, F. und Maurer, W. (2007). Powerful short-cuts for multiple testing procedures with special reference to gatekeeping strategies. Statistics in Medicine, 26(22), 4063-4073.

Hung H.M.J., Wang S.-J. (2010). Challenges to multiple testing in clinical trials. Biometrical Journal 52, 747-756.

W. Maurer, L. Hothorn, W. Lehmacher: Multiple comparisons in drug clinical trials and preclinical assays: a-priori ordered hypotheses. In Biometrie in der chemisch-pharmazeutischen Industrie, Vollmar J (ed.). Fischer Verlag: Stuttgart, 1995; 3-18.
}
\author{
Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\examples{

g <- BonferroniHolmGraph(5)

\dontrun{
# If Rgraphviz is installed, we can take a look at the graph:
library(Rgraphviz)
renderGraph(layoutGraph(g))
}

gMCP(g, pvalues=c(0.1, 0.2, 0.4, 0.4, 0.7))

}
\keyword{ misc }
\keyword{ graphs }

