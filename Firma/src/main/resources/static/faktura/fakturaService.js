var services = angular.module('faktura.services',['ngResource']);
services.service('fakturaService',['$http',function($http){
	
	this.findAll = function(){
		return $http.get("/faktura");
	}
	this.findObradjene = function(){
		return $http.get("/faktura/obr");
	}
	
	this.send = function(faktura){
		return $http.post("/faktura", faktura);
	}
	
	this.findFirm = function(){
		return $http.get("/faktura/findFirm");
	}
	
	this.findAllFirms = function(){
		return $http.get("/faktura/findAllFirms");
	}
	
	this.obradi = function(faktura, hitno){
		return $http.post('/faktura/obrada/' + hitno , faktura);
	}
	
	this.prikaziHtml = function(faktura){
		return $http.post("/faktura/transformHTML", faktura);
	}
	
}])