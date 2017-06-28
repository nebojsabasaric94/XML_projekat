<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/">
    	<html>
    	
    		<head>
                <title>Nalog(XSLT)</title>
                <style type="text/css"> </style>        
    		</head>
    	
    		<body>
    		
	    		<table style="border: 1px">
	    			<tr>
	    				<td>Naziv_dobavljaca</td>
	    				<td>Adresa_dobavljaca</td>
	    				<td>PIB_dobavljaca</td>
	    				<td>Naziv_kupca</td>
	    				<td>Adresa_kupca</td>
	    				<td>PIB_kupca</td>
	    				<td>Broj_racuna</td>
	    				<td>Datum_racuna</td>
	    				<td>Vrednost_robe</td>
	    				<td>Vrednost_usluga</td>
	    				<td>Ukupno_roba_i_usluge</td>
	    				<td>Ukupan_rabat</td>
	    				
	    			</tr>
		    		<tr>
		    			<xsl:for-each select="NalogZaPlacanje">
		 				<td> <xsl:value-of select="idPoruke"/> </td>
		 				<td> <xsl:value-of select="duznik-nalogodavac"/> </td>
		 				<td> <xsl:value-of select="svrhaPlacanja"/> </td>
		 				<td> <xsl:value-of select="primalac-poverilac"/> </td>
		 				<td> <xsl:value-of select="racunDuznika"/> </td>
		 				<td> <xsl:value-of select="modelZaduzenja"/> </td>
		 				<td> <xsl:value-of select="pozivNaBrojZaduzenja"/> </td>
		 				<td> <xsl:value-of select="racunPoverioca"/> </td>				
		 				<td> <xsl:value-of select="modelOdobrenja"/> </td>
		 				<td> <xsl:value-of select="pozivNaBrojOdobrenja"/> </td>
		 				<td> <xsl:value-of select="iznos"/> </td>
		 				</xsl:for-each>
		 			</tr>
		 		</table>
		 	 
		 		
    		</body>
    	
    	</html>
    </xsl:template>
    
</xsl:stylesheet>
