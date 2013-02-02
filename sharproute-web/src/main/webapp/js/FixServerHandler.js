
function FixServerHandler(wsuri, containerDiv) {
	
	if (this instanceof FixServerHandler) {
		this.wsuri = wsuri;
		this.containerDiv = containerDiv;
		this.websocket = new WebSocket(wsuri); 
		this.websocket.onmessage = function(evt) { 
			containerDiv.html(evt.data);
		}; 
	} else {
		return new FixServerHandler(first, last);
	}
	
}