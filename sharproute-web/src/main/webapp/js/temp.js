
function FixServerHandler(wsuri, fixServerDiv){
	
	var websocket = new WebSocket(wsUri); 
	
	this.init = function()
    {
		websocket.onmessage = function(evt) { 
			onMessage(evt); 
		}; 
    };
    
    this.onMessage = function()
    {
    	alert(evt);
    };
}




var wsUri = "ws://localhost:8080/web/FixServerStatus";

function init() {
	var websocket = new WebSocket(wsUri); 
	websocket.onmessage = function(evt) { 
		onMessage(evt); 
	}; 
}

function onMessage(evt){
	var fixServerDiv = document.getElementById("fixServer");
	var json = evt.data;
	var obj = JSON.parse(json); 
	var objectType = obj.objectType;
	var eventType = obj.eventType;
	var uid = obj.object.uid;
	var name = obj.object.name;
	var fixServerNode = getFixServerNode(fixServerDiv, objectType, uid, name);
	if (eventType == 'ADDED'){
		fixServerDiv.appendChild(fixServerNode);
	}
	if (eventType == 'REMOVED'){
		fixServerDiv.removeChild(fixServerNode);
	}
	if (eventType == 'UPDATED'){
		fixServerDiv.appendChild(fixServerNode);
	}
	if (eventType == 'EVICTED'){
		fixServerDiv.removeChild(fixServerNode);
	}
}

function getFixServerNode(fixServerDiv, objectType, uid, name){
	var fixServerNode = fixServerDiv.getElementById(objectType + uid);
	if (fixServerNode == null){
		fixServerNode = document.createElement("div");
		fixServerNode.id = uid;
		fixServerNode.innerHTML = name;
	}
	return fixServerNode;
}