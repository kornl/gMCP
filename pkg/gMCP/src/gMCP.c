#include <R.h>
#include <Rinternals.h>
#include <Rdefines.h>

void convolve(double *a, int *na, double *b, int *nb, double *ab)
{
	int i, j, nab = *na + *nb - 1;
	for(i = 0; i < nab; i++)
		ab[i] = 0.0;
	for(i = 0; i < *na; i++)
		for(j = 0; j < *nb; j++)
			ab[i + j] += a[i] * b[j];
}

SEXP pr(SEXP x) {
	SEXP dim = getAttrib(x, R_DimSymbol);
	double *m = REAL(x);
	int nrow = INTEGER(dim)[0];
	int ncol = INTEGER(dim)[1];

	double sum;
	for(int i=0; i<nrow; i++){
		for(int j=0; j<ncol; j++){
			sum = sum + m[i + nrow*j];
		}
	};
	return R_NilValue ;
}

/*
int size = 5000;
double *matrix = (double *) R_alloc(size*size, sizeof(double));
*/
