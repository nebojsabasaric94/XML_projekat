var app = angular.module('user.controllers',[]);

app.controller('userController',['$scope','userService','$location',
	function($scope, userService,$location) {
		$scope.errorNewPassword = false;
		
		$scope.registerUser = function(){
			
			userService.registerUser($scope.user).then(
				function(response){
					
				}, function(response){
					alert("greska pri registraciji");
				}
			)
		}
		
		$scope.changeAndCheckPassword = function(){
			if($scope.changePassword.newPassword === $scope.changePassword.checkNewPassword){
				userService.changePassword($scope.changePassword).then(
					function(response){
						if(response.data == "changed"){
							alert("uspesno izvrsena promena lozinke")
							$scope.errorNewPassword = false;
							$location.path("/home");
						} else if(response.data == "badOldPassword") {
							$scope.errorOldPassword = true;
							$scope.errorNewPassword = false;
						} else if(response.data == "password didn't match"){
							$scope.errorNewPassword = true;
							$scope.errorOldPassword = false;
						}
					}, function(response){
						$scope.errorNewPassword = true;
						alert("greska pri izmeni lozinke");
					}
				)
			} else {
				$scope.errorOldPassword = false;
				$scope.errorNewPassword = true;
			}
		}
		
		$scope.userBadPasswordList = function(){
			userService.findNonActiveUser().then(
				function(response){
					$scope.nonActiveUsersTable = response.data;
				}, function(response){
					alert("greska");
				}
			)	
		}

		$scope.activate = function(user){
			userService.activate(user.id).then(
				function(response){
					$location.path("/home");
				}
			)
		}
}])