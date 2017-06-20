package bank.certificate;

import java.security.PublicKey;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;

public class SubjectData {

	
	private PublicKey publicKey;
	private X500Name x500name;
	private String serialNumber;
	private Date startDate;
	
	
	
	public SubjectData() {
		super();
	}
	public SubjectData(PublicKey publicKey, X500Name x500name, String serialNumber, Date startDate) {
		super();
		this.publicKey = publicKey;
		this.x500name = x500name;
		this.serialNumber = serialNumber;
		this.startDate = startDate;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	public X500Name getX500name() {
		return x500name;
	}
	public void setX500name(X500Name x500name) {
		this.x500name = x500name;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	
	
	
}
