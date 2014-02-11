graphGUI <- function() {
	invisible(.jnew("CreateGraphGUI"))	
}

showInfo <- function() {
  s <- .jnew("SystemInfo")
  .jcall(s, "V", "printInfo")
}