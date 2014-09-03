if (!interactive()) {
  stop("This should should be run interactive. It collects helpful information for troubleshooting.")
}

path <- Sys.getenv("PATH")

result <- paste("PATH:", path, sep="\n")

cat(result)