package bank.bank;

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

import bank.firm.Firma;

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
	
	
	


}
