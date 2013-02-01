<!DOCTYPE html>
<meta charset="utf-8" />
<title>WebSocket Test</title>
<script type="text/javascript">  
	
	var wsUri = "ws://localhost:8080/web/FixServerStatus"; 
	var output;  
	
	function init() { 
		output = document.getElementById("output"); 
		testWebSocket(); 
	}  
	
	function testWebSocket() { 
		websocket = new WebSocket(wsUri); 
		websocket.onopen = function(evt) { 
			onOpen(evt) 
		}; 
		websocket.onclose = function(evt) { 
			onClose(evt) 
		}; 
		websocket.onmessage = function(evt) { 
			onMessage(evt) 
		}; 
		websocket.onerror = function(evt) { 
			onError(evt) 
		}; 
	}  
	function onOpen(evt) { 
		writeToScreen("CONNECTED");  
	}  
	function onClose(evt) { 
		writeToScreen("DISCONNECTED"); 
	}  
	function onMessage(evt) { 
		writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data +'</span>'); 
	}  
	function onError(evt) { 
		writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data); 
		websocket.close(); 
	}  
	function doSend(message) { 
		writeToScreen("SENT: " + message);  
		websocket.send(message); 
	}  
	function writeToScreen(message) { 
		output.innerHTML = message;
	}  
	window.addEventListener("load", init, false); 
</script>
<h2>Fix Servers</h2>
<div id="output"></div>
<a href="dashboard.jsp">Dashboard</a>
</html>