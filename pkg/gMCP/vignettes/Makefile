
## This file is based on the Makefile from Rcpp (under GPL ≥ 2)

all: pdf

pdf: AQuickStart.pdf gMCP.pdf parametric.pdf

pdfclean:
	rm -f AQuickStart.pdf gMCP.pdf parametric.pdf

clean:
	rm -f *.tex *.bbl *.blg *.aux *.out *.log *.idx *.toc *.ind *.ilg *.loa *.lof *.lot

AQuickStart.pdf: gMCP/AQuickStart.Rnw
	./createKnitrVignette.sh AQuickStart.Rnw

parametric.pdf: gMCP/parametric.Rnw
	./createKnitrVignette.sh parametric.Rnw

gMCP.pdf: gMCP/gMCP.Rnw
	./createKnitrVignette.sh gMCP.Rnw
