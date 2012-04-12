weights <- c(1/2, 1/2, 0, 0)
G <- simpleSuccessiveII()@m
corMat <- rbind(c(1, 0.5, 0.5, 0.5/2),
		c(0.5,1,0.5/2,0.5),
		c(0.5,0.5/2,1,0.5),
		c(0.5/2,0.5,0.5,1))
theta <- c(3, 0, 0, 0)
result2 <- calcPower(weights, alpha=0.025, G, theta, corMat, nSim = 10000)
result2
result1 <- calcPower(weights, alpha=0.025, G, theta, corMat, cr=corMat, nSim = 1000)
result1
