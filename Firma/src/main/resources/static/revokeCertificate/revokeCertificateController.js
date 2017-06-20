var app = angular.module('revokeCertificate.controllers',[]);
app.controller('revokeCertificateController',['$scope','revokeCertificateService','$location',
	function($scope,service,$location){
	
	
	$scope.revokeCertificate = function(){
		service.revokeCertificate($scope.certificateSerialNumber)
		.then(function(response){
			if(response.data.povucen == true)
				alert("uspesno povucen")
		},
		function(response){
			
		})
	}
}])