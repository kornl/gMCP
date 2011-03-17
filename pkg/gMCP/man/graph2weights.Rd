\name{graph2weights}
\alias{graph2weights}
\title{
  graph2weights
  %%  ~~function to do ... ~~
}
\description{
extract local alpha levels from \code{\link{graphMCP}} class type graphs
and transform them to a numeric vector of weights 
}
\usage{
graph2weights(g)
}
%- maybe also 'usage' for other objects documented here.
\arguments{
  \item{g}{
    object of class \code{"\link[gMCP:graphMCP-class]{graphMCP}"} defining some sequentially rejective weighted MTP
}
}
\value{
  Returns a numeric vector with weights that allocate the
  global alpha equivalent to the local alpha
  levels defined in the input object.
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
\dontrun{
library(gMCP)
g <- createBonferroniHolmGraph(3)          
gm <- graph2matrix(g)                      
w <- graph2weights(g)                      
c <- matrix(NA,3,3)                            
diag(c) <- 1
p <- runif(length(w))                      
z <- qnorm(1-p)

gACT(z,gm,w,c)
gMCP(g,p)
}
}
% Add one or more standard keywords, see file 'KEYWORDS' in the
% R documentation directory.
\keyword{ htest }

