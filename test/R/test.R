#library(gMCP)
.gsrmtVar <- list()
.gsrmtVar$alpha <- c(0.0,0.5,0.5)
.gsrmtVar$hnodes <- c("H1","H2","H3")
.gsrmtVar$m <- matrix(0, nrow=3, ncol=3)
rownames(.gsrmtVar$m) <- colnames(.gsrmtVar$m) <- .gsrmtVar$hnodes
.gsrmtVar$m["H2","H3"] <- "1"
.gsrmtVar$m["H3","H2"] <- "1"
.tmpGraph <- new("graphMCP", m=.gsrmtVar$m, weights=.gsrmtVar$alpha)
#trace("nodeData<-", browser, exit=browser, signature = c("graphMCP","character","character","ANY"))
nodeData(.tmpGraph, "H1", "rejected") <- TRUE