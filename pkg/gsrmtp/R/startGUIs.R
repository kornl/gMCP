graphGUI <- function(graph="createdGraph") {
	if (!is.character(graph)) {
		warning("Please specify the variable name for the graph as character.")
		stack <- sys.calls( )
		stack.fun <- Filter( function(.) .[[1]] == as.name("graphGUI"), stack )
		graph <- deparse( stack.fun[[1]][[2]] )
		warning(paste("We guess you wanted to use graphGUI(\"",graph,"\")",sep=""))
		# TODO Can we find out whether a call is something like
		# graphGUI(createGraphFromBretzEtAl())?
		# Because in this case we should simply stop with an error.
		# Does "exist" solve this problem? Or a confirmation during saving?
	}
	.jnew("org/mutoss/gui/CreateGraphGUI", graph)	
}