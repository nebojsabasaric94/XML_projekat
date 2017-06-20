var app = angular.module('signedCertificate.controllers',[]);
app.controller('signedCertificateController',['$scope','signedCertificateService','$location',
	function($scope,service,$location){
	
	$scope.createCertificate = function(){
		service.createCertificate($scope.certificateRequest)
		.then(function(response){
			$scope.state = 'home.addSignedCertificate.feedback';

			$location.path('home/addSignedCertificate/feedback')
		},
		function(response){
			
		})
	}
	

}])