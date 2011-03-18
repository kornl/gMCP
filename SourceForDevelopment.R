library(graph)
library(RUnit)
library(Rgraphviz)
library(mutoss)
library(gtools)
library(multcomp)
library(PolynomF)
library(MASS)
for (file in dir(path ="pkg/gMCP/R")) {
  source(paste("pkg/gMCP/R",file,sep="/"))
}

g <- createBonferroniHolmGraph(5)
graph <- createGraphFromBretzEtAl()
pvalues <- c(0.1, 0.008, 0.005, 0.15, 0.04, 0.006)
names(pvalues) <- nodes(graph)
verbose <- TRUE
result <- gMCP(graph, pvalues)