graph2latex <- function(graph) {
	tikz <- "\begin{tikzpicture}\n"
	for (node in nodes(graph)) {
		nodeColor <- ifelse(isRejected(node,"red!80", "green!80"))
		x <- 100
		y <- 100
		nodeLine <- paste("\node (",node,") at (",x,"bp,",y,"bp) [draw,circle,fill=",nodecolor,"] {$",node,"$};",sep="")
		tikz <- paste(tikz, nodeLine,sep="\n")
	}
	for (edge in edges(graph)) {
		edgeLine <- paste("\draw [->] (B) to[bend left] node[auto] {1} (A);",sep="")
		tikz <- paste(tikz, edgeLine,sep="\n")
	}
	tikz <- paste(tikz, "\end{tikzpicture}",sep="\n")
	return(tikz)
}