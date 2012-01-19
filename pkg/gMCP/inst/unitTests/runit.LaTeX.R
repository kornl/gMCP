test.LaTeX <- function() {
	graphs <- list(BonferroniHolm(5),
			parallelGatekeeping(),
			improvedParallelGatekeeping(),
			BretzEtAl2011(),
			#HungEtWang2010(),
			#HuqueAloshEtBhore2011(),
			HommelEtAl2007(),
			HommelEtAl2007Simple(),
			MaurerEtAl1995(),
			improvedFallbackI(weights=rep(1/3, 3)),
			improvedFallbackII(weights=rep(1/3, 3)),
			cycleGraph(nodes=paste("H",1:4,sep=""), weights=rep(1/4, 4)),
			fixedSequence(5),
			fallback(weights=rep(1/4, 4)),
			#generalSuccessive(weights = c(1/2, 1/2)),
			simpleSuccessiveI(),
			simpleSuccessiveII(),
			#truncatedHolm(),
			BauerEtAl2001(),
			BretzEtAl2009a(),
			BretzEtAl2009b(),
			BretzEtAl2009c())
	
	report <- gMCP:::LaTeXHeader()
	for (graph in graphs) {
		report <- paste(report, graph2latex(graph), sep="\n")
	}
	report <- paste(report, "\\end{document}", sep="\n")
	#if (Sys.getenv("USER")=="kornel") cat(report, file="/home/kornel/test.tex")
}

test.fractionStrings <- function() {
	checkEquals(getLaTeXFraction(1/9), "\\fraction{1}{9}")
	checkTrue(getFractionString(1/9+0.000001) !="1/9")
}