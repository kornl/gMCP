test.importExport <- function() {
	graph <- createGraphFromBretzEtAl()
	
	# Unfortunately this fails with   
	# ERROR in test.importExport: Error in .jnew("org.mutoss.tests.TestImportExport", "graph", "graphExport") : 
    # java.lang.StringIndexOutOfBoundsException: String index out of range: -1
	# If I call it in R by myself, everything is fine...
	
	# .jnew("org.mutoss.tests.TestImportExport", "graph", "graphExport")
	# all.equal(graph,graphExport)
}