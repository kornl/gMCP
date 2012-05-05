extractPower <- function(x, f=list()) {
  pow <- colMeans(x)
  avgPow <- sum(x)/nrow(x)
  atleast1 <- mean(rowSums(x)>0)
  allPow <- mean(rowSums(x)==dim(x)[2])
  result <- list(LocalPower = pow, ExpRejections = avgPow,
		  PowAtlst1 = atleast1, RejectAll = allPow)
  if(length(unique(names(f)))!=length(f)) stop("f must be a named list with unique names.")
  for (fn in names(f)) {
  	result[[fn]] <- sum(apply(x,1, f[[fn]]))/dim(x)[1]
  }
  result
}

calcPower <- function(weights, alpha, G, mean = rep(0, nrow(sigma)),
                      sigma = diag(length(mean)), cr = NULL,
                      nSim = 10000, seed = 4711, type = c("quasirandom", "pseudorandom"),
					  f=list()) {
  if (is.list(mean)) {
	  result <- list()
	  for (m in mean) {
		  type <- match.arg(type)
		  sims <- rqmvnorm(nSim, mean = m, sigma = sigma, seed = seed, type = type)
		  pvals <- pnorm(sims, lower.tail = FALSE)
		  out <- graphTest(pvals, weights, alpha, G, cr)
		  out <- extractPower(out, f)
		  label <- attr(m, "label")		  
		  if (!is.null(label)) {
			  attr(out, "label") <- label 
		  }
		  result[[length(result)+1]] <- out 
	  }
	  return(result)
  } else {
	  type <- match.arg(type)
	  sims <- rqmvnorm(nSim, mean = mean, sigma = sigma, seed = seed, type = type)
	  pvals <- pnorm(sims, lower.tail = FALSE)
	  out <- graphTest(pvals, weights, alpha, G, cr)
	  extractPower(out, f)
  }
}

calcMultiPower <- function(weights, alpha, G, muL, sigmaL, nL,
		sigma = diag(length(muL[[1]])), cr = NULL,
		nSim = 10000, seed = 4711, type = c("quasirandom", "pseudorandom"),
		f=list()) {
	meanL <- list()
	for (mu in muL) {
		for (s in sigmaL) {
			for (n in nL) {
				newSetting <- mu*sqrt(n)/s
				attr(newSetting, "label") <- paste("mu: ",paste(mu,collapse=","),", sigma: ",paste(s,collapse=","),", n: ",paste(n,collapse=","),sep="")
				meanL[[length(meanL)+1]] <- newSetting 
			}
		}
	}
	sResult <- ""
	g <- matrix2graph(G)
	g <- setWeights(g, weights)
	sResult <- paste(sResult, "Graph:",paste(capture.output(print(g)), collapse="\n"), sep="\n")
	resultL <- calcPower(weights, alpha, G, mean = meanL, sigma, cr, nSim, seed, type, f)
	for(result in resultL) {
		label <- attr(result, "label")
		title <- paste("Setting:",label)		
		sResult <- paste(sResult, title, paste(rep("=", nchar(title)),collapse=""), sep="\n")			
		sResult <- paste(sResult, "Local Power:",paste(capture.output(print(result$LocalPower)), collapse="\n"), sep="\n")
		sResult <- paste(sResult, "\nExpected number of rejections:", result$ExpRejections, sep="\n")
		sResult <- paste(sResult, "Prob. to reject at least one hyp.:", result$PowAtlst1, sep="\n")
		sResult <- paste(sResult, "Prob. to reject all hypotheses:", result$RejectAll, sep="\n")
		if (length(result)>4) {
			for (i in 5:length(result)) {
				#TODO pF <- attr(result, "label")
				pF <- attr(result[i], "label")
				if (is.null(pF)) pF <- names(result)[i]
				sResult <- paste(sResult, paste(pF,":",sep=""), result[i], sep="\n")
			}
		}
		sResult <- paste(sResult, "\n", sep="\n")		
	}
	return(sResult)
}

#x <- calcMultiPower(weights=BonferroniHolm(3)@weights, alpha=0.05, G=BonferroniHolm(3)@m, muL=list(c(0,0,0),c(10,10,10),c(10,20,30)), sigmaL=list(c(1,1,1)), nL=list(c(10,10,10),c(20,20,20)), f=list(p1=function(x){x[1]&&x[2]}))
#cat(x)
