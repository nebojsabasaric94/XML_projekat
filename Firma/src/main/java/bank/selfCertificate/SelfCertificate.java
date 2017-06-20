package bank.selfCertificate;

import java.io.Serializable;
import java.util.Date;


public class SelfCertificate implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	public SelfCertificate(Long id, String alias, Date startDate, String serialNumber, String password) {
		super();
		this.id = id;
		this.alias = alias;
		this.startDate = startDate;
		this.serialNumber = serialNumber;
		this.password = password;
	}
	
	public SelfCertificate() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String commonName;

	private String country; //country
	
	private String organization;  //organization
	
	private String organizationUnit; //organization unit
	
	private String email;

	private Long id;

	private String alias;

	private Date startDate;
	
	
	private String serialNumber;

	private String password;
	
	private String ksPassword;

	public Date getStartDate() {
		return startDate;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getKsPassword() {
		return ksPassword;
	}

	public void setKsPassword(String ksPassword) {
		this.ksPassword = ksPassword;
	}

	
	
	

	
	
	
	
	
	
}
