graphGUI <- function(graph="createdGraph") {
	if (!is.character(graph)) {
		stop("Please specify the variable name for the graph as character.")
	}
	.jcall("org/mutoss/gui/CreateGraphGUI", method="startGUI", graph)
}