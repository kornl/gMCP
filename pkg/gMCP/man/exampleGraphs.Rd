\name{Example Graphs}
\alias{exampleGraph}
\alias{BonferroniHolm}
\alias{BretzEtAl2011}
\alias{parallelGatekeeping}
\alias{improvedParallelGatekeeping}
\alias{HommelEtAl2007}
\alias{HungEtWang2010}
\alias{MaurerEtAl1995}
\alias{improvedFallbackI}
\alias{improvedFallbackII}
\alias{cycleGraph}
\alias{fixedSequence}
\alias{generalSuccessive}
\alias{simpleSuccessiveI}
\alias{simpleSuccessiveII}
\alias{truncatedHolm}
\alias{BretzEtAl2009Fig8}
\alias{fallback}
\alias{HuqueAloshEtBhore2011}
\title{
	Functions that create different example graphs
}
\description{ 
	Functions that creates example graphs, e.g. graphs that represents a Bonferroni-Holm adjustment, parallel gatekeeping or special procedures from selected papers.
	
	We are providing functions and not the resulting graphs directly because this way you have additional examples:
	You can look at the function body with \code{\link{body}} and see how the graph is built.
}
\usage{
	BonferroniHolm(n)
	parallelGatekeeping()
	improvedParallelGatekeeping()
	BretzEtAl2011()
	HungEtWang2010()
	HuqueAloshEtBhore2011()
	HommelEtAl2007()
	MaurerEtAl1995()
	improvedFallbackI(weights=rep(1/3, 3))
	improvedFallbackII(weights=rep(1/3, 3))
	cycleGraph(nodes, weights)
	fixedSequence(n)
	fallback(weights)
	generalSuccessive(weights = c(1/2, 1/2))
	simpleSuccessiveI()
	simpleSuccessiveII()
	truncatedHolm()
	BretzEtAl2009Fig8()
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
	\item{\code{BonferroniHolm}}{
		Returns a graph that represents a Bonferroni-Holm adjustment.
		The result is a complete graph, where all nodes have the same weights and each edge weight is \eqn{\frac{1}{n-1}}{1/(n-1)}.
	}
	\item{\code{BretzEtAl2011}}{
		Graph in figure 2 from Bretz et al. See references (Bretz et al. 2011).
	}
	\item{\code{HommelEtAl2007}}{
		Graph from Hommel et al. See references (Hommel et al. 2007).
	}
	\item{\code{parallelGatekeeping}}{
		Graph for parallel gatekeeping. See references (Dmitrienko et al. 2003).
	}
	\item{\code{improvedParallelGatekeeping}}{
		Graph for improved parallel gatekeeping. See references (Hommel et al. 2007).
	}
	\item{\code{HungEtWang2010}}{
	    Graph from Hung et Wang. See references (Hung et Wang 2010).
	}
	\item{\code{MaurerEtAl1995}}{
	    Graph from Maurer et al. See references (Maurer et al. 1995).
	}
	\item{\code{cycleGraph}}{
		Cycle graph. The weight \code{weights[i]} specifies the edge weight from node \eqn{i}{i} to node \eqn{i+1}{i+1} for \eqn{i=1,\ldots,n-1}{i=1,...,n-1} and \code{weight[n]} from node \eqn{n}{n} to node 1. 
	}
	\item{\code{improvedFallbackI}}{
	    Graph for the improved Fallback Procedure by Wiens & Dmitrienko. See references (Wiens et Dmitrienko 2005).
	}
	\item{\code{improvedFallbackII}}{
	    Graph for the improved Fallback Procedure by Hommel & Bretz. See references (Hommel et Bretz 2008).
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

Hommel, G., Bretz, F. (2008): Aesthetics and power considerations in multiple testing - a contradiction? Biometrical Journal 50:657-666.

Hung H.M.J., Wang S.-J. (2010). Challenges to multiple testing in clinical trials. Biometrical Journal 52, 747-756.

W. Maurer, L. Hothorn, W. Lehmacher: Multiple comparisons in drug clinical trials and preclinical assays: a-priori ordered hypotheses. In Biometrie in der chemisch-pharmazeutischen Industrie, Vollmar J (ed.). Fischer Verlag: Stuttgart, 1995; 3-18.

Wiens, B.L., Dmitrienko, A. (2005): The fallback procedure for evaluating a single family of hypotheses. Journal of Biopharmaceutical Statistics 15:929-942.
}
\author{
Kornelius Rohmeyer \email{rohmeyer@small-projects.de}
}

\examples{

g <- BonferroniHolm(5)

\dontrun{
# If Rgraphviz is installed, we can take a look at the graph:
library(Rgraphviz)
renderGraph(layoutGraph(g))
}

gMCP(g, pvalues=c(0.1, 0.2, 0.4, 0.4, 0.7))

}
\keyword{ misc }
\keyword{ graphs }

