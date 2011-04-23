# Testing the C interface
m <- matrix(1/3,nr=4,nc=4)
diag(m) <- 0
w <- rep(1/4,4)
p <- rep(0,4)
a <- 0.05
m
w
gMCP:::test(m, w, p, a)
m
w