library(graph)
library(RUnit)
library(Rgraphviz)
library(mutoss)
for (file in dir(path ="pkg/gsrmtp/R")) {
  source(paste("pkg/gsrmtp/R",file,sep="/"))
}

g <- createBonferroniHolmGraph(5)
graph <- createBonferroniHolmGraph(3)
pvalues <- c(0,0,0)
names(pvalues) <- nodes(graph)
verbose <- TRUE