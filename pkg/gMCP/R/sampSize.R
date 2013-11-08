## This function is taken from package DoseFinding under GPL 
## from Bjoern Bornkamp, Jose Pinheiro and Frank Bretz

## function for sample size calculation and functions to evaluate
## performance metrics for different sample sizes

sampSize <- function (upperN, lowerN = floor(upperN/2),
                      targFunc, target, tol = 0.001, alRatio,
                      Ntype = c("arm", "total"), verbose = FALSE){
  ## target function to iterate
  func <- function(n){
    targFunc(n) - target
  }

  Ntype <- match.arg(Ntype)
  if (!missing(alRatio)) {
    if (any(alRatio <= 0)) {
      stop("all entries of alRatio need to be positive")
    } else {
      alRatio <- alRatio/sum(alRatio)
    }
    if(Ntype == "arm") {
      alRatio <- alRatio/min(alRatio)
    } 
  } else { ## by default assume
    stop("allocation ratios need to be specified")
  }
  
  ## first call
  upper <- func(round(upperN*alRatio))
  if(length(upper) > 1)
    stop("targFunc(n) to evaluate to a vector of length 1.")
  if(!is.numeric(upper))
    stop("targFunc(n) needs to evaluate to a numeric.")

  ## bracket solution
  if (upper < 0)
    message("upper limit for sample size is raised")

  while (upper < 0) {
    upperN <- 2 * upperN
    upper <- func(round(upperN*alRatio))
  }
  
  lower <- func(round(lowerN*alRatio))
  
  if (lower > 0) 
    message("lower limit for sample size is decreased")

  while (lower > 0) {
    lowerN <- round(lowerN/2)
    if (lowerN == 0) 
      stop("cannot find lower limit on n")
    lower <- func(round(lowerN*alRatio))
  }

  ## now start bisection
  if (verbose) {
    cat("Upper N:", upperN, "Upper value", round(upper+target, 4), "\n")
    cat("Lower N:", lowerN, "Lower value", round(lower+target, 4), "\n\n")
  }
  
  current <- tol+1
  niter <- 0
  ## bisect sample size until tolerance is achieved
  while (abs(current) > tol & (upperN > lowerN + 1)) {
    currN <- round((upperN + lowerN)/2)
    current <- func(round(currN * alRatio))
    if (current > 0) {
      upperN <- currN
    } else {
      lowerN <- currN
    }
    niter <- niter + 1
    if (verbose) {
      cat("Iter: ", niter, ", N = ", currN, ", current value = ",
          round(current+target, 4), "\n", sep = "")
    }
  }
  ## increase sample size so that the obtained value is larger than the target
  while (current < 0) {
    currN <- currN + 1
    current <- func(round(currN * alRatio))
  }

  res <- list(samp.size = round(currN * alRatio),
              target = round(current+target, 4))
  attr(res, "alRatio") <- round(alRatio/min(alRatio), 4)
  attr(res, "target") <- target
  attr(res, "Ntype") <- Ntype
  class(res) <- "sampSize"
  res
}