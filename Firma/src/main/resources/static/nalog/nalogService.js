var services = angular.module('nalog.services',['ngResource']);
services.service('nalogService',['$http',function($http){
	
	this.findAll = function(){
		return $http.get("/nalog");
	}
	
	this.izgenerisiPrikaz = function(nalog){
		return $http.post("/nalog/transformHTML", nalog);
	}
	

	
}])