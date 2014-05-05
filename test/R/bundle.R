library(gMCP)

if (interactive()) {
  # Test: setwd("C:/Users/Kornel/vmwindows/R-3.0.3-with-gMCP-0.8-6/bin")
  x <- strsplit(getwd(), "/")[[1]]
  i <- which(x=="bin")
  if (length(i)==0) {
    warning("Can not check for correct library path.")
  } else {
    path <- paste(c(x[1:(i[length(i)]-1)], "library"), collapse="/")
    if (! (tolower(path) %in% tolower(.libPaths()))) {
      warning("Library of bundle seems not to be in the library tree (see .libPaths). Adding it.")
      .libPaths(c(path, .libPaths()))
    } else if (tolower(path) != tolower(.libPaths()[1])) {
      warning("Library of bundle seems not to be the first in the library tree. Changing order.")
      .libPaths(c(path, .libPaths()[tolower(path) != tolower(.libPaths())]))
    }
  }
  graphGUI()
}