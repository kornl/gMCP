\name{createBonferroniHolmGraph}
\alias{createBonferroniHolmGraph}

\title{
	Example graph that represents a Bonferroni-Holm adjustment
}
\description{
	Creates an example graph that represents a Bonferroni-Holm adjustment
}
\usage{
	createBonferroniHolmGraph(n, alpha=0.05)
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
	The result is a complete graph, the alpha is split equally between all hypotheses and each edge weight is \eqn{\frac{1}{n-1}}{1/(n-1)}.
}
\value{
	A graph of class \code{\link{graphSRMTP}} that represents a Bonferroni-Holm adjustment.
}
\references{
Bonferroni, C. E.: Il calcolo delle assicurazioni su gruppi di teste. In Studi in Onore del Professore Salvatore Ortu Carboni. Rome: Italy, pp. 13-60, 1935.
Bonferroni, C. E.: Teoria statistica delle classi e calcolo delle probabilita. Pubblicazioni del R Istituto Superiore di Scienze Economiche e Commerciali di Firenze 8, 3-62, 1936.
}
\author{
Kornelius Rohmeyer <rohmeyer@small-projects.de>
}

\examples{

createBonferroniHolmGraph(5)

}
\keyword{ misc }
\keyword{ graphs }

