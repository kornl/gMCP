#include <R.h>
#include <Rinternals.h>
#include <Rdefines.h>

SEXP cgMCP(double *oldM, double *oldW, double *p, double *a, int *n, double *m, double *w) {
	double *s = (double*) R_alloc(*n, sizeof(double));
	for(int i=0; i<*n; i++) {
		s[i] = 0;
	}
	for(int i=0; i<*n; i++) {
		w[i] = oldW[i];
		for(int j=0; j<*n; j++) {
			m[j+*n*i] = oldM[j+*n*i];
		}
	}

	while (1==1) {
		int j = -1;
		for(int i=0; i<*n; i++) {
			if (p[i]<=w[i]*a[0] && s[i]==0) {
				j = i;
			}
		}
		if (j==-1) return R_NilValue;
		s[j] = 1;

		for(int l=0; l<*n; l++) {
			if(s[l]==0) {
				w[l] = w[l] + w[j]*m[j + *n*l];
				for(int k=0; k<*n; k++) {
					if (s[k]==0) {
						if (l!=k && m[l + *n*j]*m[j + *n*l]<1) {
							m[l + *n*k] = (m[l + *n*k]+m[l + *n*j]*m[j + *n*k])/(1-m[l + *n*j]*m[j + *n*l]);
						} else {
							m[l + *n*k] = 0;
						}
					}
				}
			}
		}
		w[j] = 0;

	}
	return R_NilValue;
}
