graph2latex <- function(graph, package="TikZ", scale=0.7, pvalues) {	
	tikz <- paste("\\begin{tikzpicture}[scale=",scale,"]", sep="")
	#tikz <- paste(tikz, "\\tikzset{help lines/.style=very thin}", paste="\n")	
	for (node in nodes(graph)) {
		nodeColor <- ifelse(getRejected(graph, node),"red!80", "green!80")
		x <- nodeRenderInfo(graph)$nodeX[node]*20*scale
		y <- nodeRenderInfo(graph)$nodeY[node]*20*scale
		alpha <- format(getAlpha(graph,node), digits=3 ,drop0trailing=TRUE)
		double <- ""
		if (!missing(pvalues)) {
			if (is.null(names(pvalues))) {
				names(pvalues) <- nodes(graph)
			}
			if (canBeRejected(graph, node, pvalues)) { double <- "double," }
		}		
		nodeLine <- paste("\\node (",node,") at (",x,"bp,",y,"bp) [draw,circle split,",double,"fill=",nodeColor,"] {$",node,"$ \\nodepart{lower} $",alpha,"$};",sep="")
		tikz <- paste(tikz, nodeLine,sep="\n")			
	}
	# A second loop for the edges is necessary:
	for (node in nodes(graph)) {
		edgeL <- edgeWeights(graph)[[node]]	
		if (length(edgeL)!=0) {
			for (i in 1:length(edgeL)) {
				weight <- try(edgeData(graph, names(edgeL[i]), node,"weight"), silent = TRUE)
				to <- ifelse(class(weight)=="try-error", "auto", "bend left=15")			
				weight <- ifelse(edgeL[i]==0, "\\epsilon", format(edgeL[i], digits=3 ,drop0trailing=TRUE))
				edgeLine <- paste("\\draw [->,line width=1pt] (",node,") to[",to,"] node[near start,above,fill=blue!20] {",weight,"} (",names(edgeL[i]),");",sep="")
				tikz <- paste(tikz, edgeLine,sep="\n")
			}
		}
	}
	tikz <- paste(tikz, "\\end{tikzpicture}\n",sep="\n")
	return(tikz)
}

createGsrmtpReport <- function(srmtpResult, file="", ...) {
	report <- "\\documentclass[11pt]{article}"
	report <- paste(report, "\\usepackage{tikz}", sep="\n")
	report <- paste(report, "\\usetikzlibrary{snakes,arrows,shapes}", sep="\n")
	report <- paste(report, "\\begin{document}", sep="\n")
	for(i in 1:length(srmtpResult@graphs)) {
		report <- paste(report, paste("\\subsection*{Graph in Step ",i,"}", sep=""), sep="\n")
		report <- paste(report, graph2latex(srmtpResult@graphs[[i]], ..., pvalues=srmtpResult@pvalues), sep="\n")
	}
	report <- paste(report, "\\end{document}", sep="\n")
	cat(report, file=file)
}
