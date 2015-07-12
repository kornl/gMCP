test.gMCP.ext <- function() {
  r1 <- gMCP.extended(graph=graph, pvalues=pvalues, test=bonferroni.test, verbose=TRUE)
  r2 <- gMCP(graph=graph, pvalues=pvalues, verbose=TRUE)
  all.equal(r1@adjPValues, r2@adjPValues) # , check.attributes=FALSE)
}
