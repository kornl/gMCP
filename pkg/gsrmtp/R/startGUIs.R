createGraphGUI <- function(createdGraph="createdGraph") {
	if (!is.character(createdGraph)) {
		stop("Please specify the variable name for the graph as character.")
	}
	.jcall("org/mutoss/gui/CreateGraphGUI", method="startGUI", createdGraph)
}