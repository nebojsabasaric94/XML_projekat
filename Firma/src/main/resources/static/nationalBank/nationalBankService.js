var services = angular.module('nationalBank.services',['ngResource']);

services.service('nationalBankService',['$http', function($http){
	
	/*this.findAll = function(){
		return $http.get("/nationalBank");
	}*/
	
	this.createCertificate = function(certificate){
		return $http.post("/nationalBank",certificate);
	}
	
	
}])