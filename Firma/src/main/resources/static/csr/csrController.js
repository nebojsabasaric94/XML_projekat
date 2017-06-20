var app = angular.module('csr.controllers',[]);
app.controller('csrController',['$scope','csrService','$location',
	function($scope,service,$location){
	$scope.issuerCommonName = {id:null,name:""};
	$scope.certificateRequest = {};	
	$scope.requests = [];
	$scope.csrData = {};
	$scope.certificateRequest  ={};
	function loadRequests(){
		service.loadRequests()
		.then(function(response){
			for(i = 0; i < response.data.length;i++){
				$scope.requests.push({id:i,number : response.data[i]})
			}			
		},
		function(response){
			
		})
	}

	$scope.loadAliases = function(){
		$scope.keyStore.commonName = $scope.issuerCommonName.name;
		service.loadAliases($scope.keyStore)
		.then(function(response){
			$scope.issuerAliases = [];
			for(i = 0; i < response.data.length;i++){
				$scope.issuerAliases.push({id:i,name : response.data[i]})
			}
			$scope.hideModal();			
		},
		function(response){
			alert("GRESKA PRI UCITAVANJU ALIASA");
		})
	}
	function loadCNs(){
		service.loadCNs()
		.then(function(response){
			$scope.issuersCNs = [];
			for(i = 0; i < response.data.length;i++){
				$scope.issuersCNs.push({id:i,name : response.data[i]})
			}			
		},
		function(response){
			alert("GRESKA");
		})
	}
	loadCNs();	
	
	$scope.loadCSRData = function(){
		service.loadCSRData($scope.selectedRequest.number)
		.then(function(response){
			$scope.csrData = response.data;
			
			$scope.certificateRequest.country = response.data.country;
			$scope.certificateRequest.organizationUnit = response.data.organizationUnit;
			$scope.certificateRequest.organization = response.data.organization;
			$scope.certificateRequest.commonName = response.data.commonName;
			$scope.certificateRequest.email = response.data.email;
		},
		function(response){
		})
	}
	loadRequests();
	
	
	$scope.createCertificate = function(){
		$scope.certificateRequest.issuerCommonName = $scope.issuerCommonName.name;
		$scope.certificateRequest.issuerAlias = $scope.issuerAlias.name;
		$scope.certificateRequest.certificateAuthority = 'true';
		$scope.certificateRequest.serialNumber = $scope.selectedRequest.number;

		service.createCertificate($scope.certificateRequest)
		.then(function(response){
			$scope.state = 'home.csr.feedback';
			$location.path('home/csr/feedback')			

		},
		function(response){
			
		})
	}	
	$scope.openModal = function(){
		
		var modal = document.getElementById('myModal');
		modal.style.display = "block";		
	}
	$scope.hideModal = function(){
		var modal = document.getElementById('myModal');
		modal.style.display = "none";		
	}
	$scope.dismissModal = function(){
		$scope.issuerCommonName = {id:null,name:""};
		var modal = document.getElementById('myModal');
		modal.style.display = "none";		
	}	
	
}])