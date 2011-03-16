generateWeights <-
function(g,w){
  ## comute all intersection hypotheses and corresponding weights for a given graph
  n <- length(w)
  intersect <- (permutations(2,n,rep=TRUE)-1)[-1,]
  g <- apply(intersect,1,function(i) list(int=i,
                                          w=mtp.weights(i,g,w),
                                          g=mtp.edges(i,g,w)
                                     ))
  m <- as.matrix(as.data.frame(lapply(g,function(i) c(i$int,i$w))))
  colnames(m) <- NULL
  t(m)
}

