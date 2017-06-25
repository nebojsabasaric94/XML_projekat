<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
    	<html>
    	
    		<head>
                <title>Faktura(XSLT)</title>
               <!-- <style type="text/css"></style>     PROMENI POSLE            -->                
    		</head>
    	
    		<body>
    		
	    		<table style="border: 1px">
	    			<tr>
	    				<th>Naziv_dobavljaca</th>
	    				<th>Adresa_dobavljaca</th>
	    				<th>PIB_dobavljaca</th>
	    				<th>Naziv_kupca</th>
	    				<th>Adresa_kupca</th>
	    				<th>PIB_kupca</th>
	    				<th>Broj_racuna</th>
	    				<th>Datum_racuna</th>
	    				<th>Vrednost_robe</th>
	    				<th>Vrednost_usluga</th>
	    				<th>Ukupno_roba_i_usluge</th>
	    				<th>Ukupan_rabat</th>
	    				<th>Ukupan_porez</th>
	    				<th>Oznaka_valute</th>
	    				<th>Iznos_za_uplatu</th>
	    				<th>Uplata_na_racun</th>
	    				<th>Datum_valute</th>
	    			</tr>
		    		<tr>
		    			<xsl:for-each select="Faktura">
		 				<td> <xsl:value-of select="Naziv_dobavljaca"/></td>
		 				<td> <xsl:value-of select="Adresa_dobavljaca"/></td>
		 				<td> <xsl:value-of select="PIB_dobavljaca"/></td>
		 				<td> <xsl:value-of select="Naziv_kupca"/></td>
		 				<td> <xsl:value-of select="Adresa_kupca"/></td>
		 				<td> <xsl:value-of select="PIB_kupca"/></td>
		 				<td> <xsl:value-of select="Broj_racuna"/></td>
		 				<td> <xsl:value-of select="Datum_racuna"/></td>				
		 				<td> <xsl:value-of select="Vrednost_robe"/></td>
		 				<td> <xsl:value-of select="Vrednost_usluga"/></td>
		 				<td> <xsl:value-of select="Ukupno_roba_i_usluge"/></td>
		 				<td> <xsl:value-of select="Ukupan_rabat"/></td>
		 				<td> <xsl:value-of select="Ukupan_porez"/></td>
		 				<td> <xsl:value-of select="Oznaka_valute"/></td>
		 				<td> <xsl:value-of select="Iznos_za_uplatu"/></td>
		 				<td> <xsl:value-of select="Uplata_na_racun"/></td>
		 				<td> <xsl:value-of select="Datum_valute"/></td>
		 				</xsl:for-each>
		 			</tr>
		 		</table>
		 		
		 		<table style="border: 1px">
		 			<tr>
		 				<th>Redni_broj</th>
		 				<th>Naziv_robe_ili_usluge </th>
		 				<th>Kolicina </th>
		 				<th>Jedinica_mere </th>
		 				<th>Jedinicna_cena </th>
		 				<th>Vrednost </th>
		 				<th>Procenat_rabata</th>
		 				<th>Iznos_rabata </th>
		 				<th>Umanjeno_za_rabat </th>
		 				<th>Ukupan_porez_stavka </th>
		 			</tr>
		 			<tr>
		 				<xsl:for-each select="Faktura/Stavka_fakture">
			 				<td><xsl:value-of select="Redni_broj"/></td>
			 				<td><xsl:value-of select="Naziv_robe_ili_usluge"/></td>
			 				<td><xsl:value-of select="Kolicina"/></td>
			 				<td><xsl:value-of select="Jedinica_mere"/></td>
			 				<td><xsl:value-of select="Jedinicna_cena"/></td>
			 				<td><xsl:value-of select="Vrednost"/></td>
			 				<td><xsl:value-of select="Procenat_rabata"/></td>
			 				<td><xsl:value-of select="Iznos_rabata"/></td>
			 				<td><xsl:value-of select="Umanjeno_za_rabat"/></td>
			 				<td><xsl:value-of select="Ukupan_porez_stavka"/></td>
		 				</xsl:for-each>
		 			</tr>
	    		</table>
	    		
    		</body>
    	
    	</html>
    </xsl:template>
    
</xsl:stylesheet>
