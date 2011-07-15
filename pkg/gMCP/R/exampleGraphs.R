BonferroniHolm <- function(n) {
	if (missing(n)) { stop("Please provide the number of hypotheses as parameter n.") }
	weights <- rep(1/n, n)
	hnodes <- paste("H", 1:n, sep="")
	m <- matrix(1/(n-1), nrow=n, ncol=n)
	diag(m) <- 0
	rownames(m) <- colnames(m) <- hnodes
	BonferroniHolm <- new("graphMCP", m=m, weights=weights)
	# Visualization settings
	nodeX <- 100+(0:(n-1))*200
	nodeY <- rep(200, n)
	BonferroniHolm@nodeData$X <- nodeX
	BonferroniHolm@nodeData$Y <- nodeY		
	# Label settings
	for (i in 1:n) {
		n1 <- hnodes[i]
		for (j in (1:n)[-i]) {
			n2 <- hnodes[j]
			x <- ((i+j)*200-200)/2
			y <- 200 + ((i-j)*50)
			edgeData(BonferroniHolm, n1, n2, "labelX") <- x			
			edgeData(BonferroniHolm, n1, n2, "labelY") <- y
		}
	}
	attr(BonferroniHolm, "description") <- paste("Graph representing the (unweighted) Bonferroni-Holm-Procedure", 
			"",
			#"Most powerful test procedure (without further assumptions) that treats all hypotheses equally.",
			"The graph is a complete graph, where all nodes have the same weights and each edge weight is 1/(n-1).",
			"",
			"Literature: Holm, S. (1979). A simple sequentally rejective multiple test procedure. Scandinavian Journal of Statistics 6, 65-70.", sep="\n")
	return(BonferroniHolm)
}

BretzEtAl2011 <- function() {
	# M:
	m <- rbind(H11=c(0,   0.5, 0,   0.5, 0,   0  ),
			H21=c(1/3, 0,   1/3, 0,   1/3, 0  ),
			H31=c(0,   0.5, 0,   0,   0,   0.5),
			H12=c(0,   1,   0,   0,   0,   0  ),
			H22=c(0.5, 0,   0.5, 0,   0,   0  ),
			H32=c(0,   1,   0,   0,   0,   0  ))	
	# Graph creation
	weights <- c(1/3, 1/3, 1/3, 0, 0, 0)
	graph <- new("graphMCP", m=m, weights=weights)
	# Visualization settings
	nodeX <- rep(c(100, 300, 500), 2)
	nodeY <- rep(c(100, 300), each=3)
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY	
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
	attr(graph, "description") <- paste("Graph representing the procedure from Bretz et al. (2011) - Figure 2", 
			"",
			"H11, H21 and H31 represent three primary hypotheses and H21, H22 and H23 the associated secondary hypotheses.",
			"",			
			"A secondary hypothesis is only tested if the associated primary hypotheses is rejected.",
			"",
			"Since in this example it is preferred to reject two adjacent hypotheses (like H11 and H21 instead of H11 and H31) there are only edges between adjacent nodes.",
			"",
			"Literature: Bretz, F., Maurer, W. and Hommel, G. (2011), Test and power considerations for multiple endpoint analyses using sequentially rejective graphical procedures. Statistics in Medicine, 30: n/a.", sep="\n")
	return(graph)	
}


BretzEtAl2009Fig8 <- function() {
	# M:
	m <- rbind(c(0,     0,   0,   1,   0,   0  ),
		 	   c(0,     0,   0,   0,   1,   0  ),
			   c(0,     0,   0,   0,   0,   1  ),
			   c(0,   0.5, 0.5,   0,   0,   0  ),
			   c(0.5,   0, 0.5,   0,   0,   0  ),
			   c(0.5, 0.5,   0,   0,   0,   0  ))
	rownames(m) <- colnames(m) <- paste("H_{",rep(c("E","S"),each=3),1:3,"}",sep="")
	# Graph creation
	weights <- c(1/3, 1/3, 1/3, 0, 0, 0)
	graph <- new("graphMCP", m=m, weights=weights)
	# Visualization settings
	nodeX <- rep(c(100, 300, 500), 2)
	nodeY <- rep(c(100, 300), each=3)
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY	
	# Label placement
	edgeData(graph, "H11", "H21", "labelX") <- 200
	edgeData(graph, "H11", "H21", "labelY") <- 80	
	edgeData(graph, "H31", "H21", "labelX") <- 400
	edgeData(graph, "H31", "H21", "labelY") <- 80	
		
	attr(graph, "description") <- paste("Graph representing the procedure from Bretz et al. (2011) - Figure 2", 
			"",
			"H11, H21 and H31 represent three primary hypotheses and H21, H22 and H23 the associated secondary hypotheses.",
			"",			
			"A secondary hypothesis is only tested if the associated primary hypotheses is rejected.",
			"",
			"Since in this example it is preferred to reject two adjacent hypotheses (like H11 and H21 instead of H11 and H31) there are only edges between adjacent nodes.",
			"",
			"Literature: Bretz, F., Maurer, W. and Hommel, G. (2011), Test and power considerations for multiple endpoint analyses using sequentially rejective graphical procedures. Statistics in Medicine, 30: n/a.", sep="\n")
	return(graph)	
}

HommelEtAl2007 <- function() {
	# Nodes:
	weights <- c(rep(1/3, 3), rep(0,4))	
	hnodes <- c("E1", "QoL", "E2", "D1", "D2", "D3", "D4")
	# Edges:
	m <- rbind(                 
		c("0",         "1", "0",         "0",                 "0",                 "0",                 "0"                ),
		c("0",         "0", "0",         "0.25",              "0.25",              "0.25",              "0.25"             ),
		c("0",         "1", "0",         "0",                 "0",                 "0",                 "0"                ),
		c("\\epsilon", "0", "\\epsilon", "0",                 "1/3-2/3*\\epsilon", "1/3-2/3*\\epsilon", "1/3-2/3*\\epsilon"),
		c("\\epsilon", "0", "\\epsilon", "1/3-2/3*\\epsilon", "0",                 "1/3-2/3*\\epsilon", "1/3-2/3*\\epsilon"),
		c("\\epsilon", "0", "\\epsilon", "1/3-2/3*\\epsilon", "1/3-2/3*\\epsilon", "0",                 "1/3-2/3*\\epsilon"),
		c("\\epsilon", "0", "\\epsilon", "1/3-2/3*\\epsilon", "1/3-2/3*\\epsilon", "1/3-2/3*\\epsilon", "0"))
	rownames(m) <- colnames(m) <- hnodes
	# Graph creation
	graph <- new("graphMCP", m=m, weights=weights)
	# Visualization settings
	nodeX <- c(200, 400, 600, 100, 300, 500, 700)
	nodeY <- c(100, 100, 100, 300, 300, 300, 300)
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY	
	for (i in 1:4) {
		n1 <- hnodes[3+i]
		for (j in (1:4)[-i]) {
			n2 <- hnodes[3+j]
			graph <- addEdge(n1, n2, graph, 1/3)	
			x <- ((i+j)*200-200)/2+sign(i-j)*30
			y <- 300 + ((abs(i-j)-1)*60)+sign(i-j)*10+10
			edgeData(graph, n1, n2, "labelX") <- x
			edgeData(graph, n1, n2, "labelY") <- y
		}
	}
	attr(graph, "description") <- paste("Graph representing the procedure from Hommel et al. (2007)", 
			"",
			"In this clinical trial example three primary endpoints are investigated: QoL (Quality of Life score), E1 and E2.",
			"If QoL is rejected, four secondary hypotheses D1, D2, D3 and D4 are also be tested.",
			"",
			"Literature: Hommel, G., Bretz, F. und Maurer, W. (2007). Powerful short-cuts for multiple testing procedures with special reference to gatekeeping strategies. Statistics in Medicine, 26(22), 4063-4073.", sep="\n")
	attr(graph, "pvalues") <- c(0.097, 0.015, 0.005, 0.006, 0.004, 0.008, 0.04)
	return(graph)	
}

parallelGatekeeping <- function() {
	# Nodes:
	weights <- rep(c(1/2,0), each=2)	
	hnodes <- paste("H", 1:4, sep="")
	# Edges:
	m <- rbind(
		c(0, 0, 0.5, 0.5),
		c(0, 0, 0.5, 0.5),
		c(0, 0, 0.0, 1.0),
		c(0, 0, 1.0, 0.0))
	rownames(m) <- colnames(m) <- hnodes 
	# Graph creation
	graph <- new("graphMCP", m=m, weights=weights)
	# Visualization settings
	nodeX <- rep(c(100, 300), 2)
	nodeY <- rep(c(100, 300), each=2)
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY	
	# Label placement
	edgeData(graph, "H1", "H4", "labelX") <- 150
	edgeData(graph, "H1", "H4", "labelY") <- 150
	edgeData(graph, "H2", "H3", "labelX") <- 250
	edgeData(graph, "H2", "H3", "labelY") <- 150
	attr(graph, "description") <- paste("Graph representing a parallel gatekeeping procedure", 
			"",
			"Literature: Dmitrienko, A., Offen, W., Westfall, P.H. (2003). Gatekeeping strategies for clinical trials that do not require all primary effects to be significant. Statistics in Medicine. 22, 2387-2400.", sep="\n")
	return(graph)	
}

improvedParallelGatekeeping <- function() {
	graph <- parallelGatekeeping()
	graph <- addEdge("H3", "H1", graph, "\\epsilon")
	graph <- addEdge("H4", "H2", graph, "\\epsilon")
	graph <- addEdge("H3", "H4", graph, "1-\\epsilon")
	graph <- addEdge("H4", "H3", graph, "1-\\epsilon")
	attr(graph, "description") <- paste("Graph representing an improved parallel gatekeeping procedure", 
			"",
			"Literature: Bretz, F., Maurer, W., Brannath, W., Posch, M.: A graphical approach to sequentially rejective multiple test procedures. Statistics in Medicine 2009 vol. 28 issue 4 page 586-604. URL: http://www.meduniwien.ac.at/fwf_adaptive/papers/bretz_2009_22.pdf .", sep="\n")
	return(graph)	
}

fallback <- function(weights) {
	if (missing(weights)) { stop("Please provide weights.") }
	n <- length(weights)
	hnodes <- paste("H", 1:n, sep="")
	m <- matrix(0, nrow=n, ncol=n)
	for (i in 2:n) {
		m[i-1,i] <- 1
	}
	rownames(m) <- colnames(m) <- hnodes
	graph <- new("graphMCP", m=m, weights=weights)
	# Visualization settings
	nodeX <- 50+(0:(n-1))*150
	nodeY <- rep(100, n)
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY		
	attr(graph, "description") <- paste("Graph representing the fixed sequence test", 
			"",
			"Literature:  Maurer W., Hothorn L., Lehmacher W.: Multiple comparisons in drug clinical trials and preclinical assays: a-priori ordered hypotheses. In Biometrie in der chemisch-pharmazeutischen Industrie, Vollmar J (ed.). Fischer Verlag: Stuttgart, 1995; 3-18.",
			"",
			"Westfall P.H., Krishen A.: Optimally weighted, fixed sequence, and gatekeeping multiple testing procedures. Journal of Statistical Planning and Inference 2001; 99:25-40.", sep="\n")
	return(graph)
}

fixedSequence <- function(n) {
	if (missing(n)) { stop("Please provide the number of hypotheses as parameter n.") }
	weights <- c(1, rep(0, n-1))
	graph <- fallback(weights)		
	attr(graph, "description") <- paste("Graph representing the fixed sequence test", 
			"",
			"Literature:  Maurer W., Hothorn L., Lehmacher W.: Multiple comparisons in drug clinical trials and preclinical assays: a-priori ordered hypotheses. In Biometrie in der chemisch-pharmazeutischen Industrie, Vollmar J (ed.). Fischer Verlag: Stuttgart, 1995; 3-18.",
            "",
			"Westfall P.H., Krishen A.: Optimally weighted, fixed sequence, and gatekeeping multiple testing procedures. Journal of Statistical Planning and Inference 2001; 99:25-40.", sep="\n")
	return(graph)
}

simpleSuccessiveI <- function() {
	graph <- generalSuccessive()
	graph <- replaceVariables(graph, variables=list("\\\\gamma"=0, "\\\\delta"=0))
	attr(graph, "description") <- paste("General successive graph from Bretz et al. (2011), Figure 3", 
			"",
			"Literature: Bretz, F., Maurer, W. and Hommel, G. (2011), Test and power considerations for multiple endpoint analyses using sequentially rejective graphical procedures. Statistics in Medicine, 30: n/a.", sep="\n")
	return(graph)
}

simpleSuccessiveII <- function() {
	graph <- generalSuccessive()
	graph <- replaceVariables(graph, variables=list("\\\\gamma"=1/2, "\\\\delta"=1/2))
	attr(graph, "description") <- paste("General successive graph from Bretz et al. (2011), Figure 6", 
			"",
			"Literature: Bretz, F., Maurer, W. and Hommel, G. (2011), Test and power considerations for multiple endpoint analyses using sequentially rejective graphical procedures. Statistics in Medicine, 30: n/a.", sep="\n")
	return(graph)
}

truncatedHolm <- function() {
	# Nodes:
	weights <- c(1/2, 1/2, 0, 0)
	hnodes <- paste("H", 1:4, sep="")
	# Edges:
	m <- rbind(
			c("0",      "\\gamma","(1-\\gamma)/2", "(1-\\gamma)/2"),
			c("\\gamma","0",      "(1-\\gamma)/2", "(1-\\gamma)/2"),
			c("0",      "0",      "0",             "1"),       
			c("0",      "0",      "1",             "0"))     
	rownames(m) <- colnames(m) <- hnodes 
	# Graph creation
	graph <- new("graphMCP", m=m, weights=weights)
	# Visualization settings
	nodeX <- rep(c(100, 300), 2)
	nodeY <- rep(c(100, 300), each=2)
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY	
	attr(graph, "description") <- paste("Example of the Truncated Holm Procedure", 
			"",
			"Literature: Bretz, F., Maurer, W. and Hommel, G. (2011), Test and power considerations for multiple endpoint analyses using sequentially rejective graphical procedures. Statistics in Medicine, 30: n/a.", sep="\n")
	return(graph)
}

generalSuccessive <- function(weights=c(1/2,1/2)) {
	if (length(weights)!=2) stop("Please specify the weights for H1 and H2 and only these.")
	# Nodes:
	weights <- c(weights, 0, 0)
	hnodes <- paste("H", 1:4, sep="")
	# Edges:
	m <- rbind(
		c("0",      "\\gamma","1-\\gamma","0"),
		c("\\delta","0",      "0",        "1-\\delta"),
		c("0",      "1",      "0",        "0"),       
		c("1",      "0",      "0",        "0"))     
	rownames(m) <- colnames(m) <- hnodes 
	# Graph creation
	graph <- new("graphMCP", m=m, weights=weights)
	# Visualization settings
	nodeX <- rep(c(100, 300), 2)
	nodeY <- rep(c(100, 300), each=2)
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY	
	attr(graph, "description") <- paste("General successive graph from Bretz et al. (2011), Figure 6", 
			"",
			"Literature: Bretz, F., Maurer, W. and Hommel, G. (2011), Test and power considerations for multiple endpoint analyses using sequentially rejective graphical procedures. Statistics in Medicine, 30: n/a.", sep="\n")
	return(graph)		
}

HuqueAloshEtBhore2011 <- function() {
	graph <- HungEtWang2010()
	graph <- replaceVariables(graph, variables=list("\\\\nu"=1/2, "\\\\omega"=1/2, "\\\\tau"=0))
	rownames(graph@m) <- colnames(graph@m) <- paste("H", 1:4, sep="")
	graph@m["H4","H2"] <- 1
	attr(graph, "description") <- paste("Graph representing the procedure from Huque, Alosh and Bhore (2011)", 
			"",
			"Literature: Huque, M.F. and Alosh, M. and Bhore, R. (2011), Addressing Multiplicity Issues of a Composite Endpoint and Its Components in Clinical Trials. Journal of Biopharmaceutical Statistics, 21: 610-634.", sep="\n")
	return(graph)	
}


HungEtWang2010 <- function() {
	# Nodes:
	weights <- c(1,0,0,0)	
	hnodes <- c("H_{1,NI}","H_{1,S}","H_{2,NI}","H_{2,S}")
	# Edges:
	m <- rbind(  
		c("0",     "\\nu",    "1-\\nu", "0"),        
		c("0",     "0",       "\\tau",  "1-\\tau"),  
		c("0",     "\\omega", "0",      "1-\\omega"),
		c("0",     "0",       "0",      "0"))
	rownames(m) <- colnames(m) <- hnodes 
	# Graph creation
	graph <- new("graphMCP", m=m, weights=weights)
	nodeX <- rep(c(100, 300), 2)
	nodeY <- rep(c(100, 300), each=2)
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY	
	attr(graph, "description") <- paste("Graph representing the procedure from Hung and Wang (2010)",
			"",
			"H_{1,NI} : Non-inferiority of the primary endpoint",
			"H_{1,S}  : Superiority of the primary endpoint",
			"H_{2,NI} : Non-inferiority of the secondary endpoint",
			"H_{2,S}  : Superiority of the secondary endpoint",
			"",
			"Literature: Hung H.M.J., Wang S.-J. (2010). Challenges to multiple testing in clinical trials. Biometrical Journal 52, 747-756.", sep="\n")
	return(graph)
}

MaurerEtAl1995 <- function() {
	# Nodes:
	weights <- c(1,0,0,0,0)	
	hnodes <- paste("H", 1:5, sep="")
	# Edges:
	m <- rbind( 
		c(0, 1, 0, 0.0, 0.0),
		c(0, 0, 1, 0.0, 0.0),
		c(0, 0, 0, 0.5, 0.5),
		c(0, 0, 0, 0.0, 0.0),
		c(0, 0, 0, 0.0, 0.0))
	rownames(m) <- colnames(m) <- hnodes 
	# Graph creation
	graph <- new("graphMCP", m=m, weights=weights)
	# Visualization settings
	nodeX <- c(100, 200, 300, 400, 400)
	nodeY <- c(100, 100, 100, 50, 150)
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY	
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
			"",
			"(Maurer et al. apply the intersection-union principle to H1 and H2 to test sensitivity, so sensitivity is shown if and only if H1 and H2 are both rejected.)",
			"",
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

gatekeeping <- function(n, type=c("serial", "parallel", "imporved parallel"), weights=rep(1/n, n)) {
	# Nodes:
	hnodes <- paste("H", 1:(2*n), sep="")
	# Edges:
	edges <- vector("list", length=4)
	for (i in 1:n) {
		
	}
	names(edges)<-hnodes
	# Graph creation
	graph <- new("graphMCP", nodes=hnodes, edgeL=edges, weights=c(weights, rep(0, n)))
	# Visualization settings
	nodeX <- c(100, 200, 300, 400, 400)
	nodeY <- c(100, 100, 100, 50, 150)
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY
	attr(graph, "description") <- paste("Graph representing ...",
			"",
			"Literature:", sep="\n")	
	return(graph)
}

exampleGraph <- function(graph, ...) {
	switch(graph,
			Hommel=HommelEtAl2007(),
			Bretz=BretzEtAl2011(),
			ParallelGatekeeping=parallelGatekeeping(),
			ImprovedParallelGatekeeping=improvedParallelGatekeeping(),
			BonferroniHolm=BonferroniHolm(...))
}

improvedFallbackI <- function(weights=rep(1/3, 3)) {
	# Nodes:
	hnodes <- paste("H", 1:3, sep="")
	# Edges:
	m <- rbind( 
		c(0.0, 1.0,  0),
		c(0.0, 0.0,  1),
		c(0.5, 0.5,  0))
	rownames(m) <- colnames(m) <- hnodes 
	# Graph creation
	graph <- new("graphMCP", m=m, weights=weights)
	graph <- placeNodes(graph, nrow=1, ncol=3)
	attr(graph, "description") <- paste("Improved Fallback Method I by Wiens & Dmitrienko",
			"",
			"Literature: B.L. Wiens, A. Dmitrienko (2005): The fallback procedure for evaluating a single family of hypotheses. Journal of Biopharmaceutical Statistics 15:929-942.", sep="\n")
	edgeData(graph, "H3", "H1", "labelX") <- 300
	edgeData(graph, "H3", "H1", "labelY") <- 200
	edgeData(graph, "H2", "H3", "labelX") <- 400
	edgeData(graph, "H2", "H3", "labelY") <- 100
	edgeData(graph, "H3", "H2", "labelX") <- 400
	edgeData(graph, "H3", "H2", "labelY") <- 135	
	return(graph)
} 

improvedFallbackII <- function(weights=rep(1/3, 3)) {
	# Nodes:
	hnodes <- paste("H", 1:3, sep="")
	# Edges:
	m <- rbind(         
		c("0",           "1", "0"        ),
		c("1-\\epsilon", "0", "\\epsilon"),
		c("1",           "0", "0"        ))
	rownames(m) <- colnames(m) <- hnodes 
	# Graph creation
	graph <- new("graphMCP", m=m, weights=weights)
	graph <- placeNodes(graph, nrow=1, ncol=3)
	attr(graph, "description") <- paste("Improved Fallback Method II by Hommel & Bretz",
			"",
			"Literature: Bretz, F., Maurer, W. and Hommel, G. (2011), Test and power considerations for multiple endpoint analyses using sequentially rejective graphical procedures. Statistics in Medicine, 30: n/a.",
			"",
			"G. Hommel, F. Bretz (2008): Aesthetics and power considerations in multiple testing - a contradiction? Biometrical Journal 50:657-666.", sep="\n")
	edgeData(graph, "H3", "H1", "labelX") <- 300
	edgeData(graph, "H3", "H1", "labelY") <- 200
	edgeData(graph, "H1", "H2", "labelX") <- 200
	edgeData(graph, "H1", "H2", "labelY") <- 100
	edgeData(graph, "H2", "H1", "labelX") <- 200
	edgeData(graph, "H2", "H1", "labelY") <- 135	
	return(graph)
} 

joinGraphs <- function(graph1, graph2, xOffset=0, yOffset=200) {
	m1 <- graph2matrix(graph1)
	m2 <- graph2matrix(graph2)
	m <- bdiagNA(m1,m2)
	m[is.na(m)] <- 0
	nNames <- c(nodes(graph1), nodes(graph2))
	d <- duplicated(nNames)
	if(any(d)) {
		warning(paste(c("The two graphs have the following identical nodes: ", paste(nNames[d], collapse=", "), ". The nodes of the second graph will be renamed."), sep=""))
		nodes2 <- nodes(graph2)
		i <- 1
		for (x in nNames[d]) {
			while (any(nNames==paste("H",i, sep=""))) {
				i <- i + 1
			}
			nodes2[nodes2==x] <- paste("H",i, sep="")
			i <- i + 1
		}
		nNames <- c(nodes(graph1), nodes2)
	}
	rownames(m) <- nNames
	colnames(m) <- nNames
	graph <- matrix2graph(m)	
	weights <- c(getWeights(graph1), getWeights(graph2))
	if (sum(weights)>1) {
		weights <- weights / sum(weights)
	}
	graph <- setWeights(graph, weights=weights)
	nodeX <- c(getXCoordinates(graph1), getXCoordinates(graph2) + xOffset) 
	nodeY <- c(getYCoordinates(graph1), getYCoordinates(graph2) + yOffset) 
	names(nodeX) <- nNames
	names(nodeY) <- nNames
	graph@nodeData$X <- nodeX
	graph@nodeData$Y <- nodeY
	return(graph)
}