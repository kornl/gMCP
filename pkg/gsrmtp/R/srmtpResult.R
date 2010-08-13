setClass("srmtpResult",		
		representation(graphs="list",
				pvalues="numeric")
)

setMethod("print", "srmtpResult",
		function(x, ...) {
			# callNextMethod(x, ...)
			cat("SRMTP-Result\n")
			cat("\nP-values:\n")
			print(x@pvalues)
			cat("\nInitial graph:\n")
			print(x@graphs[[1]])
			if (length(x@graphs)==1) {
				cat("No hypotheses could be rejected.")
				return()
			}
			cat("\nFinal graph after", length(x@graphs)-1 ,"steps:\n")
			print(x@graphs[[length(x@graphs)]])
		})

setMethod("plot", "srmtpResult",
		function(x, y, ...) {
			# TODO Show visualization of graph			
		})

