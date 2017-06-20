package bank.certificate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Certificate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;
	

	
	@Column(unique = true)
	private String serialNumber;

	private boolean povucen;
	
	public Certificate() {
		super();
	}

	
	public Certificate(String serialNumber, boolean povucen) {
		super();
		this.serialNumber = serialNumber;
		this.povucen = povucen;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public boolean isPovucen() {
		return povucen;
	}

	public void setPovucen(boolean povucen) {
		this.povucen = povucen;
	}



	
	
	
}
