
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

<p> The <strong>project summary page</strong> you can find <a href="http://<?php echo $domain; ?>/projects/<?php echo $group_name; ?>/"><strong>here</strong></a>. </p>

<!-- <p><a href=""><img src="FullFeaturedGUI.png" width="50%" height="50%" border="0" alt=""></a></p> -->
<p><a href=""><img src="FullFeaturedGUI.png" border="0" alt=""></a></p>

<h3>Feature list:</h3>
<ul>
  <li>Create graphs with drag'n'drop.</li>
  <li>R representation based on the graph package.</li>
  <li>All steps can either be done in the R console or in the GUI.</li>
  <li>Support for epsilon edges.</li>
  <li>LaTeX and PDF/PNG export of single graphs or full reports.</li>
  <li>Adjusted p-values.</li>
  <li>Confidence intervals.</li>
</ul>

</body>
</html>
