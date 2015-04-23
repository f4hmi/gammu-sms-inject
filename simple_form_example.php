<?php
//error_reporting(E_WARNING);
if(isset($_POST['number']))
{
    include 'sms_inject.php';
    $mysql_res = mysql_connect('localhost','root','');
    mysql_select_db('gammu',$mysql_res);
    
    $smsd=new sms_inject($mysql_res);
    $smsd->mass_sms(mysql_escape_string($_POST['msg']),$_POST['number']);
}
?>
<!DOCTYPE html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="author" content="ikhsan" />

	<title>add field</title>
    <style>
        ul#phoneList li{
            list-style-type:none;
            margin-left:-15px;
        }
        form, li{
            font-family:sans-serif;
            font-size:12px;
        }
    </style>
    <script type="text/javascript">
		function delNumber(id){
			c=confirm("Are you sure to remove this number from the list?");
			if(c){
				phoneList=document.getElementById("phoneList");
                newList=document.getElementById(id);
                phoneList.removeChild(newList); 
                
                mForm=document.getElementById("mForm");
                hiddenInput=document.getElementById("hid_"+id);
                mForm.removeChild(hiddenInput);
                
			}
		}
		
        function addNumber(){
			newId=new Date().valueOf(); 
            mForm=document.getElementById("mForm"); 
            mNumber=document.getElementById("t_number").value; 
            if(mNumber.length==0){
                alert("Type a phone number"); //use regex for more complex validation
            }else{
				document.getElementById("t_number").value=''; 
                newNumber=document.createElement("input"); 
                newNumber.type="hidden"; 
                newNumber.id="hid_"+newId; 
                newNumber.name="number[]"; 
                newNumber.value=mNumber;
                mForm.appendChild(newNumber);
                
                phoneList=document.getElementById("phoneList"); 
                newList=document.createElement("li");
                newList.id=newId; 
                newList.innerHTML="<a href=\"#\" title=\"Remove this number?\" onclick=\"delNumber('"+newId+"')\">(x)</a> - " + mNumber;
                phoneList.appendChild(newList); 
            }
        }
    </script>
</head>

<body>
    <form action="<?php echo $_SERVER['PHP_SELF']?>" method="post" id="mForm">
        Message :<br />
		<textarea name="msg" style="width: 400px;height:100px"></textarea><br />
        Add Receiver : <br />
        <input type="text" id="t_number" style="width: 400px;" /><br />
        <input type="button" value="Add" onclick="addNumber()" /> 
		<input type="submit" value="Send" />
        <ul id="phoneList">
		</ul>
    </form>

</body>
</html>
