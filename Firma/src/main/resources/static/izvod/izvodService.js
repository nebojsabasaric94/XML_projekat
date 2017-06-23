var services = angular.module('izvod.services',['ngResource']);
services.service('izvodService',['$http',function($http){
	
	
	this.posaljiZahtev = function(zahtev){
		return $http.post("/izvod", zahtev);
		
	}
	
}])