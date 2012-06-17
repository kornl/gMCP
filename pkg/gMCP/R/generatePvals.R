generatePvals <- function(g,w,cr,p,adjusted=TRUE,hint=generateWeights(g,w),exhaust=FALSE, alternatives="less"){
  res <- t(apply(hint,1,pvals.dunnett,p=p,cr=cr,exhaust=exhaust, alternatives=alternatives))
  if(adjusted){
    return(ad.p(res))
  } else {
    return(res)
  } 
}

## At the moment hypotheses that are not tested at all get and adj. p-value of 1
ad.p <- function(P){
  p.ad <- rep(NA,ncol(P))
  for(i in 1:ncol(P)){
    out <- apply(P[!is.na(P[,i]),],1,min,na.rm=T)
    p.ad[i] <- ifelse(length(out)>0,max(out),1)
  }
  return(p.ad)
}
