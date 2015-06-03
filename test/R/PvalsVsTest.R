library(gMCP)
library(mvtnorm)
library(ggplot2)

w<-c(0.5,0.3)
rho<-0.1
cr<-matrix(c(1,rho,rho,1),nrow=2)
p<-c(0.02,0.03)
g<-diag(2)
g<-matrix(1,2,2)

upscale=TRUE
r1 <- r2 <- 0
for (i in 1:1000) {  
  test <- generateTest(g, w, cr, 0.05, upscale=upscale)
  
  z <- rmvnorm(n = 1000, mean=c(0,0), sigma=cr)
  # plot(sort(pnorm(z[,1])))
  pvals <- pnorm(z, lower.tail = FALSE)  
  
  result1 <- apply(z, 1, test)
  result2 <- apply(pvals, 1, function(p){adjP<-generatePvals(g,w,cr,p,upscale=upscale); ifelse(adjP<0.05,TRUE,FALSE)})
  
  r1 <- c(r1, tail(r1, 1) + sum(result1))
  r2 <- c(r2, tail(r2, 1) + sum(result2))  
  
  cat(tail(r1, 1), "vs.", tail(r2, 1), "\n")  
  
  #dat <- data.frame(y=c(r1,r2), type=rep(c("r1","r2"), each=length(r1)), x=rep(1:length(r1), 2))
  #plot(ggplot(dat, aes(x=x, y=y, color=type)) + geom_point(shape=1))
  dat <- data.frame(y=c(r1-r2), x=1:length(r1))
  plot(ggplot(dat, aes(x=x, y=y)) + geom_point(shape=1))
}

#  10004 vs. 10044 ie. 0.4% more rejections
# 10026 vs. 10067 



