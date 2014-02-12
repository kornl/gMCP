graphGUI <- function() {
	invisible(.jnew("CreateGraphGUI"))	
}

getInfo <- function() {
  return(J("SystemInfo")$getSystemInfo())  
}