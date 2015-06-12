# gMCP GITHUB README

![GUI screenshot](https://raw.github.com/kornl/gMCP/master/www/GUI.png)

For a full list and description of features, please see the [Wiki](https://github.com/kornl/gMCP/wiki).

The development version of this package can be directly installed with the R package devtools from Hadley Wickham et al. using:

    install.packages("devtools")
    library(devtools)
    install_github("kornl/gMCP", subdir="pkg/gMCP", dependencies = TRUE, build_vignettes = TRUE)

(If some LaTeX packages are missing, consider `build_vignettes = FALSE` or install them.)

But note that the gMCP jar file is only rarely updated and it may be appropriate to build it yourself from the Java source code.

Otherwise just use the current version from [CRAN](http://cran.r-project.org/web/packages/gMCP/).
