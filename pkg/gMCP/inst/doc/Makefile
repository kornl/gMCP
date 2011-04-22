
## This file is based on the Makefile from Rcpp (under GPL ≥ 2)

all: AQuickStart.pdf gMCP.pdf parametric.pdf

pdfclean:
	rm -f AQuickStart.pdf gMCP.pdf parametric.pdf

clean:
	rm -f *.tex *.bbl *.blg *.aux *.out *.log *.idx *.toc *.ind *.ilg

setvars:
ifeq (${R_HOME},)
R_HOME=	$(shell R RHOME)
endif
RPROG=	$(R_HOME)/bin/R
RSCRIPT=$(R_HOME)/bin/Rscript

AQuickStart.pdf: AQuickStart.Rnw
	$(RSCRIPT) --vanilla -e "tools::texi2dvi( 'AQuickStart.tex', pdf = TRUE, clean = TRUE )"
	rm -fr AQuickStart.tex

parametric.pdf: parametric.Rnw
	$(RSCRIPT) --vanilla -e "tools::texi2dvi( 'parametric.tex', pdf = TRUE, clean = TRUE )"
	rm -fr parametric.tex

gMCP.pdf: gMCP.Rnw
	cp -f gMCP/gMCP.Rnw gMCP.Rnw
	$(RSCRIPT) --vanilla -e "require(highlight); driver <- HighlightWeaveLatex(boxes = FALSE, bg = 'white' ); Sweave( 'gMCP.Rnw', driver = driver ); "
	$(RSCRIPT) --vanilla -e "tools::texi2dvi( 'gMCP.tex', pdf = TRUE, clean = FALSE )"
	bibtex gMCP
	makeindex -s myIndex.ist gMCP.idx
	$(RSCRIPT) -e "tools::texi2dvi( 'gMCP.tex', pdf = TRUE, clean = TRUE, index = FALSE )"
	rm -fr gMCP.tex gMCP.bbl gMCP.blg gMCP.aux gMCP.out gMCP.log
	cp -f gMCP/gMCP-fake.Rnw gMCP.Rnw
