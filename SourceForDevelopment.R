library(gMCP)
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

g <- BonferroniHolmGraph(5)

bretzG <- graphFromBretzEtAl2011()
pvalues <- c(0.1, 0.008, 0.005, 0.15, 0.04, 0.006)
names(pvalues) <- nodes(bretzG)
verbose <- TRUE
result <- gMCP(bretzG, pvalues)

graph <- graphForImprovedParallelGatekeeping()

