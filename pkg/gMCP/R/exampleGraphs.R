BonferroniHolmGraph <- function(n) {
	if (missing(n)) { stop("Please provide the number of hypotheses as parameter n.") }
	weights <- rep(1/n, n)
	hnodes <- paste("H", 1:n, sep="")
	edges <- vector("list", length=n)
	for(i in 1:n) {
		edges[[i]] <- list(edges=hnodes[(1:n)[-i]], weights=rep(1/(n-1),n-1))
	}
	names(edges)<-hnodes
	BonferroniHolmGraph <- new("graphMCP", nodes=hnodes, edgeL=edges, weights=weights)
	# Visualization settings
	nodeX <- 100+(0:(n-1))*200
	nodeY <- rep(200, n)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(BonferroniHolmGraph) <- list(nodeX=nodeX, nodeY=nodeY)	
	# Label settings
	for (i in 1:n) {
		n1 <- hnodes[i]
		for (j in (1:n)[-i]) {
			n2 <- hnodes[j]
			x <- ((i+j)*200-200)/2
			y <- 200 + ((i-j)*50)
			edgeData(BonferroniHolmGraph, n1, n2, "labelX") <- x
			edgeData(BonferroniHolmGraph, n1, n2, "labelY") <- y
		}
	}
	attr(BonferroniHolmGraph, "description") <- paste("Graph representing the Bonferroni-Holm-Procedure", 
			"",
			"Most powerful test procedure that treats all hypotheses equally.",
			"The graph is a complete graph, where all nodes have the same weights and each edge weight is 1/(n-1).",
			"",
			"Literature: Holm, S. (1979). A simple sequentally rejective multiple test procedure. Scandinavian Journal of Statistics 6, 65-70.", sep="\n")
	return(BonferroniHolmGraph)
}

graphFromBretzEtAl2009 <- function() {
	# Nodes:
	weights <- rep(c(1/3,0), each=3)	
	hnodes <- paste("H", rep(1:3, 2), rep(1:2, each=3), sep="")
	# Edges:
	edges <- vector("list", length=6)
	edges[[1]] <- list(edges=c("H21","H12"), weights=rep(1/2, 2))
	edges[[2]] <- list(edges=c("H11","H31","H22"), weights=rep(1/3, 3))
	edges[[3]] <- list(edges=c("H21","H32"), weights=rep(1/2, 2))
	edges[[4]] <- list(edges="H21", weights=1)
	edges[[5]] <- list(edges=c("H11","H31"), weights=rep(1/2, 2))
	edges[[6]] <- list(edges="H21", weights=1)
	names(edges)<-hnodes
	# Graph creation
	graph <- new("graphMCP", nodes=hnodes, edgeL=edges, weights=weights)
	# Visualization settings
	nodeX <- rep(c(100, 300, 500), 2)
	nodeY <- rep(c(100, 300), each=3)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)
	# Label placement
	edgeData(graph, "H11", "H21", "labelX") <- 200
	edgeData(graph, "H11", "H21", "labelY") <- 80	
	edgeData(graph, "H31", "H21", "labelX") <- 400
	edgeData(graph, "H31", "H21", "labelY") <- 80	
	edgeData(graph, "H21", "H11", "labelX") <- 200
	edgeData(graph, "H21", "H11", "labelY") <- 120	
	edgeData(graph, "H21", "H31", "labelX") <- 400
	edgeData(graph, "H21", "H31", "labelY") <- 120	
	edgeData(graph, "H12", "H21", "labelX") <- 150
	edgeData(graph, "H12", "H21", "labelY") <- 250	
	edgeData(graph, "H22", "H11", "labelX") <- 250
	edgeData(graph, "H22", "H11", "labelY") <- 250	
	edgeData(graph, "H32", "H21", "labelX") <- 450
	edgeData(graph, "H32", "H21", "labelY") <- 250	
	edgeData(graph, "H22", "H31", "labelX") <- 350
	edgeData(graph, "H22", "H31", "labelY") <- 250	
	attr(graph, "description") <- paste("Graph representing ", 
			"",
			"Literature: .", sep="\n")
	return(graph)	
}


graphFromHommelEtAl2007 <- function() {
	# Nodes:
	weights <- c(rep(1/3, 3), rep(0,4))	
	hnodes <- c("E1", "QoL", "E2", "D1", "D2", "D3", "D4")
	# Edges:
	edges <- vector("list", length=7)
	edges[[1]] <- list(edges="QoL", weights=1)
	edges[[2]] <- list(edges=c("D1","D2","D3","D4"), weights=rep(1/4, 4))
	edges[[3]] <- list(edges="QoL", weights=1)	
	edges[[4]] <- list(edges=c("E1","E2"), weights=c(0, 0))
	edges[[5]] <- list(edges=c("E1","E2"), weights=c(0, 0))
	edges[[6]] <- list(edges=c("E1","E2"), weights=c(0, 0))
	edges[[7]] <- list(edges=c("E1","E2"), weights=c(0, 0))
	names(edges)<-hnodes
	# Graph creation
	graph <- new("graphMCP", nodes=hnodes, edgeL=edges, weights=weights)
	# Visualization settings
	nodeX <- c(200, 400, 600, 100, 300, 500, 700)
	nodeY <- c(100, 100, 100, 300, 300, 300, 300)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)
	for (i in 1:4) {
		n1 <- hnodes[3+i]
		edgeData(graph, n1, "E1", "epsilon") <- list(1)
		edgeData(graph, n1, "E2", "epsilon") <- list(1)
		for (j in (1:4)[-i]) {
			n2 <- hnodes[3+j]
			graph <- addEdge(n1, n2, graph, 1/3)
			edgeData(graph, n1, n2, "epsilon") <- list(-2/3)	
			x <- ((i+j)*200-200)/2+sign(i-j)*30
			y <- 300 + ((abs(i-j)-1)*60)+sign(i-j)*10+10
			edgeData(graph, n1, n2, "labelX") <- x
			edgeData(graph, n1, n2, "labelY") <- y
		}
	}
	attr(graph, "description") <- paste("Graph representing ", 
			"",
			"Literature: .", sep="\n")
	return(graph)	
}

graphForParallelGatekeeping <- function() {
	# Nodes:
	weights <- rep(c(1/2,0), each=2)	
	hnodes <- paste("H", 1:4, sep="")
	# Edges:
	edges <- vector("list", length=4)
	edges[[1]] <- list(edges=c("H3","H4"), weights=rep(1/2, 2))
	edges[[2]] <- list(edges=c("H3","H4"), weights=rep(1/2, 2))
	edges[[3]] <- list(edges=c("H4"), weights=1)
	edges[[4]] <- list(edges=c("H3"), weights=1)
	names(edges)<-hnodes
	# Graph creation
	graph <- new("graphMCP", nodes=hnodes, edgeL=edges, weights=weights)
	# Visualization settings
	nodeX <- rep(c(100, 300), 2)
	nodeY <- rep(c(100, 300), each=2)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)
	# Label placement
	edgeData(graph, "H1", "H4", "labelX") <- 150
	edgeData(graph, "H1", "H4", "labelY") <- 150
	edgeData(graph, "H2", "H3", "labelX") <- 250
	edgeData(graph, "H2", "H3", "labelY") <- 150
	attr(graph, "description") <- paste("Graph representing ", 
			"",
			"Literature: .", sep="\n")
	return(graph)	
}

graphForImprovedParallelGatekeeping <- function() {
	graph <- graphForParallelGatekeeping()
	graph <- addEdge("H3", "H1", graph, 0)
	edgeData(graph, "H3", "H1", "epsilon") <- list(1)	
	graph <- addEdge("H4", "H2", graph, 0)
	edgeData(graph, "H4", "H2", "epsilon") <- list(1)
	edgeData(graph, "H3", "H4", "epsilon") <- list(-1)
	edgeData(graph, "H4", "H3", "epsilon") <- list(-1)
	attr(graph, "description") <- paste("Graph representing ", 
			"",
			"Literature: .", sep="\n")
	return(graph)	
}

graph2FromBretzEtAl2009 <- function() {
	# Nodes:
	weights <- rep(c(1/2,0), each=2)	
	hnodes <- paste("H", 1:4, sep="")
	# Edges:
	edges <- vector("list", length=4)
	edges[[1]] <- list(edges=c("H2","H3"), weights=c(NaN, NaN)) # c(γ, 1-γ)
	edges[[2]] <- list(edges=c("H1","H4"), weights=c(NaN, NaN)) # c(δ, 1-δ)
	edges[[3]] <- list(edges=c("H2"), weights=1)
	edges[[4]] <- list(edges=c("H1"), weights=1)
	names(edges)<-hnodes
	# Graph creation
	graph <- new("graphMCP", nodes=hnodes, edgeL=edges, weights=weights)
	# Set edge weights with variables
	edgeData(graph, "H1", "H2", "variableWeight") <- "\\gamma"
	edgeData(graph, "H1", "H3", "variableWeight") <- "1-\\gamma"
	edgeData(graph, "H2", "H1", "variableWeight") <- "\\delta"
	edgeData(graph, "H2", "H4", "variableWeight") <- "1-\\delta"
	# Visualization settings
	nodeX <- rep(c(100, 300), 2)
	nodeY <- rep(c(100, 300), each=2)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)
	attr(graph, "description") <- paste("Graph representing ", 
			"",
			"Literature: .", sep="\n")
	return(graph)
}

graphFromHungEtWang2010 <- function() {
	# Nodes:
	weights <- rep(c(1/2,0), each=2)	
	hnodes <- c("H_{1,NI}","H_{1,S}","H_{2,NI}","H_{2,S}")
	# Edges:
	edges <- vector("list", length=4)
	edges[[1]] <- list(edges=c("H_{1,S}","H_{2,NI}"), weights=c(NaN, NaN)) # c(nu, 1-nu)
	edges[[2]] <- list(edges=c("H_{2,NI}","H_{2,S}"), weights=c(NaN, NaN)) # c(tau, 1-tau)
	edges[[3]] <- list(edges=c("H_{1,S}","H_{2,S}"), weights=c(NaN, NaN))  # c(omega, 1-omega)
	edges[[4]] <- list()
	names(edges)<-hnodes
	# Graph creation
	graph <- new("graphMCP", nodes=hnodes, edgeL=edges, weights=weights)
	# Set edge weights with variables
	edgeData(graph, "H_{1,NI}", "H_{1,S}", "variableWeight") <- "\\nu"
	edgeData(graph, "H_{1,NI}", "H_{2,NI}", "variableWeight") <- "1-\\nu"
	edgeData(graph, "H_{1,S}", "H_{2,NI}", "variableWeight") <- "\\tau"
	edgeData(graph, "H_{1,S}", "H_{2,S}", "variableWeight") <- "1-\\tau"
	edgeData(graph, "H_{2,NI}", "H_{1,S}", "variableWeight") <- "\\omega"
	edgeData(graph, "H_{2,NI}", "H_{2,S}", "variableWeight") <- "1-\\omega"
	# Visualization settings
	nodeX <- rep(c(100, 300), 2)
	nodeY <- rep(c(100, 300), each=2)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)
	attr(graph, "description") <- paste("Graph representing ", 
			"",
			"Literature: .", sep="\n")
	return(graph)
}

graphFromMaurerEtAl1995 <- function() {
	# Nodes:
	weights <- c(1,0,0,0,0)	
	hnodes <- paste("H", 1:5, sep="")
	# Edges:
	edges <- vector("list", length=4)
	edges[[1]] <- list(edges=c("H2"), weights=1)
	edges[[2]] <- list(edges=c("H3"), weights=1)
	edges[[3]] <- list(edges=c("H4","H5"), weights=c(1/2,1/2))
	edges[[4]] <- list()#edges=c("H5"), weights=1)
	edges[[5]] <- list()#edges=c("H4"), weights=1)
	names(edges)<-hnodes
	# Graph creation
	graph <- new("graphMCP", nodes=hnodes, edgeL=edges, weights=weights)
	# Visualization settings
	nodeX <- c(100, 200, 300, 400, 400)
	nodeY <- c(100, 100, 100, 50, 150)
	names(nodeX) <- hnodes
	names(nodeY) <- hnodes
	nodeRenderInfo(graph) <- list(nodeX=nodeX, nodeY=nodeY)
	attr(graph, "description") <- paste("Graph representing a procedure in drug clinical trials (from Maurer et al. 1995, Scenario 1)",
			"",
			"In a univariate one-way design a drug A is compared against placebo and two positive control drugs B and C.",
			"",
			"The order of importance is that first the sensitivity has to be shown, i.e. that drug B and C are better than placebo. Than the efficacy of A vs. placebo is tested and if this can be shown, it is tested (with Bonferroni correction) whether A is superior to drug B and/or C.",
			"",
			"These hypotheses are represented in the graph as follows:",
			"H1: drug B better than placebo",
			"H2: drug C better than placebo",
			"H3: drug A better than placebo",
			"H4: drug A better than drug B",
			"H5: drug A better than drug C",
			"(Maurer et al. apply the intersection-union principle to H1 and H2 to test sensitivity, so sensitivity is shown if and only if H1 and H2 are both rejected.)",
			"Note that you could improve the test procedure by using a Bonferroni-Holm correction instead of the Bonferroni correction in the last step by adding an edge from H4 to H5 with weight 1 and vice versa.",
			"",
			"Literature:",
			"W. Maurer, L. Hothorn, W. Lehmacher: Multiple comparisons in drug clinical trials and preclinical assays: a-priori ordered hypotheses. In Biometrie in der chemisch-pharmazeutischen Industrie, Vollmar J (ed.). Fischer Verlag: Stuttgart, 1995; 3-18.", sep="\n")	
	return(graph)	
}

cycleGraph <- function(nodes, weights) {
	# Edges:
	n <- length(nodes)
	edges <- vector("list", length=n)
	for (i in 1:n-1) {
		edges[[i]] <- list(edges=nodes[i+1], weights=weights[i])
	}	
	edges[[n]] <- list(edges=nodes[1], weights=weights[n])
	names(edges)<-nodes
	# Graph creation
	graph <- new("graphMCP", nodes=nodes, edgeL=edges, weights=weights)
	return(graph)
}

exampleGraph <- function(graph, ...) {
	switch(graph,
			Hommel=graphFromHommelEtAl2007(),
			Bretz=graphFromBretzEtAl2009(),
			ParallelGatekeeping=graphForParallelGatekeeping(),
			ImprovedParallelGatekeeping=graphForImprovedParallelGatekeeping(),
			BonferroniHolm=BonferroniHolmGraph(...))
}