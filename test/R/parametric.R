
library(gMCP)
set.seed(1234)
pvalues <- c(0.05685171, 0.31114970, 0.30463737, 0.31168972)
m <- rbind(H1=c(0, 0.45, 0.25, 0.3),
           H2=c(0.07, 0, 0.53, 0.4),
           H3=c(0.07, 0.13, 0, 0.8),
           H4=c(0.13, 0.41, 0.46, 0))
weights <- c(0.2, 0.3, 0.4, 0.1)
graph <- new("graphMCP", m=m, weights=weights)
correlation <- matrix(NA,nrow=4,ncol=4)
correlation[1:3,1:3] <- matrix(0.4,nrow=3,ncol=3)
diag(correlation) <- 1
#alpha <- 0.0245
alpha <- 0.24
gMCP(graph, pvalues, test="parametric", correlation=correlation, alpha=alpha, upscale=FALSE)@adjPValues

gMCP.extended(graph, pvalues, test=parametric.test, correlation=correlation, alpha=alpha, upscale=FALSE, verbose=TRUE)@adjPValues

z <-  qnorm(pvalues, lower.tail = FALSE)
generateTest(g=graph@m, w=graph@weights, cr=correlation, al=alpha)(z)



if (FALSE) {
  bounds <- generateBounds(g=graph@m, w=graph@weights, cr=correlation, al=0.0245, upscale = FALSE)
  bounds
  
  dm <- t(apply(bounds, 1, function(b) {
    d <- rep(NA, length(b))
    d[!is.na(b)] <- (b[!is.na(b)] <= z[!is.na(b)])
    return(d)
  }))
}
