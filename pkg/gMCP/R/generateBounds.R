generateBounds <-
function(g,w,cr,al=.05,hint=generateWeights(g,w),exhaust=F){
  res <- t(apply(hint,1,b.dunnett,a=al,cr=cr,exhaust=exhaust))
  res
}

