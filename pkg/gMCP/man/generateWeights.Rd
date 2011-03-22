\name{generateWeights}
\alias{generateWeights}
\title{
  generateWeights
}
\description{
compute Weights for each intersection Hypotheses in the closure of a
graph based multiple testing procedure
}
\usage{
generateWeights(g, w)
}
%- maybe also 'usage' for other objects documented here.
\arguments{
  \item{g}{
    graph defined as a matrix, each element defines how much of the
    local alpha reserved for the hypothesis corresponding to its row
    index is passed on to the hypothesis corresponding to its column
    index 
}
  \item{w}{
    vector of weights, defines how much of the overall alpha is
    initially reserved for each elementary hypthosis
}
}
%\details{
%%  ~~ If necessary, more details than the description above ~~
%}
\value{
  Returns matrix with each row corresponding to one intersection
  hypothesis in the closure of the multiple testing problem. The first
  half of elements indicate whether an elementary hypotheses is in the
  intersection (1) or not (0). The second half of each row gives the
  weights allocated to each elementary hypotheses in the intersection.
}
\references{
  Bretz F, Maurer W, Brannath W, Posch M; (2008) - A graphical approach to
sequentially rejective multiple testing procedures. -  Stat Med - 28/4, 586-604
Bretz F, Posch M, Glimm E, Klinglmueller F, Maurer W, Rohmeyer K; (2011) - Graphical approaches for multiple endpoint problems using weighted Bonferroni, Simes or parametric tests - to appear
}
\author{
Florian Klinglmueller
}
\examples{
 g <- matrix(c(0,0,1,0,
               0,0,0,1,
               0,1,0,0,
               1,0,0,0), nrow = 4,byrow=TRUE)
 ## Choose weights
 w <- c(.5,.5,0,0)
 ## Weights of conventional gMCP test:
 generateWeights(g,w)
}
% Add one or more standard keywords, see file 'KEYWORDS' in the
% R documentation directory.
\keyword{ htest }

