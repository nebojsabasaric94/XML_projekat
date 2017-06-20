'use strict';


angular.module('routerApp', ['ui.router',
	'nationalBank.services', 'nationalBank.controllers',
	'findCertificate.services','findCertificate.controllers', 
	'nationalBank.services', 'nationalBank.controllers',
	'caSignedCertificate.services','caSignedCertificate.controllers',
	'signedCertificate.services','signedCertificate.controllers',
	'revokeCertificate.services','revokeCertificate.controllers',
	'csr.services','csr.controllers',
	'user.services', 'user.controllers',
	'login.services','login.controllers',
	'faktura.services', 'faktura.controllers',]) 


.config(function($stateProvider,$urlRouterProvider){
	
	$urlRouterProvider.otherwise('/home');
	
	
	$stateProvider
	.state('home.nationalBank',{
		url : '/nationalBank',
		templateUrl : 'nationalBank/nationalBankPartial.html',
		controller : 'nationalBankController'
	})
	.state('home.addCertificate',{
		url : '/addCertificate',
		templateUrl : 'nationalBank/addCertificate.html',
		controller : 'nationalBankController',
		resolve : {
			addCertificate : function($http){
				return $http.get("/userPermissionAddCertificate").then(
					function(response){
						if(response.data == "true"){
							return true;
						} else {
							history.back();
						}
					}
				)
			}
		}
	})
	/*.state('home.addCertificate.feedback',{
		url : '/feedback',
		templateUrl : 'nationalBank/feedback.html'
	})*/
	.state('home.addCaSignedCertificate',{
		url : '/addCaSignedCertificate',
		templateUrl : 'caSignedCertificate/caSignedCertificate.html',
		controller : 'caSignedCertificateController',
		resolve : {
			addCaSignedCertificate : function($http){
				return $http.get("/userPermissionAddCaSignedCertificate").then(
					function(response){
						if(response.data == "true"){
							return true;
						} else {
							history.back();
						}
					}
				)
			}
		}
	})
	.state('home.addCaSignedCertificate.feedback',{
		url : '/feedback',
		templateUrl : 'caSignedCertificate/feedback.html'
	})
	.state('home.addSignedCertificate',{
		url : '/addSignedCertificate',
		templateUrl : 'signedCertificate/signedCertificate.html',
		controller : 'signedCertificateController',
		resolve : {
			registerUser : function($http){
				return $http.get("/userPermissionCreateCSR").then(
					function(response){
						if(response.data == "true"){
							return true;
						} else {
							history.back();
						}
					}
				)
			}
		}
	})
	.state('home.addSignedCertificate.feedback',{
		url : '/feedback',
		templateUrl : 'signedCertificate/feedback.html'
	})

	.state('home.findCertificate',{
		url : '/getExistingCertificate',
		templateUrl : 'findExistingCertificate/findCertificate.html',
		controller : 'findCertificateController'
	})
	.state('home.revokeCertificate',{
		url : '/revokeCertificate',
		templateUrl : 'revokeCertificate/revokeCertificate.html',
		controller : 'revokeCertificateController',
		resolve : {
			registerUser : function($http){
				return $http.get("/userPermissionRevokeCertificate").then(
					function(response){
						if(response.data == "true"){
							return true;
						} else {
							history.back();
						}
					}
				)
			}
		}		
	})

	.state('home.csr',{
		url : '/csr',
		templateUrl : 'csr/signCsr.html',
		controller : 'csrController',
		resolve : {
			registerUser : function($http){
				return $http.get("/userPermissionSignCSR").then(
					function(response){
						if(response.data == "true"){
							return true;
						} else {
							history.back();
						}
					}
				)
			}
		}
	})

	.state('home.csr.feedback',{
		url : '/feedback',
		templateUrl : 'csr/feedback.html'
	})
	.state('home.changePassword',{
		url : '/changePassword',
		templateUrl : 'user/changePassword.html',
		controller : 'userController'
	})
	.state('home.addFaktura',{
		url : '/addFaktura',
		templateUrl : 'faktura/faktura.html',
		controller : 'fakturaController',
		resolve : {
			registerUser : function($http){
				return $http.get("/userPermissionSendInvoice").then(
					function(response){
						if(response.data == "true"){
							return true;
						} else {
							history.back();
						}
					}
				)
			}
		}
	})
	.state('home.listaFaktura',{
		url : '/listaFaktura',
		templateUrl : 'faktura/faktureList.html',
		controller : 'fakturaController',
		resolve : {
			registerUser : function($http){
				return $http.get("/userPermissionSendInvoice").then(
					function(response){
						if(response.data == "true"){
							return true;
						} else {
							history.back();
						}
					}
				)
			}
		}
	})
	.state('home.register',{
		url : '/registerUser',
		templateUrl : 'user/registerUser.html',
		controller : 'userController',
		resolve : {
			registerUser : function($http){
				return $http.get("/userPermissionRegisterUser").then(
					function(response){
						if(response.data == "true"){
							return true;
						} else {
							history.back();
						}
					}
				)
			}
		}
	})
	.state('home',{
		url : '/home',
		templateUrl : 'home.html',
		controller : 'userController'
	})
	.state('home.nonActiveUsers',{
		url : '/nonActiveUsers',
		templateUrl : 'user/userBadPasswordList.html',
		controller : 'userController',
		resolve : {
			registerUser : function($http){
				return $http.get("/userPermissionActivateUser").then(
					function(response){
						if(response.data == "true"){
							return true;
						} else {
							history.back();
						}
					}
				)
			}
		}
	})

	
	
}).service('appService', ['$http', function($http){
	
	this.userDetails = function(){
		return $http.get("/userDetails");
	}
	
	
}]).controller('appController',['$scope','appService','$location',
	function($scope, appService,$location, Authentication) {
		
		$scope.authorities = [];
		userDetails();
		
		$scope.addCertificate = false;
		$scope.revokeCertificate = false;
		$scope.registerUserNav = false;
		$scope.addCaSignedCertificate = false;
		$scope.sendInvoice = false;
		$scope.createCSR = false;
		$scope.signCSR = false;
		$scope.activateUser = false;
		
		
		$scope.error = false;
		
		$scope.toggleShowDiv = function(){
		    console.log('fired');
		    $scope.role_banker = !$scope.role_banker;
		  }
		
		
		
		function userDetails(){
			appService.userDetails().then(
				function(response){
					for(var i = 0 ; i < response.data.authorities.length; i++){
						$scope.authorities.push(response.data.authorities[i].authority);
					}
					
					auth();
					console.log($scope.authorities);
					// $scope.authorities.push(response.data.authorities);
				}, function(response){
					alert("greska pri logout-u");
				}
			)
		}
		
		function auth(){
			for(var i = 0 ; i < $scope.authorities.length; i++){
				if($scope.authorities[i] == "addCertificate")
					$scope.addCertificate = true;
				if($scope.authorities[i] == "revokeCertificate")
					$scope.revokeCertificate = true;
				if($scope.authorities[i] == "registerUser")
					$scope.registerUserNav = true;
				if($scope.authorities[i] == "addCaSignedCertificate")
					$scope.addCaSignedCertificate = true;
				if($scope.authorities[i] == "sendInvoice")
					$scope.sendInvoice = true;
				if($scope.authorities[i] == "createCSR")
					$scope.createCSR = true;
				if($scope.authorities[i] == "signCSR")
					$scope.signCSR = true;
				if($scope.authorities[i] == "activateUser")
					$scope.activateUser = true;
			}
}

}]);
