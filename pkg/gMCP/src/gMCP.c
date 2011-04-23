#include <R.h>
#include <Rinternals.h>
#include <Rdefines.h>

SEXP cgMCP(SEXP matrix, SEXP weights, SEXP pvalues, SEXP alpha) {
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
		int j = -1;
		for(int i=0; i<n; i++) {
			if (p[i]<=w[i]*a[0] && s[i]==0) {
				j = i;
			}
		}
		if (j==-1) return R_NilValue;
		s[j] = 1;
		for(int l=0; l<n; l++) {
			if(s[l]==0) {
				if (l!=j) {
					w[l] = w[l] + w[j]*m[j + n*l];
					//PrintValue(w[j]);
				} else {
					w[l] = 0;
				}
				for(int k=0; k<n; k++) {
					if (s[k]==0) {
						if (l!=k && m[l + n*j]*m[j + n*l]<1) {
							m[l + n*k] = (m[l + n*k]+m[l + n*j]*m[j + n*k])/(1-m[l + n*j]*m[j + n*l]);
						} else {
							m[l + n*k] = 0;
						}
					}
				}
			}
		}

	}
	return R_NilValue;
}
