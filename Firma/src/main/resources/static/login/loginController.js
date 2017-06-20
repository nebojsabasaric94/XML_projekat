var app = angular.module('login.controllers',[]);


app.controller('loginController',['$scope','loginService','$location',
	function($scope, loginService, $location) {

	
	$scope.error = false;
	$scope.login = function(){
		loginService.login($scope.credentials).then(
			function(response){
				$location.path("/home");
		        $scope.error = false;
			}, function(response){
				$location.path("/login");
		        $scope.error = true;
			}
		)
	}

	
}]);