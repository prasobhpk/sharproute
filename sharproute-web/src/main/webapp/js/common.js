var dl = '_';

function addOrUpdateObject(obj, parentId){
	if ($('#'+obj.getid()).length == 0){
		addObject(obj, parentId);
	} else {
		updateObject(obj, parentId);
	}
}

function removeObject(obj, parentId){
	$('#'+obj.getid()).remove();
}

function addObject(obj, parentId){
	var div = "";
	div += '<div id="' + obj.getid() + '" class="' + obj.classname() + '">';
	for (var prop in obj){
		if (!jQuery.isFunction(obj[prop])) {
			div += '<div id="' + obj.getid() + dl + prop + '" class="' + obj.classname() + '">' + obj[prop] + '</div>';
		}
	}
	div += '</div>';
	$('#'+parentId).append(div);
}

function updateObject(obj, parentId){
	for (var prop in obj){
		if (!jQuery.isFunction(obj[prop])) {
			$('#'+obj.getid() + dl + prop).html(obj[prop]);
		}
	}
}