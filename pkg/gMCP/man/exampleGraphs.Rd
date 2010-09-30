\name{Example Graphs}
\alias{createBonferroniHolmGraph}
\alias{createGraphFromBretzEtAl}
\alias{createGraphForParallelGatekeeping}
\alias{createGraphForImprovedParallelGatekeeping}

\title{
	Functions that create different example graphs
}
\description{ 
	Functions that creates example graphs, e.g. graphs that represents a Bonferroni-Holm adjustment, parallel gatekeeping or special procedures from selected papers.
	
	We are providing functions and not the resulting graphs directly because this way you have additional examples:
	You can look at the function body with \code{\link{body}} and see how the graph is built.
}
\usage{
	createBonferroniHolmGraph(n, alpha=0.05)
	createGraphFromBretzEtAl(alpha=0.05)
	createGraphForParallelGatekeeping(alpha=0.05)
	createGraphForImprovedParallelGatekeeping(alpha=0.05)
}

\arguments{
  \item{n}{
	Number of hypotheses.
  }
  \item{alpha}{
  	Overall type I error rate alpha.
  }
}
\details{
  \describe{
	\item{\code{createBonferroniHolmGraph}}{
		Returns a graph that represents a Bonferroni-Holm adjustment.
		The result is a complete graph, where the alpha is split equally between all hypotheses and each edge weight is \eqn{\frac{1}{n-1}}{1/(n-1)}.
	}
	\item{\code{createGraphFromBretzEtAl}}{
		Example graph from Bretz et al. See references (Bretz et al. 2009).
	}
	\item{\code{createGraphForParallelGatekeeping}}{
		Example graph for parallel gatekeeping. See references (Dmitrienko et al. 2003).
	}
	\item{\code{createGraphForImprovedParallelGatekeeping}}{
		Example graph for improved parallel gatekeeping. See references (Hommel et al. 2007).
	}
  }	
}
\value{
	A graph of class \code{\link{graphMCP}} that represents a sequentially rejective multiple test procedure.
}
\references{
Bonferroni, C. E.: Il calcolo delle assicurazioni su gruppi di teste. In Studi in Onore del Professore Salvatore Ortu Carboni. Rome: Italy, pp. 13-60, 1935.

Bonferroni, C. E.: Teoria statistica delle classi e calcolo delle probabilita. Pubblicazioni del R Istituto Superiore di Scienze Economiche e Commerciali di Firenze 8, 3-62, 1936.

Dmitrienko, A., Offen, W., Westfall, P.H. (2003). Gatekeeping strategies for clinical trials that do not require all primary effects to be significant. Statistics in Medicine. 22, 2387-2400.

Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch: A graphical approach to sequentially rejective multiple test procedures.
Statistics in Medicine 2009 vol. 28 issue 4 page 586-604. \url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}

Hommel, G., Bretz, F. und Maurer, W. (2007) Powerful short-cuts for multiple testing procedures with special reference to gatekeeping strategies. Statistics in Medicine, 26(22), 4063-4073.
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

