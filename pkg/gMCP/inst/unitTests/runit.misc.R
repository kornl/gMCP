test.parseEpsPolynom <- function() {	
	checkEquals(c(5, 3, 2), 
			gMCP:::parseEpsPolynom("5+3*epsilon+2*epsilon^2"))
	checkEquals(c(1), 
			gMCP:::parseEpsPolynom("1"))
	checkEquals(c(5, 3, 2), 
			gMCP:::parseEpsPolynom("2*epsilon*epsilon+5+3*epsilon"))
}

test.matrix2graph <- function() {
	m<-rbind(H1=c("0",         "0",         "0.5",         "0.5"),
			H2=c("0",         "0",         "0.5",         "0.5"),
			H3=c("\\epsilon", "0",         "0",           "1-\\epsilon"),
			H4=c("0",         "\\epsilon", "1-\\epsilon", "0"))
	colnames(m) <- rownames(m)
	m2 <- graph2matrix(matrix2graph(m))
	checkEquals(m, m2)
}