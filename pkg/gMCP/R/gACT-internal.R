w.dunnet <- function(w,cr,al=.05,exhaust, alternatives){
  if(length(cr)>1){
    conn <- conn.comp(cr)
  } else {
    conn <- 1
  }
  lconn <- sapply(conn,length)
  conn <- lapply(conn,as.numeric)
  
  error <- function(cb,w=w) {
    e <- sum(sapply(conn,function(edx){
      if(length(edx)>1){
        #return((1-pmvnorm(lower=-Inf,upper=qnorm(1-(w[edx]*cb*al)),corr=cr[edx,edx],abseps=10^-5)))
		return((1-pmvnorm(lower=qnorm((w[edx]*cb*al)/2),upper=qnorm(1-(w[edx]*cb*al)/2),corr=cr[edx,edx],abseps=10^-5)))
      } else {
        return((w[edx]*cb*al))
      }
    }))-ifelse(exhaust,al,(sum(w)*al))
    e <- ifelse(isTRUE(all.equal(e,0)),0,e)
    return(e)
  }
  up <- 1/max(w)
  cb <- uniroot(error,c(.9,up),w=w)$root
  return(qnorm(1-(w*cb*al)))
}

p.dunnet <- function(p,cr,w,exhaust, alternatives){
  if(length(cr)>1){
    conn <- conn.comp(cr)
  } else {
    conn <- 1
  }
  lconn <- sapply(conn,length)
  conn <- lapply(conn,as.numeric)
  e <- sapply(1:length(p),function(i){
    sum(sapply(conn,function(edx){
      if(length(edx)>1){
		  if(!exhaust){
          # return((1-pmvnorm(lower=-Inf,upper=qnorm(1-pmin(1,(w[edx]*p[i]/(w[i]*sum(w))))),corr=cr[edx,edx],abseps=10^-5)))
		  return((1-pmvnorm(lower=qnorm(pmin(1,(w[edx]*p[i]/(w[i]*sum(w))))/2),upper=qnorm(1-pmin(1,(w[edx]*p[i]/(w[i]*sum(w))))/2),corr=cr[edx,edx],abseps=10^-5)))
        } else {
		  # return((1-pmvnorm(lower=-Inf,upper=qnorm(1-pmin(1,(w[edx]*p[i]/(w[i])))),corr=cr[edx,edx],abseps=10^-5)))
          return((1-pmvnorm(lower=qnorm(pmin(1,(w[edx]*p[i]/(w[i])))/2),upper=qnorm(1-pmin(1,(w[edx]*p[i]/(w[i])))/2),corr=cr[edx,edx],abseps=10^-5)))
        }
      } else {
        if(!exhaust){
          return((w[edx]*p[i]/(w[i]*sum(w))))
        } else {
          return((w[edx]*p[i]/(w[i])))
        }
      }
    }))})

  e <- pmin(e,1)
  e
}

pvals.dunnett <- function(h,cr,p,exhaust, alternatives) {
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
  zb[e] <- p.dunnet(p[e],cr[e,e],w[e],exhaust, alternatives=alternatives)
  zb[which(I>0 & !hw)] <- 1
  return(zb)
}

b.dunnett <- function(h,cr,a,exhaust, alternatives) {
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
  zb[e] <- w.dunnet(w[e],cr[e,e],al=a,exhaust, alternatives=alternatives)
  zb[which(I>0 & !hw)] <- Inf
  return(zb)
}


# w = vector of weights
# h = binary vector (0,1)
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
    gt <- ((gu+gj)/(1-matrix(rep(diag(gj),nrow(gj)),nrow=nrow(gj))))
    gt[j,] <- 0
    gt[,j] <- 0
    diag(gt) <- 0
    gt[is.nan(gt)] <- 0
    return(gt)
  }
}

## as.graph <- function(m,...){
##   as(m,'graphNEL',...)
## }

myRowSums <- function(x,...){
  if(is.null(dim(x))){
    a <- sum(x,...)
  if(dim(x)==2){
    a <- rowSums(x,...)
  }
  return(a)
  }
}



conn.comp <- function(m){
  N <- 1:ncol(m)
  M <- numeric(0)
  out <- list()
  while(length(N)>0){
    Q <- setdiff(N,M)[1]
    while(length(Q)>0){
      w <- Q[1]
      M <- c(M,w)
      Q <- setdiff(unique(c(Q,which(!is.na(m[w,])))),M)
    }
    out <- c(out,list(M))
    N <- setdiff(N,M)
    M <- numeric(0)
  }
  return(out)
}

## entwurf einer fehlermeldung
## if(any(is.na(m[M,M])))
##   stop("Incomplete correlation matrix detected. Please make sure that all pairwise correlations are specified")

################################ Test stuff

# some graph with epsila

## g <- matrix(0,nr=3,nc=3)
## g[1,2] <- 1
## g[2,1] <- 1
## g[2,3] <- 1i

## w <- c(1/2,1/2,0)
