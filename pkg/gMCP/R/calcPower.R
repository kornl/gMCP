extractPower <- function(x, f=list()){
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
                      sigma = diag(length(mean)), corr = NULL,
                      nSim = 10000, seed = 4711, type = c("quasirandom", "pseudorandom"), f=list()){
  type <- match.arg(type)
  sims <- rqmvnorm(nSim, mean = mean, sigma = sigma, corr = corr,
                   seed = seed, type = type)
  pvals <- pnorm(sims, lower.tail = FALSE)
  out <- graphTest(pvals, weights, alpha, G)
  extractPower(out, f)
}
