
<!-- http://gsrmtp.R-Forge.R-project.org -->

<?php

$domain=ereg_replace('[^\.]*\.(.*)$','\1',$_SERVER['HTTP_HOST']);
$group_name=ereg_replace('([^\.]*)\..*$','\1',$_SERVER['HTTP_HOST']);
$themeroot='http://r-forge.r-project.org/themes/rforge/';

echo '<?xml version="1.0" encoding="UTF-8"?>';
?>
<!DOCTYPE html
	PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en   ">

  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><?php echo $group_name; ?></title>
	<link href="<?php echo $themeroot; ?>styles/estilo1.css" rel="stylesheet" type="text/css" />
  </head>

<body>

<!-- R-Forge Logo -->
<img src="https://r-forge.r-project.org/themes/rforge/imagesrf/logo.png" border="0" alt="R-Forge Logo" />


<!-- get project title  -->
<!-- own website starts here, the following may be changed as you like -->

<?php if ($handle=fopen('http://'.$domain.'/export/projtitl.php?group_name='.$group_name,'r')){
$contents = '';
while (!feof($handle)) {
	$contents .= fread($handle, 8192);
}
fclose($handle);
echo $contents; } ?>

<!-- end of project description -->

<h3>Feature list:</h3>
<ul>
  <li>Create graphs with drag'n'drop or directly in R.</li>
  <li>S4 objects for graphs and corresponding tests.</li>  
  <li>All steps can either be done in the R console or in the GUI.</li>
  <li>Support for epsilon edges and graphs with variables.</li>
  <li>LaTeX and PDF/PNG export of single graphs or full reports.</li>
  <li>Sequentially rejective Bonferroni, Simes and parametric tests.</li>
  <li>Adjusted p-values.</li>
  <li>Confidence intervals.</li>
  <li>Huge collection of example graphs from the literature.</li>
</ul>

You'll need <a href="http://cran.r-project.org/bin/windows/base/">R >= 2.12</a> and <a href="http://www.java.com/en/download/">Java >= 5.0</a>.
If you are a regular R user, use the normal procedure to install the <a href="http://cran.at.r-project.org/web/packages/gMCP/index.html">gMCP package</a>.
If you just want to use gMCP we provide an <a href="http://www.algorithm-forge.com/gMCP/R_with_gMCP.zip">R/gMCP bundle</a> (unzip it and click on R.exe in the "bin" directory and the GUI will show up). 
Further installation hints are <a href="http://cran.at.r-project.org/web/packages/gMCP/INSTALL">provided here</a>.

<p> You can find the <strong>project summary page</strong> <a href="http://<?php echo $domain; ?>/projects/<?php echo $group_name; ?>/"><strong>here</strong></a>. </p>

<p><img src="GUI.png" border="0" alt=""></p>

<p>If you created a graph based multiple test procedure, we would be glad to add it to our huge collection of example graphs from the literature:</p>

<p><a href="ExampleGraphsWallpaper2.jpg"><img width="500" src="ExampleGraphsWallpaper.png" border="0" alt=""></a></p>

<p>You'll find the descriptions and further examples in the package.</p>

</body>
</html>
