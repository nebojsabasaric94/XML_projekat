package bank.client;

public class ClientCertificate {

	private String commonName;

	private String country; //country
	
	private String organization;  //organization
	
	private String organizationUnit; //organization unit
	
	private String email;

	private String password;
	
	public ClientCertificate() {
		super();
	}

	public ClientCertificate(String commonName, String country, String organization, String organizationUnit,
			String email) {
		super();
		this.commonName = commonName;
		this.country = country;
		this.organization = organization;
		this.organizationUnit = organizationUnit;
		this.email = email;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	

	
	
}
