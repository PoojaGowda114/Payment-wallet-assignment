package wallet.dao;

import java.util.List;

import wallet.exception.AccountNotFoundException;
import wallet.exception.InsufficientFundsException;
import wallet.model.Transactions;
import wallet.model.Transfers;

public interface WalletDao {

	public String CreateAccount(String accountName, double accountBalance)
			throws AccountNotFoundException, InsufficientFundsException;

	public double Withdraw(double amount, String accountId) throws AccountNotFoundException, InsufficientFundsException;

	public double Deposit(double amount, String accountId) throws AccountNotFoundException;

	public List<Transactions> transactions(String accountId) throws AccountNotFoundException;

	public double transfer(String senderId, String recieverId, double amount) throws AccountNotFoundException;

	public List<Transfers> transfers(String accountId) throws AccountNotFoundException;

}
