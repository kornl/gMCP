graphGUI <- function(graph="createdGraph", debug=FALSE) {
	if (!is.character(graph)) {
		warning("Please specify the variable name for the graph as character.")
		stack <- sys.calls( )
		stack.fun <- Filter( function(.) .[[1]] == as.name("graphGUI"), stack )
		graph <- make.names(deparse( stack.fun[[1]][[2]] ))
		warning(paste("We guess you wanted to use graphGUI(\"",graph,"\")",sep=""))		
	}
	.jnew("org/mutoss/gui/CreateGraphGUI", graph, debug)	
}