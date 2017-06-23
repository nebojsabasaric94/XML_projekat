var app = angular.module('faktura.controllers',[]);


app.controller('fakturaController',['$scope','fakturaService','$location',
	function($scope, fakturaService, $location) {

	$scope.faktura = new Object();
	$scope.faktura.stavkeFakture = [];
	$scope.stavka = new Object();
	$scope.faktura.iznosZaUplatu = 0;
	$scope.regex = /^\w{3}$/;
	
	$scope.dodajStavkuDiv = false;
	$scope.findFirm = function(){
		fakturaService.findFirm().then(
			function(response){
				$scope.faktura.nazivDobavljaca = response.data.name;
				$scope.faktura.adresaDobavljaca = response.data.address;
				$scope.faktura.pibDobavljaca = response.data.pibFirm;
				
				
			}
		)
	}
	
	$scope.dodajStavkuFakture = function(stavka){
		$scope.faktura.stavkeFakture.push(stavka);
		$scope.faktura.iznosZaUplatu += stavka.vrednost;
		$scope.dodajStavkuDiv = false;
	}
	
	$scope.promeniVrednost = function(stavka){
		if(stavka.jedinicnaCena != undefined && stavka.kolicina != undefined)
			$scope.stavka.vrednost = stavka.jedinicnaCena * stavka.kolicina;
	}
	
	$scope.dodajStavku = function(){
		$scope.stavka = new Object();
		$scope.dodajStavkuDiv = true;
	}
	
	
	$scope.findAll = function(){
		fakturaService.findAll().then(
				function(response){
					$scope.fakture = response.data;
				}, function(response){
					alert("greska");
				}
			)
		
		
	}
	
	$scope.sendFaktura = function(){
		fakturaService.send($scope.faktura).then(
			function(response){
				if(response.data == "true"){
					alert("uspesno dodata faktura");
					$location.path("/home");
				} else {
					alert("greska pri dodavanju fakture, faktura nije validna");
				}
				
			}, function(response){
				alert("greska");
			}
		)
	}
	
	$scope.findAllFirms = function(){
		fakturaService.findAllFirms().then(
			function(response){
				$scope.firms = response.data;
			}
		)
	}
	
	$scope.openModal = function(){
		var modal = document.getElementById('myModal');
		modal.style.display = "block";		
	}
	
	$scope.okModal = function(){
		var modal = document.getElementById('myModal');
		$scope.faktura.nazivKupca = $scope.selectedFirm.name;
		$scope.faktura.pibKupca = $scope.selectedFirm.pibFirm;
		$scope.faktura.adresaKupca = $scope.selectedFirm.address;
		$scope.faktura.uplataNaRacun = $scope.selectedFirm.brojRacuna;
		
		modal.style.display = "none";		
	
	}
	
	$scope.cancelModal = function(){
		var modal = document.getElementById('myModal');
		modal.style.display = "none";
	}
	
	$scope.selectedFirm = null;
	$scope.setSelectedFirm = function(firm){
		$scope.selectedFirm = firm;
		
	}
	
	$scope.obradi = function(faktura, hitno){
		if(hitno == undefined)
			hitno = false;
		fakturaService.obradi(faktura, hitno)
		.then(function(response){
			$scope.findAll();
		},
		function(response){
			
		})
	}
}]);