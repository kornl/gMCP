extractPower <- function(x){
  pow <- colMeans(x)
  avgPow <- sum(x)/nrow(x)
  atleast1 <- mean(rowSums(x)>0)
  list(LocalPower = pow, ExpRejections = avgPow,
       PowAtlst1 = atleast1)
}

calcPower <- function(weights, alpha, G, mean = rep(0, nrow(sigma)),
                      sigma = diag(length(mean)),corr = NULL,
                      nSim = 10000, seed = 4711, type = c("quasirandom", "pseudorandom")){
  type <- match.arg(type)
  sims <- rqmvnorm(nSim, mean = mean, sigma = sigma, corr = corr,
                   seed = seed, type = type)
  pvals <- pnorm(sims, lower.tail = FALSE)
  out <- graphTest(pvals, weights, alpha, G)
  extractPower(out)
}
