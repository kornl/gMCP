library(gMCP)
library(mvtnorm)
library(ggplot2)
library(Matrix)

w<-c(0.5,0.3)
rho<-0.1
rho<-0.9
cr<-matrix(c(1,rho,rho,1),nrow=2)
p<-c(0.02,0.03)
g<-diag(2)
g<-matrix(1,2,2)
upscale=FALSE
r1 <- r2 <- 0

if (FALSE) {
  graph <- truncatedHolm(0.3)
  w<-graph@weights
  g<-graph@m
  rho1<-(-0.9)
  rho2<-0.9
  cr1<-matrix(c(1,rho1,rho1,1),nrow=2)
  cr2<-matrix(c(1,rho2,rho2,1),nrow=2)
  cr <- bdiagNA(cr1, cr2)
  cr <- as.matrix(bdiag(cr1, cr2))
  crT <- as.matrix(bdiag(cr1, cr2))
  p<-c(0.02,0.03,0.01,0.02)
  
}

for (i in 1:1000) {  
  test <- generateTest(g, w, cr, 0.05, upscale=upscale)
  
  z <- rmvnorm(n = 1000, mean=c(0,0,0,0), sigma=crT)
  # plot(sort(pnorm(z[,1])))
  pvals <- pnorm(z, lower.tail = FALSE)  
  
  result1 <- apply(z, 1, test)
  result2 <- apply(pvals, 1, function(p){adjP<-generatePvals(g,w,cr,p,upscale=upscale); ifelse(adjP<0.05,TRUE,FALSE)})
  
  r1 <- c(r1, tail(r1, 1) + sum(result1))
  r2 <- c(r2, tail(r2, 1) + sum(result2))  
  
  cat(tail(r1, 1), "vs.", tail(r2, 1), ", rate:",tail(r1, 1)/tail(r2, 1),"\n")  
  
  #dat <- data.frame(y=c(r1,r2), type=rep(c("r1","r2"), each=length(r1)), x=rep(1:length(r1), 2))
  #plot(ggplot(dat, aes(x=x, y=y, color=type)) + geom_point(shape=1))
  dat <- data.frame(y=c(r1-r2), x=1:length(r1))
  plot(ggplot(dat, aes(x=x, y=y)) + geom_point(shape=1))
}

###

res <- t(apply(gMCP:::generateWeights(g,w),1,gMCP:::pvals.dunnett,p=p,cr=cr,upscale=upscale))#, alternatives=alternatives))
gMCP:::ad.p(res)


###

generatePvals(g,w,cr,p,upscale=FALSE)

# calls

gMCP:::p.dunnet(p, cr, w, FALSE)
1-pmvnorm(lower=-Inf, upper=qnorm(1-pmin(1,(w*p[1]/(w[1]*sum(w))))), corr=cr,abseps=10^-5)
1-pmvnorm(lower=-Inf, upper=qnorm(1-pmin(1,(w*p[2]/(w[2]*sum(w))))), corr=cr,abseps=10^-5)

gMCP:::p.dunnet(p[1], 1, sum(w), FALSE)
#1-pmvnorm(lower=-Inf, upper=qnorm(1-pmin(1,(w*p[1]/(w[1]*sum(w))))), corr=cr,abseps=10^-5)
(p[1]/sum(w))

gMCP:::p.dunnet(p[2], 1, sum(w), FALSE)
#1-pmvnorm(lower=-Inf, upper=qnorm(1-pmin(1,(w*p[2]/(w[2]*sum(w))))), corr=cr,abseps=10^-5)
(p[2]/sum(w))


