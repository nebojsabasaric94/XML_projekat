var services = angular.module('revokeCertificate.services',['ngResource']);
services.service('revokeCertificateService',['$http',function($http){
	
	this.revokeCertificate = function(serialNumber){
		return $http.put('/certificates/'+serialNumber);
	}
}])