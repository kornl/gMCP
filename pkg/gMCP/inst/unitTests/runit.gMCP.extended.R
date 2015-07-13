test.gMCP.ext <- function() {
  graph <- BonferroniHolm(4)
  pvalues <- c(0.01, 0.05, 0.03, 0.02)
  alpha <- 0.05
  
  # Bonferroni test:
  r1 <- gMCP.extended(graph=graph, pvalues=pvalues, test=bonferroni.test, verbose=TRUE)
  r2 <- gMCP(graph=graph, pvalues=pvalues, verbose=TRUE)
  checkTrue(all.equal(r1@adjPValues, r2@adjPValues, check.attributes=FALSE)) # No names for gMCP.extended
  
  # Simes test:
  r1 <- gMCP.extended(graph=graph, pvalues=pvalues, test=simes.test, verbose=TRUE)
  r2 <- gMCP(graph=graph, pvalues=pvalues, test="Simes", verbose=TRUE)
  checkTrue(all.equal(r1@adjPValues, r2@adjPValues)) # TODO Both don't have names
  
  # Parametric test:
  
  # Trimmed Simes test:
  r1 <- gMCP.extended(graph=graph, pvalues=pvalues, test=bonferroni.trimmed.simes.test, verbose=TRUE, alpha=0.05, adjPValues = FALSE)
  r2 <- gMCP.extended(graph=graph, pvalues=pvalues, test=bonferroni.test, verbose=TRUE)
  checkTrue(all(as.numeric(r1@rejected) >= as.numeric(r2@rejected)))
  
  # Simes with partition:
  
  
}
