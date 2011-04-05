graph2latex <- function(graph, package="TikZ", scale=1, alpha=0.05, pvalues,
		fontsize=c("tiny","scriptsize", "footnotesize", "small",
		"normalsize", "large", "Large", "LARGE", "huge", "Huge"),
		nodeTikZ, labelTikZ="near start,above,fill=blue!20",
		tikzEnv=TRUE, offset=c(0,0)) {
	if (tikzEnv) {
		tikz <- paste("\\begin{tikzpicture}[scale=",scale,"]", sep="")
	} else {
		tikz <- ""
	}
	#tikz <- paste(tikz, "\\tikzset{help lines/.style=very thin}", paste="\n")	
	for (node in nodes(graph)) {
		nodeColor <- ifelse(getRejected(graph, node),"red!80", "green!80")
		x <- nodeRenderInfo(graph)$nodeX[node]*scale
		y <- nodeRenderInfo(graph)$nodeY[node]*scale
		#alpha <- format(getWeights(graph,node), digits=3, drop0trailing=TRUE)
		weight <- getLaTeXFraction(getWeights(graph,node))
		if (weight == 1) {
			weight <- "\\alpha"
		} else if (weight != "0") {
			weight <- paste(weight, "\\alpha", sep="")
		}
		double <- ""
		if (!missing(pvalues)) {
			if (is.null(names(pvalues))) {
				names(pvalues) <- nodes(graph)
			}
			if (canBeRejected(graph, node, alpha, pvalues)) { double <- "double," }
		}		
		nodeLine <- paste("\\node (",node,")",
				" at (",x+offset[1],"bp,",-y-offset[2],"bp)",
				"[draw,circle split,",ifelse(missing(nodeTikZ),"",paste(nodeTikZ,", ",sep="")),double,"fill=",nodeColor,"]",
				" {$",node,"$ \\nodepart{lower} $",weight,"$};",sep="")
		tikz <- paste(tikz, nodeLine,sep="\n")			
	}
	# A second loop for the edges is necessary:
	for (node in nodes(graph)) {
		edgeL <- edgeWeights(graph)[[node]]	
		if (length(edgeL)!=0) {
			for (i in 1:length(edgeL)) {	
				# The following to lines test whether the edge in opposite direction exists:
				weight <- try(edgeData(graph, names(edgeL[i]), node,"weight"), silent = TRUE)
				to <- ifelse(class(weight)=="try-error", "auto", "bend left=15")			
				#weight <- ifelse(edgeL[i]==0, "\\epsilon", getLaTeXFraction(edgeL[i])) # format(edgeL[i], digits=3, drop0trailing=TRUE))
				weight <- getWeightStr(graph, node, names(edgeL[i]), LaTeX=TRUE) 
				edgeLine <- paste("\\draw [->,line width=1pt] (",node,") to[",to,"] node[",labelTikZ,"] {$",weight,"$} (",names(edgeL[i]),");",sep="")
				tikz <- paste(tikz, edgeLine,sep="\n")
			}
		}
	}
	if (tikzEnv) tikz <- paste(tikz, "\\end{tikzpicture}\n",sep="\n")
	if (!missing(fontsize)) {
		tikz <- paste(paste("{\\", fontsize, sep=""), tikz, "}",sep="\n")
	}
	return(tikz)
}

getLaTeXFraction <- function(x) {
	nom <- strsplit(as.character(fractions(x)),split="/")[[1]]
	if (length(nom)==1) return(nom)
	return(paste("\\frac{",nom[1],"}{",nom[2],"}", sep=""))
}

gMCPReport <- function(object, file="", ...) {
	report <- LaTeXHeader()
	if (class(object)=="gMCPResult") {
		for(i in 1:length(object@graphs)) {
			report <- paste(report, paste("\\subsection*{Graph in Step ",i,"}", sep=""), sep="\n")
			report <- paste(report, graph2latex(object@graphs[[i]], ..., pvalues=object@pvalues), sep="\n")
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

LaTeXHeader <- function() {
	report <- "\\documentclass[11pt]{article}"
	report <- paste(report, "\\usepackage{tikz}", sep="\n")
	report <- paste(report, "\\usetikzlibrary{snakes,arrows,shapes}", sep="\n")
	report <- paste(report, "\\begin{document}", sep="\n")
	return(report)
}