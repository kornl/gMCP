graph2latex <- function(graph, package="TikZ", scale=1, alpha=0.05, pvalues,
		fontsize=c("tiny","scriptsize", "footnotesize", "small",
		"normalsize", "large", "Large", "LARGE", "huge", "Huge"),
		nodeTikZ, labelTikZ="near start,above,fill=blue!20",
		tikzEnv=TRUE, offset=c(0,0)) {
	graph <- placeNodes(graph)
	if (tikzEnv) {
		tikz <- paste("\\begin{tikzpicture}[scale=",scale,"]", sep="")
	} else {
		tikz <- ""
	}
	#tikz <- paste(tikz, "\\tikzset{help lines/.style=very thin}", paste="\n")	
	for (node in nodes(graph)) {
		nodeColor <- ifelse(getRejected(graph, node),"red!80", "green!80")
		x <- getXCoordinates(graph)*scale
		y <- getYCoordinates(graph)*scale
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
	for (i in nodes(graph)) {
		for (j in nodes(graph)) {
			if (graph@m[i,j]!=0) {
				# The following to lines test whether the edge in opposite direction exists:				
				to <- ifelse(graph@m[j,i]==0, "auto", "bend left=15")
				#weight <- ifelse(edgeL[i]==0, "\\epsilon", getLaTeXFraction(edgeL[i])) # format(edgeL[i], digits=3, drop0trailing=TRUE))
				weight <- getWeightStr(graph, i, j, LaTeX=TRUE) 
				edgeLine <- paste("\\draw [->,line width=1pt] (",i,") to[",to,"] node[",labelTikZ,"] {$",weight,"$} (",j,");",sep="")
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
		report <- paste(report, "\\subsection*{Initial graph}", sep="\n")
		report <- paste(report, graph2latex(object@graphs[[1]], ..., pvalues=object@pvalues), sep="\n")
		report <- paste(report, "\\subsection*{P-Values}", sep="\n")
		report <- paste(report, print(xtable(t(as.matrix(object@pvalues))),include.rownames=FALSE, file=tempfile()), sep="\n")	
		if (length(object@adjPValues)>0) {
			report <- paste(report, "\\subsection*{Adjusted p-values}", sep="\n")
			report <- paste(report, print(xtable(t(as.matrix(object@adjPValues))),include.rownames=FALSE, file=tempfile()), sep="\n")	
		}
		report <- paste(report, paste("\\subsection*{Rejected Hypotheses with $\\alpha=",object@alpha,"$}", sep=""), sep="\n")
		report <- paste(report, print(xtable(t(as.matrix(object@rejected))),include.rownames=FALSE, file=tempfile()), sep="\n")
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

LaTeXHeader <- function() {
	report <- "\\documentclass[11pt]{article}"
	report <- paste(report, "\\usepackage{tikz}", sep="\n")
	report <- paste(report, "\\usetikzlibrary{snakes,arrows,shapes}", sep="\n")
	report <- paste(report, "\\begin{document}", sep="\n")
	return(report)
}