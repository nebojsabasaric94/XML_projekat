package com.example.bankXml.BankXml.faktura;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;

import com.example.bankXml.BankXml.firm.Firma;



@Entity
public class Faktura {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;

	private String nazivDobavljaca;

	private String adresaDobavljaca;

	@Column(columnDefinition = "CHAR(11)")
	private String pibDobavljaca;

	@Column(length = 55)
	private String nazivKupca;

	@Column(length = 55)
	private String adresaKupca;

	@Column(columnDefinition = "CHAR(11)")
	private String pibKupca;

	@Column(columnDefinition = "CHAR(18)")
	private String brojRacuna;

	@Column
	private Date datumRacuna;

	@Column
	private Integer vrednostRobe;

	@Column
	private Integer vrednostUsluga;

	@Column
	private Integer ukupnoRobaIUsluge;

	@Column
	private Integer ukupanRabat;

	@Column
	private Integer ukupanPorez;

	@Column(columnDefinition = "CHAR(3)")
	private String oznakaValute;

	@Column
	private Integer iznosZaUplatu;

	@Column(columnDefinition = "CHAR(18)")
	private String uplataNaRacun;

	@Column
	private Date datumValute;

	// stavka fakture

	@Max(value = 999)
	private Integer redniBroj;

	@Column(length = 120)
	private String nazivRobeIliUsluge;

	private Integer kolicina;

	@Column(length = 6)
	private String jedinicaMere;

	@Column
	private Integer jedinicnaCena;

	@Column
	private Integer vrednost;

	@Column
	private Integer procenaRabata;

	@Column
	private Integer iznosRabata;

	@Column
	private Integer umanjenoZaRabat;

	@Column
	private Integer ukupanPorezStavka;
	
	@Column
	private boolean obradjena;

	@ManyToOne
	private Firma firma;

	/// -----------------------

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

	public Integer getRedniBroj() {
		return redniBroj;
	}

	public void setRedniBroj(Integer redniBroj) {
		this.redniBroj = redniBroj;
	}

	public String getNazivRobeIliUsluge() {
		return nazivRobeIliUsluge;
	}

	public void setNazivRobeIliUsluge(String nazivRobeIliUsluge) {
		this.nazivRobeIliUsluge = nazivRobeIliUsluge;
	}

	public Integer getKolicina() {
		return kolicina;
	}

	public void setKolicina(Integer kolicina) {
		this.kolicina = kolicina;
	}

	public String getJedinicaMere() {
		return jedinicaMere;
	}

	public void setJedinicaMere(String jedinicaMere) {
		this.jedinicaMere = jedinicaMere;
	}

	public Integer getJedinicnaCena() {
		return jedinicnaCena;
	}

	public void setJedinicnaCena(Integer jedinicnaCena) {
		this.jedinicnaCena = jedinicnaCena;
	}

	public Integer getVrednost() {
		return vrednost;
	}

	public void setVrednost(Integer vrednost) {
		this.vrednost = vrednost;
	}

	public Integer getProcenaRabata() {
		return procenaRabata;
	}

	public void setProcenaRabata(Integer procenaRabata) {
		this.procenaRabata = procenaRabata;
	}

	public Integer getIznosRabata() {
		return iznosRabata;
	}

	public void setIznosRabata(Integer iznosRabata) {
		this.iznosRabata = iznosRabata;
	}

	public Integer getUmanjenoZaRabat() {
		return umanjenoZaRabat;
	}

	public void setUmanjenoZaRabat(Integer umanjenoZaRabat) {
		this.umanjenoZaRabat = umanjenoZaRabat;
	}

	public Integer getUkupanPorezStavka() {
		return ukupanPorezStavka;
	}

	public void setUkupanPorezStavka(Integer ukupanPorezStavka) {
		this.ukupanPorezStavka = ukupanPorezStavka;
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



}
