RNWFILE=$1
TEXFILE=`basename $RNWFILE .Rnw`.tex
if [ -z $R_HOME ]; then 
  R_HOME=`R RHOME`
fi
RPROG=$R_HOME/bin/R
RSCRIPT=$R_HOME/bin/Rscript

$RPROG CMD Sweave $RNWFILE
$RPROG CMD Stangle $RNWFILE
$RSCRIPT --vanilla -e "tools::texi2dvi( '$TEXFILE', pdf = TRUE, clean = TRUE )"
rm -fr $TEXFILE

