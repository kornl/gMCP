test.parseEpsPolynom <- function() {	
	checkEquals(c(5, 3, 2), 
			parseEpsPolynom("5+3*e+2*e^2"))
	checkEquals(c(1), 
			parseEpsPolynom("1"))
	checkEquals(c(5, 3, 2), 
			parseEpsPolynom("2*e*e+5+3*e"))
}