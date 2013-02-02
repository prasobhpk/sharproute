<!DOCTYPE html>
<html>
<head>
	<title>sharproute dashboard</title>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
	<script src="js/common.js"></script>
	<script src="js/FixServerHandler.js"></script>
	
	<script type="text/javascript"> 
	
		function init(){
			var wsUri = "ws://localhost:8080/web/FixServerStatus"; 
			new FixServerHandler(wsUri, $("#fixServers"));
		}
		
		window.addEventListener("load", init, false);
		 
	</script>
</head>

<body>
	<div id="header">Sharproute Dashboard Header</div>
	<div id="content">
		<div id="fixServers"></div>
		<div id="fixSessions">No Fix Sessions</div>
	</div>
	<div id="footer">Sharproute Dashboard Footer</div>
</body>

</html>