package bank.faktura;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import bank.firm.Firma;

@Entity
public class Faktura {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;

	private String nazivDobavljaca;

	private String adresaDobavljaca;

	@Column(columnDefinition = "char(11)")
	private String pibDobavljaca;

	@Column(length = 55)
	private String nazivKupca;

	@Column(length = 55)
	private String adresaKupca;

	@Column(columnDefinition = "char(11)")
	private String pibKupca;

	@Column(columnDefinition = "char(18)")
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

	@Column
	private boolean obradjena;

	@ManyToOne
	private Firma firma;
	
	@JsonIgnore
	@OneToMany(mappedBy = "faktura", cascade = CascadeType.ALL) 
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
