test.parseEpsPolynom <- function() {	
	checkEquals(c(5, 3, 2), 
			gMCP:::parseEpsPolynom("5+3*epsilon+2*epsilon^2"))
	checkEquals(c(1), 
			gMCP:::parseEpsPolynom("1"))
	checkEquals(c(5, 3, 2), 
			gMCP:::parseEpsPolynom("2*epsilon*epsilon+5+3*epsilon"))
}