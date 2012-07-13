library(gMCP)
randomGraph <- function(n,sparse=.1){
  g <- matrix(runif(n*n)*(runif(n*n)>sparse),n,n)
  diag(g) <- 0
  t(apply(g,1,function(x) {
    if(!sum(x)==0) x <- x/sum(x)
    return(x)
    }))
}

randomWeights <- function(n,sparse=.5){
  w <- runif(n)*(runif(n)>sparse)
  if(!sum(w)==0){
    w <- w/sum(w)
  }
  return(w)
}

randomCor <-
#shamelessly stolen from kornelius_rohmeyer@algorithm-forge
  function(n) {
    require("mvtnorm")
    t <- rmvnorm(n,rep(0,n),diag(n))
    for (i in 1:n) {
      t[i,] <- t[i,]/sqrt(t(t[i,])%*%t[i,])
    }
    t%*%t(t)
  }

sims <- function(G,C,w,a=.05,B=50,m=0,n=4){
  test <- function(){
    cur1 <- list()
    p <- 1-pnorm(rnorm(n,m))
    cur1$p <- p
    assign('state1',cur1,envir=.GlobalEnv)
    gMCP(G,p,corr=C,alpha=a)
  }
  replicate(B,test())
}

simsims <- function(B,Bi,n=4,a=.05,m=0){
  tests <- function(m,n,B){
    Gm <- randomGraph(n)
    w <- randomWeights(n)
    C <- randomCor(n)
    cur2 <- list()
    cur2$graphm <- Gm
    cur2$weights <- w
    cur2$cor <- C
    assign('state2',cur2,envir=.GlobalEnv)
    G <- matrix2graph(Gm,w)
    cur2$graphMCP <- G
    assign('state2',cur2,envir=.GlobalEnv)
    out <- sims(G,C,W,a=a,B=B,m=m,n=n)
    cur2$graphMCPRes <- out
    assign('state2',cur2,envir=.GlobalEnv)
    r <- sapply(out,getRejected)
    cur2$rejected <- r
    assign('state2',cur2,envir=.GlobalEnv)
    sum(colSums(r)>0)
  }
  replicate(B,tests(m=m,n=n,B=Bi))
}


n <- 4
Gm <- randomGraph(n)
w <- randomWeights(n)
C <- randomCor(n)
a <- .05#runif(1)
p <- 1-pnorm(rnorm(n))
G <- matrix2graph(Gm,w)



## Works
gMCP(G,p,corr=C,alpha=1)
gMCP(G,p,corr=C,alpha=.99)
gMCP(G,p,corr=C,alpha=.75)
gMCP(G,p,corr=C,alpha=.60)
gMCP(G,p,corr=C,alpha=.05)

## let's shoot some holes into C works as well 
C[1:2,3:4] <- NA
C[3:4,1:2] <- NA

gMCP(G,p,corr=C,alpha=1)
gMCP(G,p,corr=C,alpha=.99)
gMCP(G,p,corr=C,alpha=.75)
gMCP(G,p,corr=C,alpha=.60)
gMCP(G,p,corr=C,alpha=.05)

C[3,4] <- 0
C[4,3] <- 0

gMCP(G,p,corr=C,alpha=1)
gMCP(G,p,corr=C,alpha=.99)
gMCP(G,p,corr=C,alpha=.75)
gMCP(G,p,corr=C,alpha=.60)
gMCP(G,p,corr=C,alpha=.05)

## more troubles:
w <- c(0,1,0,0)
G <- matrix2graph(Gm,w)

gMCP(G,p,corr=C,alpha=1)
gMCP(G,p,corr=C,alpha=.99)
gMCP(G,p,corr=C,alpha=.75)
gMCP(G,p,corr=C,alpha=.60)
gMCP(G,p,corr=C,alpha=.05)



## this works now (we set a high alpha to get some rejections)
gMCP(G,p,correlation='Dunnett',alpha=.5)

## there WAS some problem with finding the right output function
replicate(10,  gMCP(G,p,corr=C))

## we will track the current input parameters throughout simulations
state1 <- list()
state2 <- list()

## may take some time
out <- simsims(1000,20,a=.5)

## round of errors 
#load('unirootbug.Rd')
#gMCP(state2$graphMCP,state1$p,corr=state2$cor,al=1)


## at the moment this throws an
out1 <- sims(G,C,B=100,a=.3)  



## Fehler von Frank
Gm <- matrix(0,nr=4,nc=4)
Gm[1,3] <- 1
Gm[2,4] <- 1
Gm[3,2] <- 1
Gm[4,1] <- 1
Gm
w <- c(1/2,1/2,0,0)
G <- matrix2graph(Gm,w)
Cm <- matrix(NA,nr=4,nc=4)
diag(Cm) <- 1
Cm[1,2] <- 1/2
Cm[2,1] <- 1/2
Cm[3,4] <- 1/2
Cm[4,3] <- 1/2
p <- c(0.0131,0.1,0.012,0.01)
B <- generateBounds(Gm,w,Cm,a=.025)
test <- generateTest(Gm,w,Cm,a=.025)

z <- qnorm(1-p)
test(z)

gMCP(G,p,corr=Cm,alpha=0.025)

