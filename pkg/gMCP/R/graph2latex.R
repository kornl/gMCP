graph2latex <- function(graph, package="TikZ", scale=1, alpha=0.05, pvalues,
		fontsize=c("tiny","scriptsize", "footnotesize", "small",
		"normalsize", "large", "Large", "LARGE", "huge", "Huge"),
		nodeTikZ, labelTikZ="near start,above,fill=blue!20",
		tikzEnv=TRUE, offset=c(0,0),fill=list(reject="red!80",retain="green!80"), nodeR=25) {
	graph <- placeNodes(graph)
	colors <- c("yellow","black","blue","red","green")
	if (tikzEnv) {
		tikz <- paste("\\begin{tikzpicture}[scale=",scale,"]\n", sep="")
	} else {
		tikz <- ""
	}
	nodes2 <- getUsableNames(getNodes(graph))
	names(nodes2) <- getNodes(graph)
	#tikz <- paste(tikz, "\\tikzset{help lines/.style=very thin}", paste="\n")	
	for (node in getNodes(graph)) {
		nodeColor <- ifelse(getRejected(graph, node),fill$reject, fill$retain)
		x <- getXCoordinates(graph, node) + nodeR
		y <- getYCoordinates(graph, node) + nodeR
		#alpha <- format(getWeights(graph,node), digits=3, drop0trailing=TRUE)
		weight <- paste(getLaTeXFraction(getWeights(graph,node)), collapse=" ")
		if (weight == 1) {
			weight <- "\\alpha"
		} else if (weight != "0") {
			weight <- paste(weight, "\\alpha", sep="")
		}
		double <- ""
		if (!missing(pvalues)) {
			if (is.null(names(pvalues))) {
				names(pvalues) <- getNodes(graph)
			}
			if (canBeRejected(graph, node, alpha, pvalues)) { double <- "double," }
		}		
		nodeLine <- paste("\\node (",nodes2[node],")",
				" at (",x+offset[1],"bp,",-y-offset[2],"bp)",
				"[draw,circle split,",ifelse(missing(nodeTikZ),"",paste(nodeTikZ,", ",sep="")),double,"fill=",nodeColor,"]",
				" {$",node,"$ \\nodepart{lower} $",weight,"$};",sep="")
		tikz <- paste(tikz, nodeLine,sep="\n")			
	}
	# A second loop for the edges is necessary:
	if ("entangledMCP" %in% class(graph)) {
		for(k in 1:length(graph@subgraphs)) {
			subgraph <- graph@subgraphs[[k]]
			for (i in getNodes(subgraph)) {
				for (j in getNodes(subgraph)) {			
					if (subgraph@m[i,j]!=0) {
						# The following to lines test whether the edge in opposite direction exists:				
						to <- ifelse(subgraph@m[j,i]==0, "auto", "bend left=15")
						#weight <- ifelse(edgeL[i]==0, "\\epsilon", getLaTeXFraction(edgeL[i])) # format(edgeL[i], digits=3, drop0trailing=TRUE))
						weight <- getWeightStr(subgraph, i, j, LaTeX=TRUE) 
						edgeLine <- paste("\\draw [draw=",colors[k%%length(colors)+1],",->,line width=1pt] (",nodes2[i],") to[",to,"] node[",labelTikZ,"] {$",weight,"$} (",nodes2[j],");",sep="")
						tikz <- paste(tikz, edgeLine,sep="\n")
					}
				}
			}
		}
	} else {
		for (i in getNodes(graph)) {
			for (j in getNodes(graph)) {			
				if (graph@m[i,j]!=0) {
				  weight <- getWeightStr(graph, i, j, LaTeX=TRUE) 
				  edgeNode <- paste("node[",labelTikZ,"] {$",weight,"$}",sep="")
					# The following line test whether the edge in opposite direction exists:				          
					to <- paste(") to[",ifelse(graph@m[j,i]==0, "auto", "bend left=15"),"] ", edgeNode, sep="")
          edgeNode <- paste("node[","fill=blue!20","] {$",weight,"$}",sep="") # TODO labelTikZ is ignored in this case
          # New arc function:
					x <- try(unlist(edgeAttr(graph, i, j, "labelX")), silent = TRUE)          
					y <- try(unlist(edgeAttr(graph, i, j, "labelY")), silent = TRUE)
					if (class(x)!="try-error" && !is.null(x) && !is.na(x) && class(y)!="try-error" && !is.null(y) && !is.na(y) && x>-10 && y>-10) {
					  b <- c(x,y) + nodeR
					  x <- getXCoordinates(graph, c(i,j)) + nodeR
					  y <- getYCoordinates(graph, c(i,j)) + nodeR
					  to <- getArc(c(x[1],y[1]),b,c(x[2],y[2]), edgeNode)					  
					}          
					#weight <- ifelse(edgeL[i]==0, "\\epsilon", getLaTeXFraction(edgeL[i])) # format(edgeL[i], digits=3, drop0trailing=TRUE))					
					edgeLine <- paste("\\draw [->,line width=1pt] (",nodes2[i], to," (",nodes2[j],");",sep="")
					tikz <- paste(tikz, edgeLine,sep="\n")
				}
			}
		}
	}
	if (tikzEnv) tikz <- paste(tikz, "\\end{tikzpicture}\n",sep="\n")
	if (!missing(fontsize)) {
		tikz <- paste(paste("{\\", fontsize, sep=""), tikz, "}",sep="\n")
	}
	return(tikz)
}

# Arc from a to b and from b to c.
getArc <- function(a, b, c, edgeNode, col="black") {
  #a <- invertY(a)
  #b <- invertY(b)
  #c <- invertY(c)
  m <- getCenter(a,b,c,0.001)
  r <- sqrt(sum((m-a)^2))
  phi <- getAngle(a,b,c,m)
  #cat("a: ",a,", b: ",b,", c:", c,"m: ",m,"r: ",r,", phi: ",phi,"\n")
  return(paste(".",round(phi[1]+ifelse(phi[1]>phi[2],-90,90)),") arc(",round(phi[1]),":",round(phi[3]),":",round(r),"bp) ",edgeNode," arc(",round(phi[3]),":",round(phi[2]),":",round(r),"bp) to",sep=""))
}

getAngleNew <- function(a,b,c,m, nodeR=20*scale, scale=1) {
    
  
}

invertY <- function(x) {
  return(c(x[1],-x[2]))
}

#getCenterOfNode <- function(x,r) {
  #return(c(x[1]+r,x[2]-r))
#}

getAngle <- function(a,b,c,m, nodeR=20) {
  # phi correction factor:
  r <- sqrt((m[1]-a[1])*(m[1]-a[1])+(m[2]-a[2])*(m[2]-a[2]))
  #phiCF <- (nodeR*360)/(2*pi*r)
  phiCF <- 2*asin(nodeR/(2*r))/(2*pi)*360
  
  if ((a[1]-m[1])==0) {
    phi1 <- 90 + ifelse((m[2]-a[2]>0),0,180)
  } else {
    phi1 <- atan((-a[2]+m[2])/(a[1]-m[1]))*360/(2*pi)+ifelse((a[1]-m[1]<0),180,0)
  }
  if ((c[1]-m[1])==0) {
    phi2 <- 90 + ifelse((m[2]-c[2]>0),0,180)
  } else {
    phi2 <- atan((-c[2]+m[2])/(c[1]-m[1]))*360/(2*pi)+ifelse((c[1]-m[1]<0),180,0)
  }
  if ((b[1]-m[1])==0) {
    phi3 <- 90 + ifelse((m[2]-b[2]>0),0,180)
  } else {
    phi3 <- atan((-b[2]+m[2])/(b[1]-m[1]))*360/(2*pi)+ifelse((b[1]-m[1]<0),180,0)
  }		
  phi1 <- (phi1 + 360) %% 360 # phi for a
  phi2 <- (phi2 + 360) %% 360 # phi for c
  phi3 <- (phi3 + 360) %% 360 # phi for b
  #return(c(phi1, phi2, phi3))
  if ((phi1 > phi2 && phi1 > phi3 && phi3 > phi2) || (phi2 > phi1 && (phi3>phi2 || phi3<phi1))) {  
    # Clockwise direction: phi2 < phi1
    phi1 <- phi1 - phiCF
    phi2 <- phi2 + phiCF + 2
    if (phi3>phi1) phi3 <- phi3 -360
    if (phi2<phi1){
      return(c(phi1, phi2, phi3))
    } else {
      return(c(phi1, phi2-360, phi3))
    }
  } else {
    # Counter clockwise: phi1 < phi2
    phi1 <- phi1 + phiCF
    phi2 <- phi2 - phiCF - 2
    if (phi3<phi1) phi3 <- phi3 + 360
    if (phi1<phi2) {      
      return(c(phi1, phi2, phi3))
    } else {
      return(c(phi1, phi2+360, phi3))
    }
  }
}

getCenter <- function(a,b,c, eps=0.05) {  
  if((b[2]-c[2])==0) {
    x <- c(0,1)
  } else {
    x <- c(1,-(b[1]-c[1])/(b[2]-c[2]))
  }
  if ((a[2]-b[2])==0) {
    z <- c(0,1)
  } else {
    z <- c(1,-(a[1]-b[1])/(a[2]-b[2]))
  }
  if (abs((b[1]-a[1])/(b[2]-a[2])-(c[1]-b[1])/(c[2]-b[2]))<eps && sign(b[1]-a[1])==sign(c[1]-b[1])) {
    stop("Slopes are to similar")
  }
  if (z[1]!=0 && x[1]==0) {			
    c <- (c[1]-a[1])/(2*z[1])
    return(c((a[1]+b[1])/2+c*z[1], (a[2]+b[2])/2+c*z[2]))
  } else if (x[1]!=0 && z[1]==0) {
    d <- (a[1]-c[1])/(2*x[1])
    return(c((b[1]+c[1])/2+d*x[1], (b[2]+c[2])/2+d*x[2]))
  } else if ((x[1]==0 && z[1]==0)||(x[2]==0 && z[2]==0)) {
    stop("Slopes are too similar.")
  } else if (z[2]!=0 && x[2]==0) {			
    c <- (c[2]-a[2])/(2*z[2])
    return(c((a[1]+b[1])/2+c*z[1], (a[2]+b[2])/2+c*z[2]))
  } else if (x[2]!=0 && z[2]==0) {			
    d <- (a[2]-c[2])/(2*x[2])
    return(c((b[1]+c[1])/2+d*x[1], (b[2]+c[2])/2+d*x[2]))
  } else {
    if ((x[2]-x[1]*z[2]/z[1])==0) {
      if ((z[2]-z[1]*x[2]/x[1])==0) stop("Can this happen?")
      c <- ((c[2]-a[2])/2+((a[1]-c[1])/2*x[1])*x2)/(z[2]-z[1]*x[2]/x[1])
      return(c((a[1]+b[1])/2+c*z[1], (a[2]+b[2])/2+c*z[2]))			
    }
    d <- ((a[2]-c[2])/2+((c[1]-a[1])/2*z[1])*z[2])/(x[2]-x[1]*z[2]/z[1])		
  }  
  m <- c((b[1]+c[1])/2+d*x[1], (b[2]+c[2])/2+d*x[2])
  return(m)
}



# x <- c("H+1","H-1","H/1","H1","H2","H1+")
# getUsableNames(x)
# [1] "H1"  "H12" "H13" "H14" "H2"  "H15"
getUsableNames <- function(x) {
	x <- removeSymbols(x)
	for (i in which(duplicated(x))) {
		name <- x[i]
		j <- 2
		while (any(x==paste(x[i],j,sep=""))) {
			j <- j + 1
		}
		x[i] <- paste(x[i],j,sep="")
	}
	return(x)
}

getLaTeXFraction <- function(x) {
	result <- c()
	for (nom in strsplit(as.character(getFractionString(x)),split="/")) {		
		if (length(nom)==1) {
			result <- c(result, nom)
		} else {
			result <- c(result, paste("\\frac{",nom[1],"}{",nom[2],"}", sep=""))
		}
	}
	return(result)
}

gMCPReport <- function(object, file="", ...) {
	report <- LaTeXHeader()
	if (class(object)=="gMCPResult") {
		report <- paste(report, "\\subsection*{Initial graph}", sep="\n")
		report <- paste(report, graph2latex(object@graphs[[1]], ..., pvalues=object@pvalues), sep="\n")
		report <- paste(report, "\\subsection*{P-Values}", sep="\n")
		report <- paste(report, createTable(object@pvalues), sep="\n")	
		if (length(object@adjPValues)>0) {
			report <- paste(report, "\\subsection*{Adjusted p-values}", sep="\n")
			report <- paste(report, createTable(object@adjPValues), sep="\n")	
		}
		if (length(object@rejected)>0) {
			report <- paste(report, paste("\\subsection*{Rejected Hypotheses with $\\alpha=",object@alpha,"$}", sep=""), sep="\n")
			report <- paste(report, createTable(object@rejected), sep="\n")
		}
		if (length(object@graphs)>1) {
			for(i in 2:length(object@graphs)) {
				report <- paste(report, paste("\\subsection*{Graph in Step ",i,"}", sep=""), sep="\n")
				report <- paste(report, graph2latex(object@graphs[[i]], ..., pvalues=object@pvalues), sep="\n")
			}		
		}
	} else if (class(object)=="graphMCP") {		
		report <- paste(report, "\\subsection*{Graph for SRMTP}", sep="\n")
		report <- paste(report, graph2latex(object, ...), sep="\n")
	} else {
		stop("object has to be of class gMCPResult or graphMCP.")
	} 
	report <- paste(report, "\\end{document}", sep="\n")
	cat(report, file=file)
}

createTable <- function(vector) {
	table <- paste("\\begin{table}[ht]",
	"\\begin{center}", sep="\n")
	table <- paste(table, 
		"\n\\begin{tabular}{",paste(rep("r",length(vector)),collapse=""),"}\n",
		"\\hline\n", sep="")
    values <- paste(vector, collapse="&")
	if (is.numeric(vector)) values <- paste(sprintf("%.5f", vector), collapse="&")
	table <- paste(table, "\n", paste(names(vector), collapse="&"), " \\\\\n\\hline\n ", values, "\\\\\n\\hline\n ", sep="") 
	table <- paste(table, 
		"\\end{tabular}",
		"\\end{center}",
		"\\end{table}", sep="\n");
	return(table)
}

LaTeXHeader <- function() {
	report <- "\\documentclass[11pt]{article}"
	report <- paste(report, "\\usepackage{tikz}", sep="\n")
	report <- paste(report, "\\usetikzlibrary{decorations,arrows,shapes}", sep="\n")
	report <- paste(report, "\\begin{document}", sep="\n")
	return(report)
}
