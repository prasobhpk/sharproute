function FixServer(uid, name) {
	this.uid = uid;
	this.name = name;
	this.classname = function (){
		return "FixServer";
	};
	this.getid = function (){
		return this.classname() + dl + uid;
	};
}