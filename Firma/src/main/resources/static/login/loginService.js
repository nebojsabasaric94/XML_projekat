var services = angular.module('login.services',['ngResource']);
services.service('loginService',['$http',function($http){
	
	this.getLogin = function(){
		return $http.get("/login")
	}
	
	this.login = function(credentials){
		return $http.post("/loginButton", credentials);
	}
}])