

gMCP2 <- function(graph, pvalues, test, correlation, alpha=0.05, 
                 approxEps=TRUE, eps=10^(-3), ..., useC=FALSE, 
                 verbose=FALSE, keepWeights=TRUE, adjPValues=TRUE) {
  result <- 1
  return(match.call())
  attr(result, "call") <- match.call()
  return(result)
}

g <- BonferroniHolm(3)
x <- gMCP2(g, pvalues=c(0.01, 0.02, 0.04, 0.04, 0.7))

call2char <- function(call, g) {
  
  paste(capture.output(print(call)), collapse="\n")
}