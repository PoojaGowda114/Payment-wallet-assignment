package wallet.model;

import java.util.Date;

public class Transactions {
	private String accountId;
	private double amount;
	private String transactionType;
	private Date timeStamp;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "Transaction [accountId=" + accountId + ", amount=" + amount + ", transactionType=" + transactionType
				+ ", timeStamp=" + timeStamp + "]";
	}

}
