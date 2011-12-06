doInterim <- function(graph,z1,v,alpha=.025){
  g <- graph2matrix(graph)
  w <- getWeights(graph)
  ws <- generateWeights(g,w)
  n <- length(w)
  As <- t(apply(ws[,(1+n):(2*n)],1,partialCE,z1=z1,v=v,alpha=alpha))
  Bs <- rowSums(As)
  res <- new('gPADInterim',Aj=As,BJ=Bs,z1=z1,v=v,preplanned=graph,alpha=alpha)
  return(res)
}
             
secondStageTest <- function(interim,select,matchCE=TRUE,zWeights="reject",G2=interim@preplanned){
  n <- nhyp(interim@preplanned)
  w2s <- t(sapply(1:(2^n-1),function(J) adaptWeights(to.binom(J,n),select,G2,zWeights)))
  Cs <- w2s*interim@BJ
  if(matchCE){
    Cs <- t(apply(cbind(interim@BJ,w2s),1,function(Bw){
      matchCE(Bw[-1],Bw[1],interim@z1,interim@v,interim@alpha)
    }))
  }
  return(function(z) {
    decideTest(z,Cs)
  })
}
                      
nhyp <- function(graph){
  return(nrow(graph2matrix(graph)))
}


 


           

            
validPartialCEs <- function(object) {
  ## if(all(rowSums(object@Aj)==BJ)){
  ##   return(TRUE)
  ## } else {
  ##   stop("Invalid interim results PCEs do not match corresponding sums")
  ## }
  return(TRUE)
}
                        

partialCE <- function(w,z1,v,alpha){
  ## conditional error for an elementary hypothesis with weight at level alpha and first stage z-score and proportion v for the first stage
  ## also works for vectors
  ## returns the A(i,J) or if called with a vector the vector A(J,J)
  1-pnorm((qnorm(1-w*alpha)-(sqrt(v)*z1))/sqrt(1-v))
}

matchCE <- function(w2,B,z1,v,alpha,enhanced=T){
  ## find a suitable alpha level that matches the sum of PCEs for the selected hypotheses and adapted weights to that of the pre-planned procedure
  if(all(w2==0)){
    return(w2)
  }
  ## enhanced for B>1 we can reject the intersection at interim
  if(B>1){
    return(rep(1,length(w2)))
  }
  d <- function(alpha,w2,z1,v,B){
    sum(partialCE(w2,z1,v,alpha))-B
  }
  ## catch zero's
  r <- uniroot(d,c(0,1),w2=w2,z1=z1,v=v,B=B)$root
  partialCE(w2,z1,v,r)
}

adaptWeights <- function(J,select,G2,dw='reject'){
  ## adapt the weights this is basically a wrapper to mtp.weights that handles dropped hypotheses
  w <- getWeights(G2)
  g <- graph2matrix(G2)
  ## in case we only include selected hypotheses
  if(all((J-select)>0)){
    return(mtp.weights(J,g,w))
  }
  ## canonical rule number 1
  if(dw=='reject'){
    return(mtp.weights(J * select,g,w))
  }
  ## canonical rule number 2 with fallback to 1 in case all weights are zero
  if(dw=='accept'){
    if(all(w <- mtp.weights(J,g,w)*select)==0){
      return(mtp.weights(J * select,g,w))
    } else {
      return(w)
    }
  }
  ## strict rule may produce all zero weights
  if(dw=='strict'){
    return(mtp.weights(J,g,w)*select)
  }
  ## 
  stop('Invalid rule to determine second stage weights')
}

decideTest <- function(z,bounds){
  p <- 1-pnorm(z)
  dm <- t(sapply(1:nrow(bounds),function(n) {
    ## check whether z is larger than boundary
    m <- ncol(bounds)
    b <- bounds[n,]
    J <- to.binom(n,m)
    d <- rep(NA,length(b))
    d[J] <- (b[J]>=p[J])
    return(d)
  }))
  d <- apply(dm,2,function(h) {
    ## closed testing
    all(apply(dm[!is.na(h),],1,any,na.rm=T))
  })
  d
}

to.binom <- function(int,n=floor(log2(int))+1){
  ## 6 times faster than the old function (Thankyou!)
  if(n+2<=floor(log2(int))){
    stop('Vector length to small to hold binary number')
  }
  ((int)%/% 2^((n:1)-1))%%2
}



parse.intersection <- function(binom){
  paste("H(",paste(which(binom),collapse=','),")",sep="")
}

to.intersection <- function(int){
  maxn <- floor(log2(max(int)))+1
  if(length(int)>1){
    unlist(lapply(lapply(int,to.binom,n=maxn),parse.intersection))
  } else {
    parse.intersection(to.binom(int,n=maxn))
  }
}
         
## test.to.binom <- function(v){
##   sum(2^(which(v)-1))
## }

## all((1:1000-sapply(lapply(1:1000,to.binom),test.to.binom))==0)
