var app = angular.module('nalog.controllers',[]);


app.controller('nalogController',['$scope','nalogService','$location','$window',
	function($scope, nalogService, $location, $window) {
	
	
	$scope.findAll = function(){
		nalogService.findAll().then(
				function(response){
					$scope.nalozi = response.data;
				}, function(response){
					alert("greska");
				}
			)
		
		
	}
	$scope.izgenerisiPrikaz = function(nalog){
		nalogService.izgenerisiPrikaz(nalog).then(
			function(response){
				alert("done");
			}
				
		)
	}
	
}]);