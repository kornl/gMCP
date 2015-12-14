test.upscaleF <- function() {
  # Bonferroni n+1 -> n
  for (n in 3:5) {
    graph <- subgraph(BonferroniHolm(n), paste("H", 1:n,sep=""))
    # Could also be extracted by function subgraph
    resultG <- gMCP:::upscale(graph)
    # TODO Automatically check equality to BonferroniHolm(n-1)
    print(resultG)
  }
  
  radomM <- function(n) {
    m <- matrix(runif(n*n), n, n)
    diag(m) <- 0
    m <- m / rowSums(m)*runif(n)
    return(m)
  }
  
  set.seed(1234)
  w <- runif(3)
  w <- w/sum(w)*0.8
  gr <- new("graphMCP", m=radomM(3), weights=w)
  resultG <- gMCP:::upscale(gr)
  checkEquals(rowSums(resultG@m), rep(1, 3), checkNames=FALSE)
  
  # Check not strongly connected graphs:
  m <- as.matrix(bdiag(radomM(3), radomM(4)))
  w <- runif(7)
  w <- w/sum(w)*runif(7)
  gr <- new("graphMCP", m=m, weights=w)
  resultG <- gMCP:::upscale(gr)
  
}

test.upscale <- function() {
  g <- BonferroniHolm(5)
  r1 <- gMCP(g, pvalues=c(0.01, 0.02, 0.04, 0.04, 0.7))
  # Simple Bonferroni with empty graph:
  g2 <- matrix2graph(matrix(0, nrow=5, ncol=5))
  r2 <- gMCP(g2, pvalues=c(0.01, 0.02, 0.04, 0.04, 0.7))
  # With 'upscale=TRUE' equal to BonferroniHolm:
  r3 <- gMCP(g2, pvalues=c(0.01, 0.02, 0.04, 0.04, 0.7), upscale=TRUE)
  checkEquals(r1@rejected, r3@rejected)
  checkTrue(all(r1@rejected>=r2@rejected)) # FALSE<TRUE
}
