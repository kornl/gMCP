\name{graphTest}
\alias{graphTest}
\title{ Multiple testing using graphs }
\description{
  Implements the graphical test procedure described in Bretz et
  al. (2009). Note that the gMCP function in the gMCP package performs
  the same task.
}
\usage{
  graphTest(pvalues, weights = NULL, alpha = 0.05, G = NULL, cr = NULL, graph = NULL, verbose = FALSE, subgraphWeights = 1)
}
\arguments{
  \item{pvalues}{ Either a vector or a matrix containing the local p-values
    for the hypotheses in the rows. }
  \item{weights}{ Initial weight levels for the test procedure, in
    case of multiple graphs this needs to be a matrix. }
  \item{alpha}{ Overall alpha level of the procedure. }
  \item{G}{ Matrix determining the graph underlying the test
    procedure. Note that the diagonal need to contain only 0s, while the
    rows need to sum to 1. When multiple graphs should be used this
    needs to be a list containing the different graphs as elements. }
  \item{subgraphWeights}{ Numeric vector of length equal to the number of graphs 
    specified in argument \code{G}. Specifies how the alpha should be 
    distributed between the subgraphs.
  }
  \item{graph}{ As an alternative to the specification via \code{alphas}
    and \code{G} one can also hand over a \code{graphMCP} object to the
    code. \code{graphMCP} objects can be created for example with the
    \code{graphGUI} function.
    }
  \item{cr}{ Correlation matrix that should be used for the parametric test.
    If \code{cr==NULL} the Bonferroni based test procedure is used. }
  \item{verbose}{ If verbose is TRUE, additional information about the graphical
    rejection procedure is displayed.  }
}
\value{
  A vector or a matrix containing the test results for the hypotheses under
  consideration. Significant tests are denoted by a 1, non-significant
  results by a 0.
}
\references{

  Bretz, F., Maurer, W., Brannath, W. and Posch, M. (2009) A graphical
  approach to sequentially rejective multiple test
  procedures. Statistics in Medicine, 28, 586--604

  Bretz, F., Maurer, W. and Hommel, G. (2010) Test and power
  considerations for multiple endpoint analyses using sequentially
  rejective graphical procedures, to appear in Statistics in Medicine

}
\author{ Bjoern Bornkamp }
\examples{

#### example from Bretz et al. (2010)
weights <- c(1/3, 1/3, 1/3, 0, 0, 0)
graph <- rbind(c(0,       0.5, 0,     0.5, 0,      0),
               c(1/3,     0,   1/3,	  0,   1/3,    0),
               c(0,       0.5, 0,     0,   0,      0.5),
               c(0,       1,   0,     0,   0,      0),
               c(0.5,     0,   0.5,   0,   0,      0),
               c(0,       1,   0,     0,   0,      0))
pvals <- c(0.1, 0.008, 0.005, 0.15, 0.04, 0.006)
graphTest(pvals, weights, alpha=0.025, graph)

## observe graphical procedure in detail
graphTest(pvals, weights, alpha=0.025, graph, verbose = TRUE)

## now use many p-values (useful for power simulations)
pvals <- matrix(rbeta(6e4, 1, 30), ncol = 6)
out <- graphTest(pvals, weights, alpha=0.025, graph)
head(out)

## example using multiple graphs (instead of 1)
G1 <- rbind(c(0,0.5,0.5,0,0), c(0,0,1,0,0),
            c(0, 0, 0, 1-0.01, 0.01), c(0, 1, 0, 0, 0),
            c(0, 0, 0, 0, 0))
G2 <- rbind(c(0,0,1,0,0), c(0.5,0,0.5,0,0),
            c(0, 0, 0, 0.01, 1-0.01), c(0, 0, 0, 0, 0),
            c(1, 0, 0, 0, 0))
weights <- rbind(c(1, 0, 0, 0, 0), c(0, 1, 0, 0, 0))
pvals <- c(0.012, 0.025, 0.005, 0.0015, 0.0045)
out <- graphTest(pvals, weights, alpha=0.025, G=list(G1, G2), subgraphWeights=c(1/2,1/2), verbose = TRUE)

## now again with many p-values
pvals <- matrix(rbeta(5e4, 1, 30), ncol = 5)
out <- graphTest(pvals, weights, alpha=0.025, G=list(G1, G2), subgraphWeights=c(1/2,1/2))
head(out)
}
\keyword{ htest }

