graph2latex <- function(graph, package="TikZ") {
	tikz <- "\\begin{tikzpicture}"
	for (node in nodes(graph)) {
		nodeColor <- ifelse(isRejected(graph, node),"red!80", "green!80")
		x <- 100
		y <- 100
		nodeLine <- paste("\\node (",node,") at (",x,"bp,",y,"bp) [draw,circle,fill=",nodeColor,"] {$",node,"$ \\nodepart{lower} $",format(getAlpha(graph,node), digits=3 ,drop0trailing=TRUE),"$};",sep="")
		tikz <- paste(tikz, nodeLine,sep="\n")			
	}
	# A second loop for the edges is necessary:
	for (node in nodes(graph)) {
		edgeL <- edgeWeights(graph)[[node]]	
		if (length(edgeL)!=0) {
			for (i in 1:length(edgeL)) {
				weight <- try(edgeData(graph, names(edgeL[i]), node,"weight"), silent = TRUE)
				to <- ifelse(class(weight)=="try-error", "auto", "bend left")			
				weight <- ifelse(edgeL[i]==0, "\\epsilon", edgeL[i])
				edgeLine <- paste("\\draw [->] (",node,") to[",to,"] node[fill=blue!20] {",weight,"} (",names(edgeL[i]),");",sep="")
				tikz <- paste(tikz, edgeLine,sep="\n")
			}
		}
	}
	tikz <- paste(tikz, "\\end{tikzpicture}\n",sep="\n")
	return(tikz)
}

createGsrmtpReport <- function(srmtpResult, file="", package="TikZ") {
	report <- "\\documentclass[11pt]{article}"
	report <- paste(report, "\\usepackage{tikz}", sep="\n")
	report <- paste(report, "\\usetikzlibrary{snakes,arrows,shapes}", sep="\n")
	report <- paste(report, "\\begin{document}", sep="\n")
	for(graph in srmtpResult@graphs) {
		report <- paste(report, "\\subsection*{Graph}", sep="\n")
		report <- paste(report, graph2latex(graph), sep="\n")
	}
	report <- paste(report, "\\end{document}", sep="\n")
	cat(report, file=file)
}
