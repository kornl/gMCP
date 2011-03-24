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



## alpha needs to be <.5 otherwise we might get into trouble (dirty fix)
gMCP(G,p,corr=C,alpha=1)
gMCP(G,p,corr=C,alpha=.99)
gMCP(G,p,corr=C,alpha=.75)
gMCP(G,p,corr=C,alpha=.60)

## zero weights are not handled well
G <- matrix2graph(Gm,rep(0,4))
gMCP(G,p,corr=C,alpha=.05)


## this doesn't work yet
gMCP(G,p,corr='Dunnet')

## there is some problem with finding the right output function
replicate(10,  gMCP(G,p,corr=C))

## we will track the current input parameters throughout simulations
state1 <- list()
state2 <- list()

# may take some time
out <- simsims(10,100,a=.5)

## at the moment this throws an
out1 <- sims(G,C,B=100,a=.3)  





