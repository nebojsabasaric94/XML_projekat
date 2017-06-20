var services = angular.module('user.services',['ngResource']);

services.service('userService', ['$http', function($http){
	
	this.registerUser = function(user){
		return $http.post("/registerUser", user);
	}
	
	this.changePassword = function(changePassword){
		return $http.put("/changePassword", changePassword);
	}
	
	this.logout = function(){
		return $http.get("/logout");
	}
	
	this.findNonActiveUser = function(){
		return $http.get("/userBadPassword");
	}
	
	this.activate = function(id){
		return $http.delete("/userBadPassword/" + id);
	}
}])