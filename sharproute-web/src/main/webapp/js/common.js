function createWsUri(suffix){
	if (window.location.protocol == 'http:') {
        return 'ws://' + window.location.host + suffix;
    } else {
        return 'wss://' + window.location.host + suffix;
    }
}