graph2weights <- function(g) {
  w <- sapply(nodeData(g),function(x) x$alpha)
  w <- w/sum(w)
}


