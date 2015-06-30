graph <- BonferroniHolm(4)
pvalues <- c(0.01, 0.05, 0.03, 0.02)
alpha <- 0.05

# Test functions can be written in two ways:
# 1) If pvalues, weights and alpha are given, a logical value is returned whether the null hypothesis can be rejected.
# 2) If only pvalues and weights are given the minimal value for alpha is returned for which the null hypothesis can be rejected.

# Weighted Bonferroni-test
bonferroni.test <- function(pvalues, weights, alpha) {
  if (missing(alpha)) {
    min(pvalues/weights)
  } else {
    return(any(pvalues<=alpha*weights))
  }
}

# Simes test
simes.test <- function(pvalues, weights, alpha) {
  mJ <- Inf  				
  for (j in J) {
    Jj <- subset!=0 & (pvalues2 <= pvalues2[j]) # & (1:n)!=j
    if (adjPValues) {
      mJt <- pvalues2[j]/sum(weights[i, Jj])	
      if (is.na(mJt)) { # this happens only if pvalues2[j] is 0
        mJt <- 0
      }
      #cat("pvalues2:\n", pvalues2, "\nmJt: ", mJt, "\nmJ: ", mJ, "\nj: ", j, "\nJ: ", J, "\nsubset: ", subset, "\n")
      if (mJt<mJ) {
        mJ <- mJt
      }
    }
    #cat("j: ",j, ", Jj: ",Jj,"\n")
    #cat("p_",j,"=",pvalues2[j],"<=a*(w_",paste(which(Jj),collapse ="+w_"),")=",alpha,"*(",paste(weights[i, Jj],collapse ="+"),")=",sum(weights[i, Jj]),"\n")
    if (pvalues2[j]<=alpha*sum(weights[i, Jj])) {
      result[i, n+1] <- 1
      if (verbose) {
        explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: p_",j,"=", pvalues2[j],"<=a*(w_",paste(which(Jj),collapse ="+w_"),")\n     =",alpha,"*(",paste(weights[i, Jj],collapse ="+"),")=",alpha*sum(weights[i, Jj]),sep="")
      }
    }
  }	
  result[i, n+2] <- mJ
}

# attach(loadNamespace("gMCP"), name="namespace:gMCP", pos=3)

gMCP.extended(graph, pvalues, test)

gMCP.extended <- function(graph, pvalues, test, correlation, alpha=0.05, 
                 approxEps=TRUE, eps=10^(-3), ..., upscale=ifelse(missing(test)&&!missing(correlation)||!missing(test)&&test=="Bretz2011", TRUE, FALSE),
                 useC=FALSE, verbose=FALSE, keepWeights=TRUE, adjPValues=TRUE) {
  # Check whether sequential rejective testing is applicable.
  if(FALSE) {
    graph2 <- subgraph(graph, !getRejected(graph))
    pvalues2 <- pvalues[!getRejected(graph)]
  } else {
    graph2 <- graph
    pvalues2 <- pvalues
  }  
  
  allSubsets <- permutations(length(getNodes(graph2)))[-1,]
  result <- cbind(allSubsets, 0, Inf)
  n <- length(graph@weights)
  # Allow for different generateWeights? generateWeights can handle entangled graphs.
  weights <- generateWeights(graph2@m, getWeights(graph2))[,(n+(1:n))]
  
  if (verbose) explanation <- rep("not rejected", dim(allSubsets)[1])
  for (i in 1:dim(allSubsets)[1]) {
    subset <- allSubsets[i,]
    if(!all(subset==0)) {
      J <- which(subset!=0)	
      test.result <- test(pvalues[J], weights[1, J], alpha)
      if (test.result) {
        if (verbose) explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: ",explanation[i], sep="") 
      } else {
        rejected <- "rejected"
        if (verbose) {
          expl <- attr(test.result, "explanation")
          if(!is.null(expl)) explanation <- expl
        } 
        if (adjPValues) {
          stat <- attr(test.result, "stat")
        }          
        if (verbose) explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: ", rejected, sep="")
      }
    }
  }
  adjPValuesV <- rep(NA, n)
  for (i in 1:n) {
    if (all(result[result[,i]==1,n+1]==1)) {
      graph <- rejectNode(graph, getNodes(graph2)[i])
    }
    adjPValuesV[i] <- max(result[result[,i]==1,n+2])
  }  
  # Creating result object:
  result <- new("gMCPResult", graphs=list(graph, alpha=alpha, pvalues=pvalues, rejected=getRejected(graph), adjPValues=adjPValuesV))
  # Adding explanation for rejections:
  if (verbose) {
    output <- paste(output, paste(explanation, collapse="\n"), sep="\n")
    if (!callFromGUI) cat(output,"\n")
    attr(result, "output") <- output
  }
  # Adding attribute call:
  attr(result, "call") <- call2char(match.call())
}