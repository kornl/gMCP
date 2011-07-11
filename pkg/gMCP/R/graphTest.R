graphTest <- function(pvalues, alphas = NULL, G = NULL, graph = NULL,
                      verbose = FALSE){

  usegraph <- !is.null(graph)
  if(usegraph & (class(graph) != "graphMCP"))
    stop("graph needs to an object of class graphMCP")
  if(usegraph & !is.null(alphas))
    stop("either graph or alphas and G need to be specified")
  if(usegraph & !is.null(G))
    stop("either graph or alphas and G need to be specified")
  if(usegraph){
    ## get alpha vector and transition matrix
    ll <- convert(graph)
    alphas <- ll$alphas
    G <- ll$G
  }
    
  nH <- ifelse(!is.matrix(pvalues), length(pvalues), ncol(pvalues))
  nH <- as.integer(nH)
  checkArgs(pvalues, alphas, G, nH)
  
  if(is.list(G)){
    nGraphs <- length(G)
    G <- c(unlist(G))
  } else {
    nGraphs <- as.integer(1)
  }
  if(!is.matrix(pvalues)){
    res <- .C("graphproc", h=double(nH), a=as.double(alphas), G=as.double(G),
              as.double(pvalues), nH, as.double(G), as.integer(nGraphs),
              as.integer(verbose))
    out <- c(H = res$h)
    attr(out, "last.alphas") <- res$a
    attr(out, "last.G") <- matrix(res$G, ncol = nH)
    return(out)
  } else {
    nCount <- as.integer(nrow(pvalues))
    res <- .C("graphmult", h=double(nH*nCount), double(nH),
              as.double(alphas), double(nGraphs*nH),
              as.double(G), as.double(G), as.double(G),
              as.double(pvalues), double(nH), nCount, nH,
              as.integer(nGraphs), as.integer(verbose))
    out <- matrix(res$h, nrow = nCount)
    if(is.null(colnames(G)))
      colnames(out) <- paste("H", 1:nH, sep="")
    else
      colnames(out) <- colnames(G)
    return(out)
  }
}

checkArgs <- function(pvalues, alphas, G, nH){
  
  alplen <- ifelse(is.matrix(alphas), ncol(alphas),
                   length(alphas))
  if(any(alphas < 0) | any(alphas > 1))
    stop("entries of alphas need to be in [0,1].")
  if(any(pvalues < 0) | any(pvalues > 1))
    stop("entries of pvalues need to be in [0,1].")
  if(is.list(G)){
    nGraphs <- length(G)
    for(i in 1:nGraphs){
      if(any(G[[i]] < 0) | any(G[[i]] > 1))
        stop("entries of G need to be in [0,1]")
      if(any(rowSums(G[[i]]) > 1))
        stop("rows of G need to sum to values <= 1")
      if(nrow(G[[i]]) != ncol(G[[i]]))
        stop("non-quadratic matrix G")
      if(nH != nrow(G[[i]]))
        stop("non-conforming pvalues and G.")
      if(alplen != nrow(G[[i]]))
        stop("non-conforming alphas and G.")
    }
    if(!is.matrix(alphas)){
      stop("alphas needs to have as many rows as there are graphs")
    } else {
      if(nrow(alphas) != nGraphs)
        stop("alphas needs to have as many rows as there are graphs")
    }

  } else {
    if(any(G < 0) | any(G > 1))
      stop("entries of G need to be in [0,1]")
    if(any(rowSums(G) > 1))
      stop("rows of G need to sum to values <= 1")
    if(nrow(G) != ncol(G))
      stop("non-quadratic matrix G") 
    if(nH != nrow(G))
      stop("non-conforming pvalues and G.")
    if(alplen != nrow(G))
      stop("non-conforming alphas and G.")
  }
}

convert <- function(g){
  ## converts a graph object as
  ## used in the gMCP library
  ## and returns vector of alphas
  ## and significance levels
  if(class(g) != "graphMCP")
    stop("g needs to an object of class graphMCP")
  Hnams <- g@nodes
  nH <- length(Hnams)
  alphas <- numeric(nH)
  G <- matrix(0, nrow = nH, ncol = nH)
  for(i in 1:nH){
    alphas[i] <- g@nodeData@data[[i]]$alpha
  }
  names(alphas) <- Hnams

  nams <- names(g@edgeData)
  for(nam in nams){
    nam2 <- strsplit(nam, "\\|")
    indx <- grep(nam2[[1]][1], Hnams)
    indy <- grep(nam2[[1]][2], Hnams)
    wgt <- g@edgeData@data[[nam]]$weight
    G[indx, indy] <- wgt
  }
  dimnames(G) <- list(Hnams, Hnams)
  list(alphas=alphas, G=G)
}
