var app = angular.module('izvod.controllers',[]);


app.controller('izvodController',['$scope','izvodService','$location',
	function($scope, izvodService, $location) {
	
	$scope.show = false;

	$scope.posaljiZahtev = function(){
		var zahtev = $scope.zahtev;
		zahtev.redniBrojPreseka = 1;
		
		izvodService.posaljiZahtev(zahtev).then(
			function(response){
				izvodService.primiPresek().then(
					function(response2){
						$scope.presek = response2.data.presek;
					})
			}
		)
		$scope.show = true;
	}
	
}]);