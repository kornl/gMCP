
# Weighted Bonferroni-test
bonferroni.test <- function(pvalues, weights, alpha=0.05, adjPValues=TRUE, ...) {
  if (adjPValues) {
    return(min(pvalues/weights))
  } else {
    return(any(pvalues<=alpha*weights))
  }
}

# Weighted parametric test
parametric.test <- function(pvalues, weights, alpha=0.05, adjPValues=TRUE, correlation, ...) {
  
  # ToDo Document dropping these dimensions with zero weights
  I <- which(weights>0)
  pvalues <- pvalues[I]
  weights <- weights[I]
  correlation <- correlation[I,I]
  
  if(length(correlation)>1){
    conn <- conn.comp(correlation)
  } else {
    conn <- 1
  }

  conn <- lapply(conn,as.numeric)
  e <- sapply(1:length(pvalues),function(i) {
    sum(sapply(conn, function(edx){
      if(length(edx)>1){
        return(1-pmvnorm(lower=-Inf,
                         upper=qnorm(1-pmin(1,(weights[edx]*pvalues[i]/(weights[i])))),
                         corr=correlation[edx,edx],abseps=10^-5))
      } else {
        return((weights[edx]*pvalues[i]/(weights[i]*sum(weights))))
      }
    }))})
  
  e <- min(c(e,1))
  
  if (adjPValues) {
    return(e)
  } else {
    return(e<=alpha)
  }
}

# Weighted Bonferroni-test / trimmed Simes
bonferroni.trimmed.simes.test <- function(pvalues, weights, alpha=0.05, adjPValues=FALSE, ...) {
  if (adjPValues) stop("Alpha level is needed and adjusted p-values can not be calculated for this test.")
  if (length(pvalues)==2) {
    # Truncated Simes:
    rejected <- (pvalues[1]<=alpha*weights[1] && pvalues[2]<=1-alpha*weights[2]) ||
      (pvalues[2]<=alpha*weights[2] && pvalues[1]<=1-alpha*weights[1]) ||
      max(pvalues)<=alpha
    return(rejected)
    #TODO adjusted p-values?
  } else {
    return(bonferroni.test(pvalues, weights, alpha, adjPValues, ...))
  }
}

# Simes on subsets, otherwise Bonferroni
# As an additional argument a list of subsets must be provided, that states in which cases a Simes test is applicable (i.e. if all hypotheses to test belong to one of these subsets), e.g.
# subsets <- list(c("H1", "H2", "H3"), c("H4", "H5", "H6"))
simes.on.subsets.test <- function(pvalues, weights, alpha=0.05, adjPValues=TRUE, subset, ...) {
  subsets <- list(...)[["subsets"]]
  if (any(sapply(subsets, function(x) {all(subset %in% subsets)}))) {
    # Simes test:
    return(simes.test(pvalues, weights, alpha, adjPValues, ...))
  } else {
    # Bonferroni test:
    return(bonferroni.test(pvalues, weights, alpha, adjPValues, ...))
  }  
}

# Simes test
simes.test <- function(pvalues, weights, alpha=0.05, adjPValues=TRUE, ...) {
  mJ <- Inf  				
  for (j in 1:length(pvalues)) {
    Jj <- pvalues <= pvalues[j] # & (1:n)!=j
    if (adjPValues) {
      mJt <- pvalues[j]/sum(weights[Jj])	
      if (is.na(mJt)) { # this happens only if pvalues[j] is 0
        mJt <- 0
      }
      if (mJt<mJ) {
        mJ <- mJt
      }
    }
    # explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: p_",j,"=", pvalues[j],"<=a*(w_",paste(which(Jj),collapse ="+w_"),")\n     =",alpha,"*(",paste(weights[i, Jj],collapse ="+"),")=",alpha*sum(weights[i, Jj]),sep="")
  }
  if (adjPValues) {
    return(mJ)
  } else {
    return(mJ<=alpha)
  }
}

# resampling tests?

# attach(loadNamespace("gMCP"), name="namespace:gMCP", pos=3)

#' Test functions can be written in two ways:
#' 1) If pvalues, weights and alpha are given, a logical value is returned whether the null hypothesis can be rejected.
#' 2) If only pvalues and weights are given the minimal value for alpha is returned for which the null hypothesis can be rejected.
#' ... contains correlation
#' graph <- BonferroniHolm(4)
#' pvalues <- c(0.01, 0.05, 0.03, 0.02)
#' alpha <- 0.05
#' gMCP.extended(graph=graph, pvalues=pvalues, test=bonferroni.test, verbose=TRUE)
gMCP.extended <- function(graph, pvalues, test, alpha=0.05, eps=10^(-3), upscale=FALSE, verbose=FALSE, adjPValues=TRUE, correlation, ...) {
  callFromGUI <- !is.null(list(...)[["callFromGUI"]])
  
  # Replace epsilon
  
  provide.subset <- ("subsets" %in% names(formals(test)))
  provide.correlation <- ("correlation" %in% names(formals(test)))
  
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
  n <- length(graph2@weights)
  # Allow for different generateWeights? generateWeights can handle entangled graphs.
  weights <- generateWeights(graph2@m, getWeights(graph2))[,(n+(1:n))]
  
  if (verbose) explanation <- rep("not rejected", dim(allSubsets)[1])
  for (i in 1:dim(allSubsets)[1]) {
    subset <- allSubsets[i,]
    if(!all(subset==0)) {
      J <- which(subset!=0)	
      if (adjPValues) {
        #test.result <- do.call(test, list(pvalues2[J], weights[i, J], alpha, adjPValues, ...), quote=TRUE)
        if (provide.correlation) {
          test.result <- test(pvalues=pvalues2[J], weights=weights[i, J], alpha=alpha, adjPValues=adjPValues, correlation=correlation[J,J, drop=FALSE], ...)
        } else {
          test.result <- test(pvalues=pvalues2[J], weights=weights[i, J], alpha=alpha, adjPValues=adjPValues, ...)
        }
        result[i, n+2] <- test.result
        result[i, n+1] <- ifelse(test.result<=alpha*sum(weights[i, J]), 1, 0)
        if (verbose) {
          if (result[i, n+1]==1) {
            explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: rejected (adj-p:",result[i, n+2],")", sep="") 
          } else {
            explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: not rejected (adj-p:",result[i, n+2],")", sep="") 
          }
        }
        #if (verbose) {
        #  explanation[i] <- paste("Adjusted p-value for subset {",paste(J,collapse=","),"}: ", result[i, n+2], " [pvalues: ", dput2(pvalues2[J]) ,", weights: ", dput2(round(weights[i, J],4)), "]", sep="")  
        #}
      } else { # No adjusted p-values, just rejections:
        test.result <- test(pvalues2[J], weights[i, J], alpha, adjPValues, ...)
        result[i, n+1] <- ifelse(test.result, 1, 0)
        result[i, n+2] <- NA
        if (verbose) {
          if (test.result) {
            explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: rejected [pvalues=", dput2(pvalues2[J]) ,", weights=", dput2(round(weights[i, J],4)), "]", sep="") 
          } else {
            explanation[i] <- paste("Subset {",paste(J,collapse=","),"}: not rejected [pvalues=", dput2(pvalues2[J]) ,", weights=", dput2(round(weights[i, J],4)), "]", sep="") 
          }
        }
      }
      if (verbose) {
        expl <- attr(test.result, "explanation")
        if (!is.null(expl)) explanation[i] <- expl
      }
    }
  }
  adjPValuesV <- rep(NA, n)
  for (i in 1:n) {
    if (all(result[result[,i]==1,n+1]==1)) {
      graph2 <- rejectNode(graph2, getNodes(graph2)[i])
    }
    adjPValuesV[i] <- max(result[result[,i]==1,n+2])
  }  
  # Creating result object:
  result <- new("gMCPResult", graphs=list(graph, graph2), alpha=alpha, pvalues=pvalues, rejected=getRejected(graph2), adjPValues=adjPValuesV)
  # Adding explanation for rejections:
  output <- "Info:"
  if (verbose) {
    output <- paste(output, paste(explanation, collapse="\n"), sep="\n")
    if (!callFromGUI) cat(output,"\n")
    attr(result, "output") <- output
  }
  # Adding attribute call:
  attr(result, "call") <- call2char(match.call())
  return(result)
}