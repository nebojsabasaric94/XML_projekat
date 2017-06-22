package com.xml.bank;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xml.firm.Firma;
import com.xml.mt102.Mt102;
import com.xml.mt102.Mt900Mt102;
import com.xml.mt102.Mt910Mt102;
import com.xml.strukturartgsnaloga.Mt900;
import com.xml.strukturartgsnaloga.Mt910;
import com.xml.strukturartgsnaloga.StrukturaRtgsNaloga;

@Entity
public class Bank {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;

	@Column(unique = true, columnDefinition = "CHAR(3)")
	@NotBlank
	private String bankCode;

	@Column(unique = true, columnDefinition = "CHAR(10)")
	@NotBlank
	private String PIB;

	@Column(unique = true, columnDefinition = "CHAR(18)")
	@NotBlank
	private String obracunskiRacunBanke;

	@Column(unique = true, columnDefinition = "CHAR(8)")
	@NotBlank
	private String swiftKodBanke;

	private Integer stanjeRacunaBanke;

	@Column(length = 120)
	@NotBlank
	private String name;

	@Column(length = 120)
	@NotBlank
	private String address;

	@Column(length = 128)
	@Email
	private String email;

	@Column(length = 128)
	private String web;

	@Column(length = 20)
	private String phone;

	@Column(length = 20)
	private String fax;

	@JsonIgnore
	@OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
	private List<Firma> firms;

	@JsonIgnore
	@OneToMany(mappedBy = "banka", cascade = CascadeType.ALL)
	private List<Mt102> mt102;

	@JsonIgnore
	@OneToMany(mappedBy = "bankaPoverioca", cascade = CascadeType.ALL)
	private List<Mt102> mt102Poverioca;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "bankaDuznika", cascade = CascadeType.ALL)
	private List<StrukturaRtgsNaloga> strukturaDuznika;

	@JsonIgnore
	@OneToMany(mappedBy = "bankaPoverioca", cascade = CascadeType.ALL)
	private List<StrukturaRtgsNaloga> strukturaPoverioca;
	
	@JsonIgnore
	@OneToMany(mappedBy = "bankaDuznika", cascade = CascadeType.ALL)
	List<Mt900> mt900;
	
	@JsonIgnore
	@OneToMany(mappedBy = "bankaPoverioca", cascade = CascadeType.ALL)
	List<Mt910> mt910;

	@JsonIgnore
	@OneToMany(mappedBy = "bankaDuznika", cascade = CascadeType.ALL)
	List<Mt900Mt102> mt900Mt102;
	
	@JsonIgnore
	@OneToMany(mappedBy = "bankaPoverioca", cascade = CascadeType.ALL)
	List<Mt910Mt102> mt910Mt102;	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getPIB() {
		return PIB;
	}

	public void setPIB(String pIB) {
		PIB = pIB;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public List<Firma> getFirms() {
		return firms;
	}

	public void setFirms(List<Firma> firms) {
		this.firms = firms;
	}

	public String getObracunskiRacunBanke() {
		return obracunskiRacunBanke;
	}

	public void setObracunskiRacunBanke(String obracunskiRacunBanke) {
		this.obracunskiRacunBanke = obracunskiRacunBanke;
	}

	public String getSwiftKodBanke() {
		return swiftKodBanke;
	}

	public void setSwiftKodBanke(String swiftKodBanke) {
		this.swiftKodBanke = swiftKodBanke;
	}

	public Integer getStanjeRacunaBanke() {
		return stanjeRacunaBanke;
	}

	public void setStanjeRacunaBanke(Integer stanjeRacunaBanke) {
		this.stanjeRacunaBanke = stanjeRacunaBanke;
	}

	public List<Mt102> getMt102() {
		return mt102;
	}

	public void setMt102(List<Mt102> mt102) {
		this.mt102 = mt102;
	}

	public List<StrukturaRtgsNaloga> getStrukturaDuznika() {
		return strukturaDuznika;
	}

	public void setStrukturaDuznika(List<StrukturaRtgsNaloga> strukturaDuznika) {
		this.strukturaDuznika = strukturaDuznika;
	}

	public List<StrukturaRtgsNaloga> getStrukturaPoverioca() {
		return strukturaPoverioca;
	}

	public void setStrukturaPoverioca(List<StrukturaRtgsNaloga> strukturaPoverioca) {
		this.strukturaPoverioca = strukturaPoverioca;
	}

	public List<Mt900> getMt900() {
		return mt900;
	}

	public void setMt900(List<Mt900> mt900) {
		this.mt900 = mt900;
	}

	public List<Mt910> getMt910() {
		return mt910;
	}

	public void setMt910(List<Mt910> mt910) {
		this.mt910 = mt910;
	}

	public List<Mt900Mt102> getMt900Mt102() {
		return mt900Mt102;
	}

	public void setMt900Mt102(List<Mt900Mt102> mt900Mt102) {
		this.mt900Mt102 = mt900Mt102;
	}

	public List<Mt910Mt102> getMt910Mt102() {
		return mt910Mt102;
	}

	public void setMt910Mt102(List<Mt910Mt102> mt910Mt102) {
		this.mt910Mt102 = mt910Mt102;
	}

	public List<Mt102> getMt102Poverioca() {
		return mt102Poverioca;
	}

	public void setMt102Poverioca(List<Mt102> mt102Poverioca) {
		this.mt102Poverioca = mt102Poverioca;
	}
	
}
