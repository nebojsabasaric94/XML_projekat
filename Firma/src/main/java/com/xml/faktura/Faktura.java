package com.xml.faktura;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.xml.AddaptDate;
import com.xml.firm.Firma;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Faktura")
public class Faktura {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@XmlTransient
	private Long id;

	@XmlElement(name = "Naziv_dobavljaca", required = true)
	private String nazivDobavljaca;

	@XmlElement(name = "Adresa_dobavljaca", required = true)
	private String adresaDobavljaca;

	@XmlElement(name = "PIB_dobavljaca", required = true)
	@Column(columnDefinition = "char(11)")
	private String pibDobavljaca;

	@XmlElement(name = "Naziv_kupca", required = true)
	@Column(length = 55)
	private String nazivKupca;

	@XmlElement(name = "Adresa_kupca", required = true)
	@Column(length = 55)
	private String adresaKupca;

	@XmlElement(name = "PIB_kupca", required = true)
	@Column(columnDefinition = "char(11)")
	private String pibKupca;

	@XmlElement(name = "Broj_racuna", required = true)
	@Column(columnDefinition = "char(18)")
	private String brojRacuna;

	@XmlElement(name = "Datum_racuna", required = true)
	@XmlJavaTypeAdapter(AddaptDate.class)
	@Column
	private Date datumRacuna;

	@XmlElement(name = "Vrednost_robe", required = true)
	@Column
	private Integer vrednostRobe;

	@XmlElement(name = "Vrednost_usluga", required = true)
	@Column
	private Integer vrednostUsluga;

	@XmlElement(name = "Ukupno_roba_i_usluge", required = true)
	@Column
	private Integer ukupnoRobaIUsluge;

	@XmlElement(name = "Ukupan_rabat", required = true)
	@Column
	private Integer ukupanRabat;

	@XmlElement(name = "Ukupan_porez", required = true)
	@Column
	private Integer ukupanPorez;

	@XmlElement(name = "Oznaka_valute", required = true)
	@Column(columnDefinition = "CHAR(3)")
	private String oznakaValute;

	@XmlElement(name = "Iznos_za_uplatu", required = true)
	@Column
	private Integer iznosZaUplatu;

	@XmlElement(name = "Uplata_na_racun", required = true)
	@Column(columnDefinition = "CHAR(18)")
	private String uplataNaRacun;

	@XmlElement(name = "Datum_valute", required = true)
	@Column
	@XmlJavaTypeAdapter(AddaptDate.class)
	private Date datumValute;

	@XmlTransient
	@Column
	private boolean obradjena;
	
	@XmlTransient
	@ManyToOne
	private Firma firma;
	
	//@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@XmlElement(name = "Stavka_fakture", required = true)
	private List<StavkaFakture> stavkeFakture;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNazivDobavljaca() {
		return nazivDobavljaca;
	}

	public void setNazivDobavljaca(String nazivDobavljaca) {
		this.nazivDobavljaca = nazivDobavljaca;
	}

	public String getAdresaDobavljaca() {
		return adresaDobavljaca;
	}

	public void setAdresaDobavljaca(String adresaDobavljaca) {
		this.adresaDobavljaca = adresaDobavljaca;
	}

	public String getPibDobavljaca() {
		return pibDobavljaca;
	}

	public void setPibDobavljaca(String pibDobavljaca) {
		this.pibDobavljaca = pibDobavljaca;
	}

	public String getNazivKupca() {
		return nazivKupca;
	}

	public void setNazivKupca(String nazivKupca) {
		this.nazivKupca = nazivKupca;
	}

	public String getAdresaKupca() {
		return adresaKupca;
	}

	public void setAdresaKupca(String adresaKupca) {
		this.adresaKupca = adresaKupca;
	}

	public String getPibKupca() {
		return pibKupca;
	}

	public void setPibKupca(String pibKupca) {
		this.pibKupca = pibKupca;
	}

	public String getBrojRacuna() {
		return brojRacuna;
	}

	public void setBrojRacuna(String brojRacuna) {
		this.brojRacuna = brojRacuna;
	}

	public Date getDatumRacuna() {
		return datumRacuna;
	}

	public void setDatumRacuna(Date datumRacuna) {
		this.datumRacuna = datumRacuna;
	}

	public Integer getVrednostRobe() {
		return vrednostRobe;
	}

	public void setVrednostRobe(Integer vrednostRobe) {
		this.vrednostRobe = vrednostRobe;
	}

	public Integer getVrednostUsluga() {
		return vrednostUsluga;
	}

	public void setVrednostUsluga(Integer vrednostUsluga) {
		this.vrednostUsluga = vrednostUsluga;
	}

	public Integer getUkupnoRobaIUsluge() {
		return ukupnoRobaIUsluge;
	}

	public void setUkupnoRobaIUsluge(Integer ukupnoRobaIUsluge) {
		this.ukupnoRobaIUsluge = ukupnoRobaIUsluge;
	}

	public Integer getUkupanRabat() {
		return ukupanRabat;
	}

	public void setUkupanRabat(Integer ukupanRabat) {
		this.ukupanRabat = ukupanRabat;
	}

	public Integer getUkupanPorez() {
		return ukupanPorez;
	}

	public void setUkupanPorez(Integer ukupanPorez) {
		this.ukupanPorez = ukupanPorez;
	}

	public String getOznakaValute() {
		return oznakaValute;
	}

	public void setOznakaValute(String oznakaValute) {
		this.oznakaValute = oznakaValute;
	}

	public Integer getIznosZaUplatu() {
		return iznosZaUplatu;
	}

	public void setIznosZaUplatu(Integer iznosZaUplatu) {
		this.iznosZaUplatu = iznosZaUplatu;
	}

	public String getUplataNaRacun() {
		return uplataNaRacun;
	}

	public void setUplataNaRacun(String uplataNaRacun) {
		this.uplataNaRacun = uplataNaRacun;
	}

	public Date getDatumValute() {
		return datumValute;
	}

	public void setDatumValute(Date datumValute) {
		this.datumValute = datumValute;
	}

	public Firma getFirma() {
		return firma;
	}

	public void setFirma(Firma firma) {
		this.firma = firma;
	}

	public boolean isObradjena() {
		return obradjena;
	}

	public void setObradjena(boolean obradjena) {
		this.obradjena = obradjena;
	}

	public List<StavkaFakture> getStavkeFakture() {
		return stavkeFakture;
	}

	public void setStavkeFakture(List<StavkaFakture> stavkeFakture) {
		this.stavkeFakture = stavkeFakture;
	}

}
