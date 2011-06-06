#include <R.h>
#include <Rinternals.h>
#include <Rdefines.h>

SEXP cgMCP(double *oldM, double *oldW, double *p, double *a, int *n, double *s, double *m, double *w) {

	/* s is a vector initialized with 0 - later node i is rejected <=> s[i]==1 */
	// double *s = (double*) R_alloc(*n, sizeof(double));
	for(int i=0; i<*n; i++) {
		s[i] = 0;
	}

	/* Copying oldM to m and oldW to w, since we do not want to change the parameters in R */
	for(int i=0; i<*n; i++) {
		w[i] = oldW[i];
		for(int j=0; j<*n; j++) {
			m[j+*n*i] = oldM[j+*n*i];
		}
	}

	while (1==1) {

		/* Searching for a node that can be rejected, e.g. p[i]<=w[i]*a[0] */
		int j = -1;
		for(int i=0; i<*n; i++) {
			if (p[i]<=w[i]*a[0] && s[i]==0) {
				j = i;
			}
		}

		/* If there is no node that can be rejected, return: */
		if (j==-1) return R_NilValue;

		/* Otherwise reject it: */
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
		/* Remove all edges from and to node j*/
		for(int l=0; l<*n; l++) {
			m[l+*n*j] = 0;
			m[j+*n*l] = 0;
		}
		w[j] = 0;
	}

	/* We do not return anything, but the last two parameters m and w have been changed (and only these two).
	 * And actually this line is never reached...
	 */
	return R_NilValue;
}
