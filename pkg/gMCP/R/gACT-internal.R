w.dunnet <- function(w,cr,al=.05){
  if(length(cr)>1){
    conn <- connComp(as.graph(!is.na(cr)))
  } else {
    conn <- 1
  }
  lconn <- sapply(conn,length)
  conn <- lapply(conn,as.numeric)
  
  error <- function(cb) {
    e <- sum(sapply(conn,function(edx){
      if(length(edx)>1){
        return((1-pmvnorm(lower=-Inf,upper=qnorm(1-(w[edx]*cb*al)),corr=cr[edx,edx])))
      } else {
        return((w[edx]*cb*al))
      }
    }))-al
    e <- ifelse(isTRUE(all.equal(e,0)),0,e)
    return(e)
  }
  up <- 1/max(w)
  cb <- uniroot(error,c(.9,up))$root
  return(qnorm(1-(w*cb*al)))
}


b.dunnet <- function(h,cr,a) {
#  if(a > .5){
#    stop("alpha levels above .5 are not supported")
#  }
  n <- length(h)
  I <- h[1:(n/2)]
  w <- h[((n/2)+1):n]
  hw <- sapply(w,function(x) !isTRUE(all.equal(x,0)))
  e <- which(I>0 & hw)
  zb <- rep(NA,n/2)
  if(length(e) == 0){
    return(zb)
  }
  zb[e] <- w.dunnet(w[e],cr[e,e],al=a)
  zb[which(I>0 & !hw)] <- Inf
  return(zb)
}

mtp.weights <- function(h,g,w){
  ## recursively compute weights for a given graph and intersection hypothesis
  if(sum(h)==length(h)){
    return(w)
  } else {
    j <- which(h==0)[1]
    h[j] <- 1
    wu <- mtp.weights(h,g,w)
    gu <- mtp.edges(h,g,w)
    guj <- gu[j,]
    wt <- wu+wu[j]*guj
    wt[j] <- 0
    return(wt)
  }
}

mtp.edges <- function(h,g,w){
  ## recursively compute the edges for the graph of a given intersection hypothesis
  if(sum(h)==length(h)){
    return(g)
  } else {
    j <- which(h==0)[1]
    h[j] <- 1
    gu <- mtp.edges(h,g,w)
    gj <- gu[,j]%*%t(gu[j,])
    gt <- ((gu+gj)/(1-matrix(rep(diag(gj),nrow(gj)),nr=nrow(gj))))
    gt[j,] <- 0
    gt[,j] <- 0
    diag(gt) <- 0
    gt[is.nan(gt)] <- 0
    return(gt)
  }
}

as.graph <- function(m,...){
  as(m,'graphNEL',...)
}

myRowSums <- function(x,...){
  if(is.null(dim(x))){
    a <- sum(x,...)
  if(dim(x)==2){
    a <- rowSums(x,...)
  }
  return(a)
  }
}


################################ Test stuff

# some graph with epsila

## g <- matrix(0,nr=3,nc=3)
## g[1,2] <- 1
## g[2,1] <- 1
## g[2,3] <- 1i

## w <- c(1/2,1/2,0)
