test.gMCP.ext <- function() {
  graph <- BonferroniHolm(4)
  pvalues <- c(0.01, 0.05, 0.03, 0.02)
  alpha <- 0.05
  
  # Bonferroni test:
  r1 <- gMCP.extended(graph=graph, pvalues=pvalues, test=bonferroni.test, verbose=TRUE)
  r2 <- gMCP(graph=graph, pvalues=pvalues, verbose=TRUE)
  all.equal(r1@adjPValues, r2@adjPValues, check.attributes=FALSE) # No names for gMCP.extended
  
  # Simes test:
  r1 <- gMCP.extended(graph=graph, pvalues=pvalues, test=simes.test, verbose=TRUE)
  r2 <- gMCP(graph=graph, pvalues=pvalues, test="Simes", verbose=TRUE)
  all.equal(r1@adjPValues, r2@adjPValues) # TODO Both don't have names
  
  # Parametric test:
  
  # Trimmed Simes test:
  
  # Simes with partition:
  
  
}
