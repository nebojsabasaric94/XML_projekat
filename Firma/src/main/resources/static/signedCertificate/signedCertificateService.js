var services = angular.module('signedCertificate.services',['ngResource']);

services.service('signedCertificateService',['$http',function($http){

	this.createCertificate = function(certificate){
		return $http.post("/client/certificates/csr",certificate);
	}	
}])