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
                                sigma = diag(5), 
                                nSim = 10000, 
                                type = "quasirandom")
cat(result)
