var app = angular.module('izvod.controllers',[]);


app.controller('izvodController',['$scope','izvodService','$location',
	function($scope, izvodService, $location) {

	$scope.posaljiZahtev = function(){
		var zahtev = $scope.zahtev;
		zahtev.redniBrojPreseka = 1;
		
		
		izvodService.posaljiZahtev(zahtev).then(
			function(response){
				alert("Zahtev poslat");
			}
		)
	}
	
}]);