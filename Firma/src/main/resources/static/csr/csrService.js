var services = angular.module('csr.services',['ngResource']);
services.service('csrService',['$http',function($http){
	
	this.loadRequests = function(){
		return $http.get('client/certificates/requests');
	}
	
	this.loadCSRData = function(number){
		return $http.get('client/certificates/requests/'+number);
	}
	
	this.loadAliases = function(ksc){
		return $http.post("/certificates/aliases",ksc);
	}
	this.loadCNs = function(){
		return $http.get("/certificates/commonName");
	}	
	this.createCertificate = function(certificate){
		return $http.post("/client/certificates",certificate);
	}	
}])