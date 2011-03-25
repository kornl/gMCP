generateTest <-
function(g,w,c,al){
  bounds <- generateBounds(g,w,c,al)
  return(
         function(z){
           dm <- t(apply(bounds,1,function(b) {
             ## check whether z is larger than boundary
             d <- rep(NA,length(b))
             d[!is.na(b)] <- (b[!is.na(b)]<=z[!is.na(b)])
             return(d)
           }))
           d <- apply(dm,2,function(h) {
             ## closed testing
             #min(rowSums(dm[!is.na(h),],na.rm=TRUE)>0)>0
             all(apply(dm[!is.na(h),],1,any,na.rm=T))
           })
           d
         })
}

