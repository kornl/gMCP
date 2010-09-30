graphGUI <- function(graph="createdGraph", grid=1, debug=FALSE) {
	if (!is.character(graph)) {
		if ("graphSRMTP" %in% class(graph)) {
			newGraphName <- "createdGraph"
			i <- 2
			while(exists(newGraphName, envir=globalenv())) {
				newGraphName <- paste("createdGraph", i, sep="")
				i <- i + 1
			}
			assign(newGraphName, graph, envir=globalenv())
			graph <- newGraphName
		} else {
			warning("Please specify the variable name for the graph as character.")
			stack <- sys.calls( )
			stack.fun <- Filter( function(.) .[[1]] == as.name("graphGUI"), stack )
			graph <- make.names(deparse( stack.fun[[1]][[2]] ))
			warning(paste("We guess you wanted to use graphGUI(\"",graph,"\")",sep=""))
		}
	}
	invisible(.jnew("org/mutoss/gui/CreateGraphGUI", make.names(graph), debug, grid))	
}