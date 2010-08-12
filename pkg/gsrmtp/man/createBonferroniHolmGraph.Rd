\name{createBonferroniHolmGraph}
\alias{createBonferroniHolmGraph}
\alias{createGraphFromBretzEtAl}

\title{
	Functions that create different example graphs
}
\description{ 
	Functions that creates example graphs, e.g. graphs that represents a Bonferroni-Holm adjustment
}
\usage{
	createBonferroniHolmGraph(n, alpha=0.05)
	createGraphFromBretzEtAl()
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
  \itemize{
	\item{createBonferroniHolmGraph}{
		Returns a graph that represents a Bonferroni-Holm adjustment.
		The result is a complete graph, the alpha is split equally between all hypotheses and each edge weight is \eqn{\frac{1}{n-1}}{1/(n-1)}.
	}
	\item{createGraphFromBretzEtAl}{
		Example graph from Bretz et al. See references.
	}
  }	
}
\value{
	A graph of class \code{\link{graphSRMTP}} that represents a sequentially rejective multiple test procedure.
}
\references{
Bonferroni, C. E.: Il calcolo delle assicurazioni su gruppi di teste. In Studi in Onore del Professore Salvatore Ortu Carboni. Rome: Italy, pp. 13-60, 1935.
Bonferroni, C. E.: Teoria statistica delle classi e calcolo delle probabilita. Pubblicazioni del R Istituto Superiore di Scienze Economiche e Commerciali di Firenze 8, 3-62, 1936.
Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch: A graphical approach to sequentially rejective multiple test procedures.
Statistics in Medicine 2009 vol. 28 issue 4 page 586-604. \url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}
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

srmtp(g, pvalues=c(0.1, 0.2, 0.4, 0.4, 0.7))

}
\keyword{ misc }
\keyword{ graphs }

