getStr <- function(x) {
	frt <- rep(1:100, times=100)/rep(1:100, each=100)
	w <- which(TRUE==all.equal(rep(x,10000), frt))
	if (length(w)==0) return(as.character(x))
	return(paste(w[1]%%100,w[1]%/%100,sep="/"))
}