var app = angular.module('izvod.controllers',[]);

var redniBrojPreseka = 1;

app.controller('izvodController',['$scope','izvodService','$location',
	function($scope, izvodService, $location) {
	
	$scope.show = false;

	$scope.posaljiZahtev = function(){
		var zahtev = $scope.zahtev;
		zahtev.redniBrojPreseka = redniBrojPreseka;
		
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
	
	$scope.sledeciPresek = function(){
		if($scope.presek.stavka.length > 1 ){
			redniBrojPreseka++;
			var zahtev = $scope.zahtev;
			zahtev.redniBrojPreseka = redniBrojPreseka;
			
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
	}
	
	$scope.prethodniPresek = function(){
		if(redniBrojPreseka > 1){
			redniBrojPreseka--;
			var zahtev = $scope.zahtev;
			zahtev.redniBrojPreseka = redniBrojPreseka;
			
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
	}
	
}]);