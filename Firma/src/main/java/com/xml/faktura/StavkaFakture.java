package com.xml.faktura;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement(name = "Stavka_fakture")
@XmlAccessorType(XmlAccessType.FIELD)
public class StavkaFakture implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@XmlTransient
	private Long id;

	@XmlElement(name = "Redni_broj", required = true)
	@Max(value = 999)
	private Integer redniBroj;

	@XmlElement(name = "Naziv_robe_ili_usluge", required = true)
	@Column(length = 120)
	private String nazivRobeIliUsluge;

	@XmlElement(name = "Kolicina", required = true)
	@Column
	private Integer kolicina;

	@XmlElement(name = "Jedinica_mere", required = true)
	@Column(length = 6)
	private String jedinicaMere;

	@XmlElement(name = "Jedinicna_cena", required = true)
	@Column
	private Integer jedinicnaCena;

	@XmlElement(name = "Vrednost", required = true)
	@Column
	private Integer vrednost;

	@XmlElement(name = "Procenat_rabata", required = true)
	@Column
	private Integer procenatRabata;

	@XmlElement(name = "Iznos_rabata", required = true)
	@Column
	private Integer iznosRabata;

	@XmlElement(name = "Umanjeno_za_rabat", required = true)
	@Column
	private Integer umanjenoZaRabat;

	@XmlElement(name = "Ukupan_porez_stavka", required = true)
	@Column
	private Integer ukupanPorezStavka;
	
	/*@ManyToOne
	@XmlTransient
	private Faktura faktura;*/
	

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

	public Integer getProcenatRabata() {
		return procenatRabata;
	}

	public void setProcenatRabata(Integer procenatRabata) {
		this.procenatRabata = procenatRabata;
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

	/*public Faktura getFaktura() {
		return faktura;
	}

	public void setFaktura(Faktura faktura) {
		this.faktura = faktura;
	}*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	

}
