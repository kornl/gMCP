test.sampSize <- function() {
  target <- 0.8
  
  graph <- BonferroniHolm(2)  
  powerReqFunc <- function(x) { x[1] || x[2] }
  result <- sampSize(graph, esf=c(1,1), effSize=c(1,1), corr.sim=diag(2), powerReqFunc=powerReqFunc, target=target, alpha=0.05)
  
  result2 <- calcPower(graph=graph, alpha=0.05, mean = rep(sqrt(result[[1]]$samp.size), 2), f=powerReqFunc)
  checkTrue(result2[[5]]>target)
}
