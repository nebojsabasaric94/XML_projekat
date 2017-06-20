package bank.invoice;

import java.sql.Date;

import javax.persistence.Column;

public class Invoice {
	
	@Column
	private String id;
	
	@Column
	private String supplierName;
	
	@Column
	private String supplierAddress;
	
	@Column
	private String supplierPib;
	
	@Column
	private String buyerName;
	
	@Column
	private String buyerAddress;
	
	@Column
	private String buyerPib;
	
	@Column
	private int billNumber;
	
	@Column
	private Date billDate;
	
	@Column
	private float goodsValue;
	
	@Column
	private float serviceValue;
	
	@Column
	private float totalgoodsAndService;
	
	@Column
	private float totalRebate;
	 
	@Column
	private float totalTax;
	
	@Column
	private String currencyCode;
	
	@Column
	private float AmountToPay;
	
	@Column
	private String PaymentToAccount;
	
	@Column
	private Date currencyDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

	public String getSupplierPib() {
		return supplierPib;
	}

	public void setSupplierPib(String supplierPib) {
		this.supplierPib = supplierPib;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(String buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public String getBuyerPib() {
		return buyerPib;
	}

	public void setBuyerPib(String buyerPib) {
		this.buyerPib = buyerPib;
	}

	public int getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(int billNumber) {
		this.billNumber = billNumber;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public float getGoodsValue() {
		return goodsValue;
	}

	public void setGoodsValue(float goodsValue) {
		this.goodsValue = goodsValue;
	}

	public float getServiceValue() {
		return serviceValue;
	}

	public void setServiceValue(float serviceValue) {
		this.serviceValue = serviceValue;
	}

	public float getTotalgoodsAndService() {
		return totalgoodsAndService;
	}

	public void setTotalgoodsAndService(float totalgoodsAndService) {
		this.totalgoodsAndService = totalgoodsAndService;
	}

	public float getTotalRebate() {
		return totalRebate;
	}

	public void setTotalRebate(float totalRebate) {
		this.totalRebate = totalRebate;
	}

	public float getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(float totalTax) {
		this.totalTax = totalTax;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public float getAmountToPay() {
		return AmountToPay;
	}

	public void setAmountToPay(float amountToPay) {
		AmountToPay = amountToPay;
	}

	public String getPaymentToAccount() {
		return PaymentToAccount;
	}

	public void setPaymentToAccount(String paymentToAccount) {
		PaymentToAccount = paymentToAccount;
	}

	public Date getCurrencyDate() {
		return currencyDate;
	}

	public void setCurrencyDate(Date currencyDate) {
		this.currencyDate = currencyDate;
	}
	
}
