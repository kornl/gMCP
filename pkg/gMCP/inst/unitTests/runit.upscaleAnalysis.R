test.upscaleF <- function() {
  m <- rbind(H1=c(0, 0.333333333333333, 0.333333333333333),
             H2=c(0.333333333333333, 0, 0.333333333333333),
             H3=c(0.333333333333333, 0.333333333333333, 0))
  weights <- c(0.25, 0.25, 0.25)
  graph <- new("graphMCP", m=m, weights=weights)
  gMCP:::upscale(graph)
  # Check equal to BonferroniHolm(3)
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
