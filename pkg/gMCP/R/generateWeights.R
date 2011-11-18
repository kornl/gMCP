generateWeights <-
function(g,w){
  ## comute all intersection hypotheses and corresponding weights for a given graph
  n <- length(w)
  intersect <- (permutations(n))[-1,]
  g <- apply(intersect,1,function(i) list(int=i,
                                          w=mtp.weights(i,g,w),
                                          g=mtp.edges(i,g,w)
                                     ))
  m <- as.matrix(as.data.frame(lapply(g,function(i) c(i$int,i$w))))
  colnames(m) <- NULL
  t(m)
}

permutations <- function(n) {
	outer((1:(2^n))-1, (n:1)-1, FUN=function(x,y) {(x%/%2^y)%%2})
}

weightsForAllIntersections <- function(graph) {
	return(generateWeights(graph@m, graph@weights))
}