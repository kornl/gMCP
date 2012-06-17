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

m <- rbind(H1=c(0,           0,           0.5,           0.5          ),
		H2=c(0,           0,           0.5,           0.5          ),
		H3=c("\\epsilon", 0,           0,             "1-\\epsilon"),
		H4=c(0,           "\\epsilon", "1-\\epsilon", 0            ))

graph <- matrix2graph(m)
#graph <- graphForImprovedParallelGatekeeping()
graph
substituteEps(graph, eps=0.001)

gMCP(graph, pvalues=c(0.02, 0.04, 0.01, 0.02), eps=0.001)

graph <-  as(new("graphAM", adjMat=matrix(c(0,1,0,0,0,1,0,0,0), nrow=3), edgemode="directed"), "graphNEL")
acc(graph, c("n1", "n2", "n3"))


Cm <- matrix(NA,nr=4,nc=4)
diag(Cm) <- 1
Cm1 <- Cm
Cm[1,2] <- 1/2
Cm[2,1] <- 1/2
Cm[3,4] <- 1/2
Cm[4,3] <- 1/2
Cm2 <- Cm
Cm1
Cm2
Gm <- matrix(0,nr=4,nc=4)
Gm[1,3] <- 1
Gm[2,4] <- 1
Gm[3,2] <- 1
Gm[4,1] <- 1
Gm
w <- c(1/2,1/2,0,0)