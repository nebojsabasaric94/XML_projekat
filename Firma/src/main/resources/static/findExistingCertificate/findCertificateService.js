var services = angular.module('findCertificate.services',['ngResource']);

services.service('findCertificateService', ['$http', function($http){
	
	this.findCertificate = function(serialNumber){
		return $http.get('/certificates/download/'+serialNumber,{headers:{responseType: 'arraybuffer'}});
		
		/*.success(function(data, status){

	        console.log(data);

	        var blob = new Blob([data], {type: 'application/x-x509-ca-cert'});
	        saveAs(blob, serialNumber+'.cer');
	    })

	    .error(function(data, status){
	    })*/

	}
	this.checkCertificateStatus = function(serialNumber){
		return $http.get('/certificates/'+serialNumber);
	}
}])