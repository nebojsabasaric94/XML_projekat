var app = angular.module('findCertificate.controllers',[]);

app.controller('findCertificateController',['$scope','findCertificateService','$location',
	function($scope, findCertificateService,$location) {
		$scope.status = -1;
		
		
		$scope.findCertificate = function(){
			findCertificateService.findCertificate($scope.findSerialNumber)
			.then(function(response){
		        var blob = new Blob([response.data], {type: 'application/x-x509-ca-cert'});
		        saveAs(blob,$scope.findSerialNumber+'.cer');
		        $scope.findSerialNumber = "";
			},
			function(response){
				alert("NOT FOUND");
			})
		}
		
		
		$scope.checkCertificateStatus = function(){
			findCertificateService.checkCertificateStatus($scope.certificateSerialNumber)
			.then(function(response){
				if(response.data != ""){
					if(response.data.povucen == true){
						$scope.status = 0;
					}
					else
						$scope.status = 1;
					} else {
						$scope.status = 2;
					}
				},
				function(response){
					alert("NOT FOUND")
				})
		}

}])