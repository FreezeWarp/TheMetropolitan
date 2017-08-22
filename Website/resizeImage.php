<?php
/* Super basic image resize program. Takes in PNG or JPEG, outputs as JPEG. Caches resizes to disk. */

require('global.php');

// File and new size
$newwidth = (isset($_GET['width']) && $_GET['width'] <= 1000 ? $_GET['width'] : 600);

$filename = $_GET['file'];
$resizedFilename = $filename . ',width=' . $newwidth;

if (isset($_GET['noresize'])) {
  header('Content-Type: ' . mime_content_type($filename));
  echo file_get_contents($filename);
}
else if (file_exists($resizedFilename)
    && !(isAdmin() && isset($_GET['refresh']))) {
  header('Content-Type: image/jpeg');
  echo file_get_contents($resizedFilename);
}
else {
  // Get new sizes
  if ($newwidth) {
    list($width, $height) = getimagesize($filename);
    $newheight = $height * ($newwidth / $width);

    // Load
    $thumb = imagecreatetruecolor($newwidth, $newheight);
  }

  switch (exif_imagetype($filename)) {
    case IMAGETYPE_JPEG:
      $source = imagecreatefromjpeg($filename);
      break;
    case IMAGETYPE_PNG:
      $source = imagecreatefrompng($filename);
      break;
    default:
      die('Invalid file.');
      break;
  }

  if ($newwidth) {
    // Resize
    imagecopyresampled($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);
  }
  else {
    $thumb = $source;
  }

  // Output
  header('Content-Type: image/jpeg');
  imagejpeg($thumb, $resizedFilename, 90);
  imagejpeg($thumb);
  imagedestroy($thumb);
}