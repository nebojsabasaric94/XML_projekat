package com.example.bankXml.BankXml.faktura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;

@Entity
public class StavkaFakture {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;
	
	@Max(value = 999)
	private Integer redniBroj;

	@Column(length = 120)
	private String nazivRobeIliUsluge;

	@Column
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
	
	@ManyToOne
	private Faktura faktura;
	

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

	public Faktura getFaktura() {
		return faktura;
	}

	public void setFaktura(Faktura faktura) {
		this.faktura = faktura;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	

}
