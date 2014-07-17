#' EXPERIMENTAL: Evaluate conditional errors at interim for a pre-planned
#' graphical procedure
#' 
#' Computes partial conditional errors (PCE) for a pre-planned graphical
#' procedure given information fractions and first stage z-scores. If a
#' function giving group sequential boundaries is specified for each
#' elementary hypotheses PCEs are computed for the corresponding group
#' sequential trial - Implementation of adaptive procedures is still in
#' an early stage and may change in the near future
#' 
#' At the moment partial conditional error rates are only 
#' computed assuming that the trial is already at
#' at the last preplanned interim analysis before the final
#' analysis. E.g. if a three stage group sequential trial is
#' planned we assume that the adaptive interim analysis is
#' performed after the second stage. Early stopping because 
#' an interim test statistic crosses an early rejection
#' boundary at some previous stage can be implemented by
#' setting the corresponding z statistics to Inf.
#'
#' Group sequential boundaries have to be specified using a function
#' of the form \code{function(w,v,alpha)} - where w is a vector of
#' weights with length equal to the number of elementary hypotheses,
#' \code{v} the timing of the interim analysis, and \code{alpha} the
#' overall alpha level - which returns a matrix where each column
#' corresponds to an elementary hypotheses and the first row gives the
#' interim rejection boundary; the second row the final rejection
#' boundary. See \code{link{agMCPldbounds}}.
#' 
#' For details see the given references.
#' 
#' @param graph A graph of class \code{\link{graphMCP}}.
#' @param z1 A numeric vector giving first stage z-scores.
#' @param v A numeric vector giving the proportions of pre-planned measurements
#' collected up to the interim analysis. Will be recycled of length different
#' than the number of elementary hypotheses.
#' @param alpha A numeric specifying the maximal allowed type one error rate.
#' @param gSB group sequential boundaries function 
#' @return An object of class \code{gPADInterim}, more specifically a list with
#' elements
#' @returnItem Aj a matrix of PCEs for all elementary hypotheses in each
#' intersection hypothesis
#' @returnItem BJ a numeric vector giving sum of PCEs per intersection
#' hypothesis
#' @returnItem preplanned Pre planned test represented by an object of class
#' \code{\link{graphMCP}}
#' @author Florian Klinglmueller \email{float@@lefant.net}
#' @seealso \code{\link{graphMCP}}, \code{\link{secondStageTest}}
#' @references Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch: A
#' graphical approach to sequentially rejective multiple test procedures.
#' Statistics in Medicine 2009 vol. 28 issue 4 page 586-604.
#' \url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}
#' 
#' Frank Bretz, Martin Posch, Ekkehard Glimm, Florian Klinglmueller, Willi
#' Maurer, Kornelius Rohmeyer (2011): Graphical approaches for multiple
#' comparison procedures using weighted Bonferroni, Simes or parametric tests.
#' Biometrical Journal 53 (6), pages 894-913, Wiley.
#' \url{http://onlinelibrary.wiley.com/doi/10.1002/bimj.201000239/full}
#' 
#' Posch M, Futschik A (2008): A Uniform Improvement of Bonferroni-Type Tests
#' by Sequential Tests JASA 103/481, 299-308
#' 
#' Posch M, Maurer W, Bretz F (2010): Type I error rate control in adaptive
#' designs for confirmatory clinical trials with treatment selection at interim
#' Pharm Stat 10/2, 96-104
#' @keywords htest graphs
#' @examples
#' 
#' 
#' ## Simple successive graph (Maurer et al. 2011)
#' ## two treatments two hierarchically ordered endpoints
#' a <- .025
#' G <- simpleSuccessiveI()
#' ## some z-scores:
#' 
#' p1=c(.1,.12,.21,.16)
#' z1 <- qnorm(1-p1)
#' p2=c(.04,1,.14,1)
#' z2 <- qnorm(1-p2)
#' v <- c(1/2,1/3,1/2,1/3)
#' 
#' intA <- doInterim(G,z1,v)
#' 
#' ## select only the first treatment 
#' fTest <- secondStageTest(intA,c(1,0,1,0))
#' 
#' 
#' 
#' @export doInterim
#' 
doInterim <- function(graph,z1,v,alpha=.025,gSB=NULL){
  g <- graph2matrix(graph)
  w <- getWeights(graph)
  ws <- generateWeights(g,w)
  m <- length(w)
  #########################################################
  ## non run-time optimized version of group sequentialism
  ##
  ## At the moment partial conditional error rates are only 
  ## computed assuming that the trial is already at
  ## at the last preplanned interim analysis before the final
  ## analysis. E.g. if a three stage group sequential trial is
  ## planned we assume that the adaptive interim analysis is
  ## performed after the second stage. Early stopping because 
  ## an interim ttest statistic crossed a early rejection
  ## boundary at some previous stage can be implemented by
  ## setting the corresponding z statistics to Inf.
  #########################################################
  ## second stage weights
  ssw <- ws[,(m+1):(2*m)]

  ## rejection boundaries
  if(!is.null(gSB)){
      boundaries <- simplify2array(lapply(1:nrow(ssw),function(i) gSB(ssw[i,],alpha=alpha,v=v)))
      ## early rejection boundaries
      sb <- t(boundaries[1,,])
      ## nominal second stage local levels divided by alpha
      ssw <- pnorm(t(boundaries[2,,]),lower.tail=FALSE)/alpha
      As <- t(apply(ssw,1,gMCP:::partialCE,z1=z1,v=v,alpha=alpha))
      ## any z1 that crosses an early rejection boundary?
      cerb <- sweep(sb,2,z1,"<=")
      ## set As to 1 where z1 crosses an early rejection boundary
      As[cerb] <- 1
  } else {
      As <- t(apply(ssw,1,gMCP:::partialCE,z1=z1,v=v,alpha=alpha))
      sb <- matrix(NA,0,0) 
  }
  Bs <- rowSums(As)
  rejected <- rep(FALSE,m)
  for(i in 1:m){
      index <- contains(i,m)
      rejected[i] <- all(Bs[index]>=1)
  }
  res <- new('gPADInterim',Aj=As,BJ=Bs,z1=z1,v=v,preplanned=graph,alpha=alpha,rejected=rejected,erb=sb)
  return(res)
}

#' EXPERIMENTAL: Construct a valid level alpha test for the second stage of an
#' adaptive design that is based on a pre-planned graphical MCP
#' 
#' Based on a pre-planned graphical multiple comparison procedure, construct a
#' valid multiple level alpha test that conserves the family wise error in the
#' strong sense regardless of any trial adaptations during an unblinded interim
#' analysis. - Implementation of adaptive procedures is still in an early stage
#' and may change in the near future
#' 
#' For details see the given references.
#' 
#' @param interim An object of class \code{\link{gPADInterim}}.
#' @param select A logical vector giving specifying which hypotheses are
#' carried forward to the second stage
#' @param matchCE Logical specifying whether second stage weights should be
#' computed proportional to corresponding PCEs
#' @param zWeights Either "reject","accept", or "strict" giving the rule what
#' should be done in cases where none of the selected hypotheses has positive
#' second stage weight.
#' @param G2 An object of class \code{\link{graphMCP}} laying down the rule to
#' compute second stage weights. Defaults to pre-planned graph.
#' @return A function of signature \code{function(z2)} with arguments:
#' 
#' that returns objects of class \code{\link{gMCPResult}}.
#' @returnItem z2 A numeric vector with second stage z-scores. Z-scores of
#' dropped hypotheses should be set no \code{NA},
#' @author Florian Klinglmueller \email{float@@lefant.net}
#' @seealso \code{\link{graphMCP}}, \code{\link{doInterim}}
#' @references Frank Bretz, Willi Maurer, Werner Brannath, Martin Posch: A
#' graphical approach to sequentially rejective multiple test procedures.
#' Statistics in Medicine 2009 vol. 28 issue 4 page 586-604.
#' \url{http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf}
#' 
#' Bretz F., Posch M., Glimm E., Klinglmueller F., Maurer W., Rohmeyer K.
#' (2011): Graphical approaches for multiple endpoint problems using weighted
#' Bonferroni, Simes or parametric tests - to appear.
#' 
#' Posch M, Futschik A (2008): A Uniform Improvement of Bonferroni-Type Tests
#' by Sequential Tests JASA 103/481, 299-308
#' 
#' Posch M, Maurer W, Bretz F (2010): Type I error rate control in adaptive
#' designs for confirmatory clinical trials with treatment selection at interim
#' Pharm Stat 10/2, 96-104
#' @keywords htest graphs
#' @examples
#' 
#' 
#' ## Simple successive graph (Maurer et al. 2011)
#' ## two treatments two hierarchically ordered endpoints
#' a <- .025
#' G <- simpleSuccessiveI()
#' ## some z-scores:
#' 
#' p1=c(.1,.12,.21,.16)
#' z1 <- qnorm(1-p1)
#' p2=c(.04,1,.14,1)
#' z2 <- qnorm(1-p2)
#' v <- c(1/2,1/3,1/2,1/3)
#' 
#' intA <- doInterim(G,z1,v)
#' 
#' ## select only the first treatment 
#' fTest <- secondStageTest(intA,c(1,0,1,0))
#' 
#' 
#' 
#' @export secondStageTest
#' 
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


#' Generate a group sequential boundaries function using ldbounds.
#'
#' Provides a simple way to generate group sequential boundary
#' functions based on the Lan De'Mets spending function approach
#' implemented in ldbounds. See \code{bounds} in package
#' \code{ldbounds} for details.
#'
#' @param iuse vector specifying which spending function to use for each elementary hypotheses. 
#' @param opts list of length iuse where each element provides a list of additional options for \code{bounds} from package \code{ldbounds}
#'
#' @examples
#' ## O'Brien Fleming Boundaries for hypotheses one and two, Pocock for the rest
#' obfpoc <- agMCPldbounds(c(1,1,2,2))
#' obfpoc(c(1/3,1/3,1/6,1/6),1/2,.025)
#'
#' @export agMCPldbounds
agMCPldbounds <- function(iuse,opts = NULL){
    if(!require(ldbounds))
        stop("In order to use agMCPldbounds to construct group sequential boundary functions you need to install package: ldbounds")
    if(!is.null(opts)){
        ## error handling
        if(length(opts) != length(iuse)){
            stop("List of options needs to be the same length as number of spending functions")
        }
        function(w,v,alpha){
            sapply(1:length(iuse),function(i) {
                if(w[i]>0) {
                    do.call(bounds,c(list(iuse=iuse[i],alpha=w[i]*alpha,t=c(v,1)),opts[[i]]))$upper.bounds
                    } else {
                        c(Inf,Inf)
                    }
        })
        }
    } else {
        function(w,v,alpha){
           sapply(1:length(iuse),function(i) {
               if(w[i]>0) {
                   do.call(bounds,list(iuse=iuse[i],alpha=w[i]*alpha,t=c(v,1)))$upper.bounds
                   } else {
                       c(Inf,Inf)
                   }
       })
       }
    }
}

nhyp <- function(graph){
  return(nrow(graph2matrix(graph)))
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
    d[which(J==1)] <- (b[which(J==1)]>=p[which(J==1)])
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
  paste("H(",paste(which(binom==1),collapse=','),")",sep="")
}

to.intersection <- function(int){
  maxn <- floor(log2(max(int)))+1
  if(length(int)>1){
    unlist(lapply(lapply(int,to.binom,n=maxn),parse.intersection))
  } else {
    parse.intersection(to.binom(int,n=maxn))
  }
}

contains <- function(i,m){
    i <- m-i+1
    ## computes binary numbers with a 1 at the i'th place
    n <- 2^m-1
    if(i == 1){
        return((1:ceiling(n/2)*2)-1)
    }
    first <- 2^(i-1)
    breakpoints <- (1:ceiling(n/first))*first
    breaks <- ceiling(length(breakpoints)/2)
    unlist(lapply(1:breaks,function(i) breakpoints[i*2-1]:(breakpoints[i*2]-1)))
}

## test.to.binom <- function(v){
##   sum(2^(which(v)-1))
## }

## all((1:1000-sapply(lapply(1:1000,to.binom),test.to.binom))==0)
