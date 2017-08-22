<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 16/08/2016
 * Time: 16:28
 */
setcookie('secretkey', $_GET['secretkey']);
header('Location: ./');
?>