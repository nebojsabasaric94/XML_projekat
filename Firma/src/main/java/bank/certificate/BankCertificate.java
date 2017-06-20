package bank.certificate;

import java.util.Date;

public class BankCertificate {

	

	private Long id;
	
	private String commonName;

	private String country; //country
	
	private String organization;  //organization
	
	private String organizationUnit; //organization unit
	
	private String email;	
	
	private boolean certificateAuthority;
	
	private String issuerCommonName;
	
	private String issuerAlias;
	
	private String issuerPassword;
	
	private Date startDate;
	
	
	private String serialNumber;
	
	private String alias;
	
	private String password;
	
	private String ksPassword;
	/*@Column
	private X500Name x500Name;*/

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getIssuerAlias() {
		return issuerAlias;
	}

	public void setIssuerAlias(String issuerAlias) {
		this.issuerAlias = issuerAlias;
	}

	public String getIssuerCommonName() {
		return issuerCommonName;
	}

	public void setIssuerCommonName(String issuerCommonName) {
		this.issuerCommonName = issuerCommonName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationUnit() {
		return organizationUnit;
	}

	public void setOrganizationUnit(String organizationUnit) {
		this.organizationUnit = organizationUnit;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isCertificateAuthority() {
		return certificateAuthority;
	}

	public void setCertificateAuthority(boolean certificateAuthority) {
		this.certificateAuthority = certificateAuthority;
	}

	public String getIssuerPassword() {
		return issuerPassword;
	}

	public void setIssuerPassword(String issuerPassword) {
		this.issuerPassword = issuerPassword;
	}

	public String getKsPassword() {
		return ksPassword;
	}

	public void setKsPassword(String ksPassword) {
		this.ksPassword = ksPassword;
	}

	

	
	
		
}
