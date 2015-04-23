<?php
include_once 'sms_inject.class.php';

$res=mysql_connect('localhost','root','');
mysql_select_db('sms',$res);

$sms=new sms_inject($res);
$msg="In the cellular phone industry, mobile phones and their networks sometimes support concatenated short message service (or concatenated SMS) to overcome the limitation on the number of characters that can be sent in a single SMS text message transmission (which is usually 160).";
$sms->send_sms($msg,'085747078078','send_sms');
$sms->mass_sms($msg,array('085747079079','085225307799','085225099883'),'mass_sms');
?> 