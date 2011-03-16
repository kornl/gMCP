generateBounds <-
function(g,w,cr,al=.05,hint=generateWeights(g,w)){
  res <- t(apply(hint,1,b.dunnet,a=al,cr=cr))
  res
}

