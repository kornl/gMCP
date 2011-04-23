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

SEXP pr(SEXP matrix, SEXP weights, SEXP pvalues, SEXP alpha) {
	double *m = REAL(matrix);
	double *w = REAL(weights);
	double *a = REAL(alpha);
	double *p = REAL(pvalues);

	SEXP dim = getAttrib(matrix, R_DimSymbol);
	int n = INTEGER(dim)[0];

	double *s = (double*) R_alloc(n, sizeof(double));
	for(int i=0; i<n; i++) {
		s[i] = 0;
	}

	while (1==1) {
		int i = -1;
		for(int j=0; j<n; j++) {
			if (p[j]<=w[j]*a[0] && s[j]==0) {
				i = j;
			}
		}
		if (i==-1) return R_NilValue;
		s[i] = 1;
		for(int j=0; j<n; j++) {
			if (i!=j) {
				w[j] = w[j] + w[i]*m[i + n*j];
				//PrintValue(w[j]);
			} else {
				w[j] = 0;
			}
		}

	}
	return R_NilValue;
}

/*
int size = 5000;
double *matrix = (double *) R_alloc(size*size, sizeof(double));
*/
