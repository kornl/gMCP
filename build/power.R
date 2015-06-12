m <- rbind(H1=c(0, 0.25, 0.25, 0.25, 0.25),
           H2=c(0, 0, 1, 0, 0),
           H3=c(0, 1, 0, 0, 0),
           H4=c(0, 0, 0, 0, 1),
           H5=c(0, 0, 0, 1, 0))

weights <- c(1, 0, 0, 0, 0)
graph <- new("graphMCP", m=m, weights=weights)

result <- gMCP:::calcMultiPower(weights=weights, 
                                alpha=0.05, 
                                G=m, 
                                muL = list(c(2, 1, 1, 1, 1)), 
                                sigmaL = list(c(1, 1, 1, 1, 1)), 
                                nL = list(c(10, 10, 10, 10, 10)), 
                                sigma = diag(5))
cat(result)

# Which calls

ncp <- c(6.324555320336759,3.1622776601683795,3.1622776601683795,3.1622776601683795,3.1622776601683795)
gMCP:::calcPower(weights=weights, G=m, mean=ncp)

sims <- gMCP:::rqmvnorm(10000, mean = ncp, sigma = diag(5))
# Looks good so far:
apply(sims, 2, mean)
apply(sims, 2, sd)
#
pvals <- pnorm(sims, lower.tail = FALSE)
out <- gMCP:::graphTest(pvalues=pvals, weights=weights, alpha=0.05, G=m, test=test)
out <- gMCP:::extractPower(out)


