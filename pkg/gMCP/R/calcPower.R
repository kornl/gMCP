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
                      nSim = 10000, seed = NULL, type = c("quasirandom", "pseudorandom"),
					  f=list(), test = "Bretz2011") {
	type <- match.arg(type)
	if (any(is.na(sigma))) stop("While parameter 'cr' can contain NAs, this does not make sense for 'sigma'.")
	#print(G)
	if (is.list(mean)) {
	  result <- list()
	  for (m in mean) {
		  sims <- rqmvnorm(nSim, mean = m, sigma = sigma, seed = seed, type = type)
		  pvals <- pnorm(sims, lower.tail = FALSE)
		  out <- graphTest(pvals, weights, alpha, G, cr, test)
		  out <- extractPower(out, f)
		  label <- attr(m, "label")		  
		  if (!is.null(label)) {
			  attr(out, "label") <- label 
		  }
		  result[[length(result)+1]] <- out 
	  }
	  return(result)
  } else {
	  sims <- rqmvnorm(nSim, mean = mean, sigma = sigma, seed = seed, type = type)
	  pvals <- pnorm(sims, lower.tail = FALSE)
	  out <- graphTest(pvals, weights, alpha, G, cr)
	  extractPower(out, f)
  }
}

calcMultiPower <- function(weights, alpha, G, muL, sigmaL, nL,
		sigma = diag(length(muL[[1]])), cr = NULL,
		nSim = 10000, seed = 4711, type = c("quasirandom", "pseudorandom"),
		f=list(), digits=4, variables=NULL) {
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
	if (is.null(variables)) {
		sResult <- paste(sResult, "Graph:",paste(capture.output(print(g)), collapse="\n"), sep="\n")
		resultL <- calcPower(weights, alpha, G, mean = meanL, sigma, cr, nSim, seed, type, f)
		sResult <- paste(sResult, resultL2Text(resultL, digits), sep="\n")
	} else {
		# For testing purposes: variables <- list(a=c(1,2), b=(3), x=c(2,3,4), d=c(1,2))
		i <- rep(1, length(variables))
		j <- 1
		running <- TRUE
		while (running) {
			variablesII <- rep(0, length(variables))
			for(k in 1:length(variables)) {
				variablesII[k] <- variables[[k]][i[k]]
			}
			names(variablesII) <- names(variables)
			GII <- replaceVariables(G, as.list(variablesII))
			print(GII)
			print(weights)
			print(alpha)
			print(meanL)
			additionalLabel <- paste(",", paste(paste(names(variables),"=",variablesII,sep=""), collapse=", "))
			resultL <- calcPower(weights=weights, alpha=alpha, G=GII, mean = meanL, sigma, cr, nSim, seed, type, f)
			sResult <- paste(sResult, resultL2Text(resultL, digits, additionalLabel=additionalLabel), sep="\n")
			# Going through all of the variable settings:
			i[j] <- i[j] + 1
			while (i[j]>length(variables[[j]]) && running) {
				if (j<length(i)) {
					j <- j + 1
				} else {
					running <- FALSE
				}
				i[j] <- i[j] + 1
				for (k in 1:(j-1)) {
					i[k] <- 1
				}
			}
		}		
	}
	
	return(sResult)
}

resultL2Text <- function(resultL, digits, additionalLabel="") {
	sResult <- ""
	for(result in resultL) {
		label <- attr(result, "label")
		title <- paste("Setting: ",label, additionalLabel, sep="")		
		sResult <- paste(sResult, title, paste(rep("=", nchar(title)),collapse=""), sep="\n")			
		sResult <- paste(sResult, "Local Power:",paste(capture.output(print(round(result$LocalPower, digits))), collapse="\n"), sep="\n")
		sResult <- paste(sResult, "\nExpected number of rejections:", round(result$ExpRejections, digits), sep="\n")
		sResult <- paste(sResult, "Prob. to reject at least one hyp.:", round(result$PowAtlst1, digits), sep="\n")
		sResult <- paste(sResult, "Prob. to reject all hypotheses:", round(result$RejectAll, digits), sep="\n")
		if (length(result)>4) {
			for (i in 5:length(result)) {
				#TODO pF <- attr(result, "label")
				pF <- attr(result[i], "label")
				if (is.null(pF)) pF <- names(result)[i]
				sResult <- paste(sResult, paste(pF, ":", sep=""), result[i], sep="\n")
			}
		}
		sResult <- paste(sResult, "\n", sep="\n")		
	}
	return(sResult)
}

#x <- calcMultiPower(weights=BonferroniHolm(3)@weights, alpha=0.05, G=BonferroniHolm(3)@m, muL=list(c(0,0,0),c(10,10,10),c(10,20,30)), sigmaL=list(c(1,1,1)), nL=list(c(10,10,10),c(20,20,20)), f=list(p1=function(x){x[1]&&x[2]}))
#cat(x)
