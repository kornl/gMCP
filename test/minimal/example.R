.jpackage("JGR")
# Or simply library(JGR)?
jriClasses <- system.file("jri/JRI.jar", package = "rJava", lib.loc = NULL)
.jaddClassPath(jriClasses)
