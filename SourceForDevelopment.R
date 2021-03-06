library(gMCP)
library(RUnit)
#library(graph)
#library(Rgraphviz)
library(multcomp)
library(PolynomF)
library(MASS)
for (file in dir(path ="pkg/gMCP/R")) {
  source(paste("pkg/gMCP/R",file,sep="/"))
}

g <- BonferroniHolm(5)

bretzG <- BretzEtAl2011()
pvalues <- c(0.1, 0.008, 0.005, 0.15, 0.04, 0.006)
names(pvalues) <- getNodes(bretzG)
verbose <- TRUE
result <- gMCP(bretzG, pvalues)

graph <- improvedParallelGatekeeping()

