
## This file is based on the Makefile from Rcpp (under GPL ≥ 2)

all: AQuickStart.pdf gMCP.pdf parametric.pdf

pdfclean:
	rm -f AQuickStart.pdf gMCP.pdf parametric.pdf

clean:
	rm -f *.tex *.bbl *.blg *.aux *.out *.log

setvars:
ifeq (${R_HOME},)
R_HOME=	$(shell R RHOME)
endif
RPROG=	$(R_HOME)/bin/R
RSCRIPT=$(R_HOME)/bin/Rscript

AQuickStart.pdf: AQuickStart.Rnw
	$(RPROG) CMD Sweave AQuickStart.Rnw
	$(RSCRIPT) --vanilla -e "tools::texi2dvi( 'AQuickStart.tex', pdf = TRUE, clean = TRUE ) "
	rm -fr AQuickStart.tex

AQuickStart.pdf: parametric.Rnw
	$(RPROG) CMD Sweave parametric.Rnw
	$(RSCRIPT) --vanilla -e "tools::texi2dvi( 'parametric.tex', pdf = TRUE, clean = TRUE ) "
	rm -fr parametric.tex

gMCP.pdf: gMCP.Rnw
	$(RSCRIPT) --vanilla -e "require(highlight); driver <- HighlightWeaveLatex(boxes = TRUE, bg = 'white' ); Sweave( 'gMCP.Rnw', driver = driver ); "
	$(RSCRIPT) --vanilla -e "tools::texi2dvi( 'gMCP.tex', pdf = TRUE, clean = FALSE )"
	bibtex gMCP
	makeindex -s myIndex.ist gMCP.idx
	$(RSCRIPT) -e "tools::texi2dvi( 'gMCP.tex', pdf = TRUE, clean = TRUE )"
	rm -fr gMCP.tex gMCP.bbl gMCP.blg gMCP.aux gMCP.out gMCP.log
