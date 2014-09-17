#' Calculate power values
#' 
#' Calculates local power values, expected number of rejections, the power to
#' reject at least one hypothesis and the power to reject all hypotheses.
#' 
#' 
#' @param x A matrix containing the rejected hypothesis, as produces by the
#' graphTest function.
#' @param f List of user defined power functions. If one is interested in the
#' power to reject hypotheses 1 and 3 one could specify \code{function(x) {x[1]
#' && x[3]}}
#' @return A list containg at least the following four elements and
#' an element for each element in the parameter \code{f}.
#' \describe{
#' \item{\code{LocPower}}{A numeric giving the local powers for the hypotheses}
#' \item{\code{ExpNrRej}}{The expected number of rejections}
#' \item{\code{PowAtlst1}}{The power to reject at least one hypothesis}
#' \item{\code{RejectAll}}{The power to reject all hypotheses}
#' }
#' @keywords htest
#' @export extractPower
extractPower <- function(x, f=list()) {
  pow <- colMeans(x)
  avgPow <- sum(x)/nrow(x)
  atleast1 <- mean(rowSums(x)>0)
  allPow <- mean(rowSums(x)==dim(x)[2])
  result <- list(LocalPower = pow, ExpRejections = avgPow,
		  PowAtlst1 = atleast1, RejectAll = allPow)
  if (is.function(f)) {f <- list(f)}
  if (length(f)>0) {
    n <- names(f)
    if (is.null(n) || all(is.na(n))) n <- paste("func", 1:length(f), sep="")
    n[n=="" | is.na(n)] <- paste("func", 1:sum(n==""), sep="")
    n <- make.names(n, unique=TRUE)
    names(f) <- n
  }
  for (fn in names(f)) {
  	result[[fn]] <- sum(apply(x,1, f[[fn]]))/dim(x)[1]
  }
  result
}

#' Calculate power values
#' 
#' Given the distribution under the alternative (assumed to be multivariate
#' normal), this function calculates the power to reject at least one
#' hypothesis, the local power for the hypotheses as well as the expected
#' number of rejections.
#' 
#' 
#' @param weights Initial weight levels for the test procedure, see graphTest
#' function.
#' @param alpha Overall alpha level of the procedure, see graphTest function.
#' (For entangled graphs \code{alpha} should be a numeric vector of length 
#' equal to the number of graphs, each element specifying the partial alpha 
#' for the respective graph.
#' The overall alpha level equals \code{sum(alpha)}.)
#' @param G Matrix determining the graph underlying the test procedure. Note
#' that the diagonal need to contain only 0s, while the rows need to sum to 1.
#' When multiple graphs should be used this needs to be a list containing the
#' different graphs as elements.
#' @param mean Mean under the alternative
#' @param corr.sim Covariance matrix under the alternative.
#' @param corr.test Correlation matrix that should be used for the parametric test.
#' If \code{corr.test==NULL} the Bonferroni based test procedure is used. Can contain
#' NAs.
#' @param type What type of random numbers to use. \code{quasirandom} uses a
#' randomized Lattice rule, and should be more efficient than
#' \code{pseudorandom} that uses ordinary (pseudo) random numbers.
#' @param nSim Monte Carlo sample size. If type = "quasirandom" this number is
#' rounded up to the next power of 2, e.g. 1000 is rounded up to
#' \eqn{1024=2^10}{1024=2^10} and at least 1024.
#' @param f List of user defined power functions (or just a single power
#' function).  If one is interested in the power to reject hypotheses 1 and 3
#' one could specify: \cr\code{f=function(x) {x[1] && x[3]}}.\cr If the power
#' of rejecting hypotheses 1 and 2 is also of interest one would use a
#' (optionally named) list: \cr 
#' \code{f=list(power1and3=function(x) {x[1] && x[3]},}\cr
#' \code{power1and2=function(x) {x[1] && x[2]})}.
#' If the list has no names, the functions will be referenced 
#' to as "func1", "func2", etc. in the output.
#' @param test In the parametric case there is more than one way to handle
#' subgraphs with less than the full alpha. If the parameter \code{test} is
#' missing, the tests are performed as described by Bretz et al. (2011), i.e.
#' tests of intersection null hypotheses always exhaust the full alpha level
#' even if the sum of weights is strictly smaller than one. If
#' \code{test="simple-parametric"} the tests are performed as defined in
#' Equation (3) of Bretz et al. (2011).
#' @param ... For backwards compatibility. For example up to version 0.8-7
#' the parameters \code{corr.model} and \code{corr.test} were called \code{sigma}
#' and \code{cr}.
#' @return A list containg three elements
#' \describe{
#' \item{\code{LocalPower}}{A numeric giving the local powers for the hypotheses}
#' \item{\code{ExpRejections}}{The expected number of rejections}
#' \item{\code{PowAtlst1}}{The power to reject at least one hypothesis}
#' }
#' @references
#' 
#' Bretz, F., Maurer, W., Brannath, W. and Posch, M. (2009) A graphical
#' approach to sequentially rejective multiple test procedures. Statistics in
#' Medicine, 28, 586--604
#' 
#' Bretz, F., Maurer, W. and Hommel, G. (2010) Test and power considerations
#' for multiple endpoint analyses using sequentially rejective graphical
#' procedures, to appear in Statistics in Medicine
#' @keywords htest
#' @examples
#' 
#' ## reproduce example from Stat Med paper (Bretz et al. 2010, Table I)
#' ## first only consider line 2 of Table I
#' ## significance levels
#' weights <- c(1/2, 1/2, 0, 0)
#' ## graph
#' G <- rbind(c(0, 0.5, 0.5, 0),
#'            c(0.5, 0, 0, 0.5),
#'            c(0, 1, 0, 0),
#'            c(1, 0, 0, 0))
#' ## or equivalent:
#' G <- simpleSuccessiveII()@@m
#' ## alternative (mvn distribution)
#' corMat <- rbind(c(1, 0.5, 0.5, 0.5/2),
#'                 c(0.5,1,0.5/2,0.5),
#'                 c(0.5,0.5/2,1,0.5),
#'                 c(0.5/2,0.5,0.5,1))
#' theta <- c(3, 0, 0, 0)
#' calcPower(weights, alpha=0.025, G, theta, corMat, nSim = 100000)
#' 
#' 
#' ## now reproduce all 14 simulation scenarios
#' ## different graphs
#' weights1 <- c(rep(1/2, 12), 1, 1)
#' weights2 <- c(rep(1/2, 12), 0, 0)
#' eps <- 0.01
#' gam1 <- c(rep(0.5, 10), 1-eps, 0, 0, 0)
#' gam2 <- gam1
#' ## different multivariate normal alternatives
#' rho <- c(rep(0.5, 8), 0, 0.99, rep(0.5,4))
#' th1 <- c(0, 3, 3, 3, 2, 1, rep(3, 7), 0)
#' th2 <- c(rep(0, 6), 3, 3, 3, 3, 0, 0, 0, 3)
#' th3 <- c(0, 0, 3, 3, 3, 3, 0, 2, 2, 2, 3, 3, 3, 3)
#' th4 <- c(0,0,0,3,3,3,0,2,2,2,0,0,0,0)
#' 
#' ## function that calculates power values for one scenario
#' simfunc <- function(nSim, a1, a2, g1, g2, rh, t1, t2, t3, t4, Gr){
#'   al <- c(a1, a2, 0, 0)
#'   G <- rbind(c(0, g1, 1-g1, 0), c(g2, 0, 0, 1-g2), c(0, 1, 0, 0), c(1, 0, 0, 0))
#'   corMat <- rbind(c(1, 0.5, rh, rh/2), c(0.5,1,rh/2,rh), c(rh,rh/2,1,0.5), c(rh/2,rh,0.5,1))
#'   mean <- c(t1, t2, t3, t4)
#'   calcPower(al, alpha=0.025, G, mean, corMat, nSim = nSim)
#' }
#' 
#' ## calculate power for all 14 scenarios
#' outList <- list()
#' for(i in 1:14){
#'   outList[[i]] <- simfunc(10000, weights1[i], weights2[i], 
#'                     gam1[i], gam2[i], rho[i], th1[i], th2[i], th3[i], th4[i])
#' }
#' 
#' ## summarize data as in Stat Med paper Table I 
#' atlst1 <- as.numeric(lapply(outList, function(x) x$PowAtlst1))
#' locpow <- do.call("rbind", lapply(outList, function(x) x$LocalPower))
#' 
#' round(cbind(atlst1, locpow), 5)
#' 
#' @export calcPower
calcPower <- function(weights, alpha, G, mean = rep(0, nrow(corr.sim)),
                      corr.sim = diag(length(mean)), corr.test = NULL,
                      nSim = 10000, type = c("quasirandom", "pseudorandom"),
					  f=list(), test, ...) {
  if (!is.null(list(...)[["sigma"]]) && missing(corr.sim)) {
     corr.sim <- list(...)[["sigma"]]
  }
  if (!is.null(list(...)[["cr"]]) && missing(corr.test)) {
    corr.test <- list(...)[["cr"]]
  }
  
	type <- match.arg(type)
	if (any(is.na(corr.sim))) stop("While parameter 'corr.test' can contain NAs, this does not make sense for 'corr.sim'.")
	#print(G)
	if (is.list(mean)) {
	  result <- list()
	  for (m in mean) {
		  sims <- rqmvnorm(nSim, mean = m, sigma = corr.sim, type = type)
		  pvals <- pnorm(sims, lower.tail = FALSE)
		  out <- graphTest(pvalues=pvals, weights=weights, alpha=alpha, G=G, cr=corr.test, test=test)
		  out <- extractPower(out, f)
		  label <- attr(m, "label")		  
		  if (!is.null(label)) {
			  attr(out, "label") <- label 
		  }
		  result[[length(result)+1]] <- out 
	  }
	  return(result)
  } else {
    #print(mean)
    #print(corr.sim)
    #print(nSim)
	  sims <- rqmvnorm(nSim, mean = mean, sigma = corr.sim, type = type)
	  pvals <- pnorm(sims, lower.tail = FALSE)
	  out <- graphTest(pvalues=pvals, weights=weights, alpha=alpha, G=G, cr=corr.test, test=test)
	  extractPower(out, f)
  }
}

calcMultiPower <- function(weights, alpha, G, ncpL, muL, sigmaL, nL,
		corr.sim = diag(length(muL[[1]])), corr.test = NULL,
		nSim = 10000, type = c("quasirandom", "pseudorandom"),
		f=list(), digits=4, variables=NULL, test, ...) {
  if (!is.null(list(...)[["sigma"]]) && missing(corr.sim)) {
    corr.sim <- list(...)[["sigma"]]
  }
  if (!is.null(list(...)[["cr"]]) && missing(corr.test)) {
    corr.test <- list(...)[["cr"]]
  }
  if (!missing(ncpL) && (!missing(muL)||!missing(sigmaL)||!missing(nL))) {
    warning("Only parameter 'ncpL' will be used, not 'muL', 'sigmaL' or 'nL'.")
  }
  if (missing(ncpL)) {
    ncpL <- list()
    for (mu in muL) {
      for (s in sigmaL) {
        for (n in nL) {
          newSetting <- mu*sqrt(n)/s
          attr(newSetting, "label") <- paste("mu: ",paste(mu,collapse=","),", sigma: ",paste(s,collapse=","),", n: ",paste(n,collapse=","),sep="")
          ncpL[[length(ncpL)+1]] <- newSetting 
        }
      }
    }
  } else {
    for (i in 1:length(ncpL)) {
      attr(ncpL[[i]], "label") <- names(ncpL)[i]
    }
  }
	sResult <- ""
	g <- matrix2graph(G)
	g <- setWeights(g, weights)
	if (is.null(variables)) {
		sResult <- paste(sResult, "Graph:",paste(capture.output(print(g)), collapse="\n"), sep="\n")
		resultL <- calcPower(weights, alpha, G, mean = ncpL, corr.sim, corr.test, nSim, type, f, test=test)
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
			#print(GII)
			#print(weights)
			#print(alpha)
			#print(ncpL)
			additionalLabel <- paste(",", paste(paste(names(variables),"=",variablesII,sep=""), collapse=", "))
			resultL <- calcPower(weights=weights, alpha=alpha, G=GII, mean = ncpL, corr.sim, corr.test, nSim, type, f, test=test)
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
