package bank.invoice;


import javax.persistence.Column;

public class InvoiceItem {
	
	@Column
	private int serialNumber;
	
	@Column
	private String goodsOrServiceName;
	
	@Column
	private float amount;
	
	@Column
	private String unitOfMeasure;
	
	@Column
	private float unitPrice;
	
	@Column
	private float value;
	
	@Column
	private float rebatePrecentage;
	
	@Column
	private float rabateAmount;
	
	@Column
	private float rabateReduced;
	
	@Column
	private float totalTax;

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getGoodsOrServiceName() {
		return goodsOrServiceName;
	}

	public void setGoodsOrServiceName(String goodsOrServiceName) {
		this.goodsOrServiceName = goodsOrServiceName;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public float getRebatePrecentage() {
		return rebatePrecentage;
	}

	public void setRebatePrecentage(float rebatePrecentage) {
		this.rebatePrecentage = rebatePrecentage;
	}

	public float getRabateAmount() {
		return rabateAmount;
	}

	public void setRabateAmount(float rabateAmount) {
		this.rabateAmount = rabateAmount;
	}

	public float getRabateReduced() {
		return rabateReduced;
	}

	public void setRabateReduced(float rabateReduced) {
		this.rabateReduced = rabateReduced;
	}

	public float getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(float totalTax) {
		this.totalTax = totalTax;
	}
	
	
	
}
