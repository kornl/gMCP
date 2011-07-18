#
# Code taken (and adapted) from package MCPAN under GPL.
# Authors: Frank Schaarschmidt, Daniel Gerhard, Martin Sill
#

plotSimCI <-
		function(estimate, lower=NULL, upper=NULL, alternative=c("two.sided","less","greater"),
				lines=NULL, lineslty=2, lineslwd=1, linescol="black",
				CIvert=FALSE, CIlty = 1, CIlwd=1, CIcex=1, CIcol="black", CIlength=NULL,
				HL=TRUE, ...)
{
	if(HL){
		old.par <- par(no.readonly=TRUE)
	}
	
	aargs <- list(...)
	
# check input variables
	
	if(length(estimate)<1 | (!is.numeric(estimate)&!is.integer(estimate)))
	{stop("Argument estimate should be a numeric vector")}
	
	k<-length(estimate)
	num <- 1:k
	
	if(is.null(names(estimate)))
	{compn <- paste("C", num, sep="")}
	else{compn <- names(estimate)}
	
	if(!is.null(lower))
	{
		if(!is.numeric(lower)&!is.integer(lower))
		{stop("Argument lower should be a numeric vector")}
		if(length(lower)!=k)
		{stop("Argument lower should be a vector of the same length as estimate!")}
	}
	
	if(!is.null(upper))
	{
		if(!is.numeric(upper)&!is.integer(upper))
		{stop("Argument upper should be a numeric vector")}
		if(length(upper)!=k)
		{stop("Argument upper should be a vector of the same length as estimate!")}
	}
	
	alternative<-match.arg(alternative)
	
	if(!is.null(lines))
	{
		if(!is.numeric(lines)&!is.integer(lines))
		{stop("Argument lines should be a numeric vector")}
	}
	
	CIlty<-rep(CIlty, length.out=k)
	CIlwd<-rep(CIlwd, length.out=k)
	CIcol<-rep(CIcol, length.out=k)
	
	mymai <- par("mai")
	
# define the plot range
	
	if(is.null(lower) & is.null(upper)){warning("No confidence limits specified: arguments lower and upper are both empty!")}
	
# define the plot range in 
# dependence of the alternative:
	
	switch(alternative,
			"two.sided"={
				allpoints <- c(lower, estimate, upper)
				if(all(!is.finite(allpoints))){stop("Arguments estimate, lower and upper contain only infinity or missing values!")}
				allexist<-allpoints[is.finite(allpoints)]
				lplot <- min(c(allexist, lines))
				uplot <- max(c(allexist, lines))
			},
			
			"less"=
					{
						
						allpoints<-c(estimate, upper)
						if(all(!is.finite(allpoints))){stop("Arguments estimate and upper contain only infinity or missing values!")}
						allexist<-allpoints[is.finite(allpoints)]
						lplot <- min(c(lines, allexist)) 
						uplot <- max(c(allexist, lines))
					},
			
			"greater"=
					{
						
						allpoints<-c(lower, estimate)
						if(all(!is.finite(allpoints))){stop("Arguments estimate and lower contain only infinity or missing values!")}
						allexist<-allpoints[is.finite(allpoints)]
						lplot <- min(c(lines, allexist)) 
						uplot <- max(c(lines, allexist))
						
					})
	
# Define the final plot ranges:
	
	llplot <- lplot - 0.1*abs(lplot-uplot)
	uuplot <- uplot + 0.1*abs(lplot-uplot)
	
# define the type of interval drawn,
# appropriate for unbounded CI
	
	switch(alternative,
			
			"two.sided"={
				
				if(is.null(lower)){lower<-rep(llplot,k); code<-rep(2,k)
					warning("No lower limits specified!")
				}
				else{
					if(is.null(upper)){upper<-rep(uuplot,k); code<-rep(1,k)
						warning("No upper limits specified!")
					}
					else{
						
						code<-rep(3,k)
						
						infl<-!is.finite(lower)
						lower[infl]<-llplot
						code[infl]<-2
						
						infu<-!is.finite(upper)
						upper[infu]<-uuplot
						code[infu]<-1
						
						infts <- infl&infu
						code[infts]<-0
					}}
			},
			
			"less"={
				code<-rep(2,k)
				
				if(is.null(upper)){upper<-rep(uuplot,k); code<-rep(0,k)
					warning("No upper limits specified although alternative='less'!")
				}
				
				infu<-!is.finite(upper)
				upper[infu]<-uuplot
				code[infu]<-0
				
			},
			
			"greater"={
				code<-rep(1,k)
				
				if(is.null(lower)){lower<-rep(llplot,k); code<-rep(0,k)
					warning("No lower limits specified although alternative='greater'!")
				}
				
				infl<-!is.finite(lower)
				lower[infl]<-llplot
				code[infl]<-0
			})
	
	
# Define the defaults for main, sub, ylab, xlab:
	
	if (is.null(aargs$main)) {aargs$main<-""} 
	if (is.null(aargs$sub)) {aargs$sub<-""}
	if (is.null(aargs$ylab)) {aargs$ylab<-""} 
	if (is.null(aargs$xlab)) {aargs$xlab<-""}
	
# Box arguments
	
	bargs<-list()
	if(is.null(aargs$bty)){BTY<-"o"}
	else{BTY<-aargs$bty}
	
# plot function for vertical CI:
	
	if(CIvert==TRUE)
	{
		
		if(HL)
		{
			plot.new()
			
			# adjust margin under the x axis according to length of comparison names
			
			xwidth<- 1.5 * max(strwidth(compn, units = "inches", cex = par("cex.axis"))) 
			
			if (mymai[1] < xwidth) 
				mymai[1] <- xwidth
			par(mai=mymai, new=TRUE)
		}
		aargs$x<-num
		aargs$y<-estimate
		aargs$axes<-FALSE
		
		if(is.null(aargs$ylim)){aargs$ylim<-c(llplot, uuplot)}
		if(is.null(aargs$type)){aargs$type<-"p"}
		if(is.null(aargs$pch)){aargs$pch<-16}
		if(is.null(aargs$cex)){aargs$cex<-CIcex}
		
		do.call("plot", aargs)
		
		axis(side = 1, at = num, labels=compn, las=2, ... )
		axis(side=2, las=2, ...)
		box(bty=BTY)
		
		abline(v=num, col="lightgrey", lty=3)
		
		if(!is.null(lines))
		{
			abline(h=lines, lty=lineslty, lwd=lineslwd, col=linescol)
		}
		
		if(is.null(CIlength))
		{
			
			arrlength<-1/(k*2)
			
#if(k<25)
# {arrlength<-0.08}
#else
# {arrlength<-0.05}
		}
		else{
			arrlength<-CIlength
		}
		
		switch(alternative,
				
				"two.sided"={
					
					for(i in 1:length(num))
					{
						arrows(x0=num[i], x1=num[i], y0=lower[i], y1=upper[i],
								length = arrlength, angle = 90, code = code[i],
								col = CIcol[i], lty = CIlty[i], lwd = CIlwd[i])
						
					}
				},
				
				"less"={
					for(i in 1:length(num))
					{
						arrows(x0=num[i], x1=num[i], y0=llplot, y1=upper[i],
								length = arrlength, angle = 90, code = code[i],
								col = CIcol[i], lty = CIlty[i], lwd = CIlwd[i])
					}
				},
				
				"greater"={
					
					for(i in 1:length(num))
					{
						arrows(x0=num[i], x1=num[i], y0=lower[i], y1=uuplot,
								length = arrlength, angle = 90, code = code[i],
								col = CIcol[i], lty = CIlty[i], lwd = CIlwd[i])
					}
				}
		)
		
		
	}
	
	
# plot function for horizontal CI:
	
	
	if(CIvert==FALSE)
	{
		if(HL)
		{
			plot.new()
			
			# adjust margin under the x axis according to length of comparison names
			ywidth<- 1.5 * max(strwidth(compn, units = "inches", cex = par("cex.axis"))) 
			
			if (mymai[2] < ywidth) 
				mymai[2] <- ywidth
			par(mai=mymai, new=TRUE)
		}
		
		rnum<-rev(num)
		
		aargs$y<-rnum
		aargs$x<-estimate
		aargs$axes<-FALSE
		
		if(is.null(aargs$xlim)){aargs$xlim<-c(llplot, uuplot)}
		if(is.null(aargs$type)){aargs$type<-"p"}
		if(is.null(aargs$pch)){aargs$pch<-16}
		if(is.null(aargs$cex)){aargs$cex<-CIcex}
		
		do.call("plot", aargs)
		
		axis(side = 2, at = rnum, labels=compn, las=2, ...)
		axis(side = 1, ...)
		box(bty=BTY)
		
		abline(h=num, col="lightgrey", lty=3)
		
		abline(v=lines, lty=lineslty, lwd=lineslwd, col=linescol)
		
		if(is.null(CIlength))
		{
			arrlength<-1/(k*2)
		}
		
		switch(alternative,
				
				"two.sided"={
					for(i in 1:length(num))
					{
						
						arrows(y0=rnum[i], y1=rnum[i], x0=lower[i], x1=upper[i],
								length = arrlength, angle = 90, code = code[i],
								col = CIcol[i], lty = CIlty[i], lwd = CIlwd[i])
						
					}
				},
				
				"less"={
					for(i in 1:length(num))
					{
						arrows(y0=rnum[i], y1=rnum[i], x0=llplot, x1=upper[i],
								length = arrlength, angle = 90, code = code[i],
								col = CIcol[i], lty = CIlty[i], lwd = CIlwd[i])
					}
				},
				
				"greater"={
					for(i in 1:length(num))
					{
						arrows(y0=rnum[i], y1=rnum[i], x0=lower[i], x1=uuplot,
								length = arrlength, angle = 90, code = code[i],
								col = CIcol[i], lty = CIlty[i], lwd = CIlwd[i])
					}
				})
		
		
	}
	
	if(HL){
		par(old.par)
	}
	
}