import java.util.List;
import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import wallet.dao.WalletDao;
import wallet.dao.WalletDaoImpl;
import wallet.model.Transfers;
import wallet.exception.AccountNotFoundException;
import wallet.exception.InsufficientFundsException;
import wallet.model.Transactions;

public class Wallet {

	private static Scanner sc = new Scanner(System.in);
	static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	static WalletDao walletDao = context.getBean("walletDao", WalletDaoImpl.class);

	public static void main(String[] args) throws AccountNotFoundException, InsufficientFundsException {
		while (true) {
			System.out.println("1. Create Account\n");
			System.out.println("2. Deposit Amount\n");
			System.out.println("3. Withdraw Amount\n");
			System.out.println("4. All Transactions");
			System.out.println("5. Transfers");
			System.out.println("6. All Transfers");
			System.out.println("0. Exit");
			System.out.println("Choice? ");
			int choice = sc.nextInt();
			switch (choice) {
			case 1:
				CreateAccount();
				break;
			case 2:
				DepositAmount();
				break;
			case 3:
				WithdrawAmount();
				break;
			case 4:
				AllTransactions();
				break;
			case 5:
				Transfers();
				break;
			case 6:
				AllTransfers();
				break;
			case 0:
				System.exit(0);
			default:
				System.out.println("Invalid Choice\n");
			}
		}
	}

	private static void CreateAccount() throws AccountNotFoundException, InsufficientFundsException {

		System.out.println("Enter the name:");
		sc.nextLine();
		String accountName = sc.nextLine();
		System.out.println("Enter the balance:");
		double accountBalance = sc.nextDouble();

		String res = walletDao.CreateAccount(accountName, accountBalance);
		System.out.println("Account Created Successfully");
	}

	private static void DepositAmount() throws AccountNotFoundException {
		System.out.println("Enter the accountId:");
		sc.nextLine();
		String accountId = sc.nextLine();
		System.out.println("Enter the amount to be deposited:");
		double amount = sc.nextDouble();
		double accountBalance = walletDao.Deposit(amount, accountId);
		System.out.println(accountBalance + " is the current account balance.");

	}

	private static void WithdrawAmount() throws AccountNotFoundException, InsufficientFundsException {
		System.out.println("Enter the accountId");
		sc.nextLine();
		String accountId = sc.nextLine();
		System.out.println("Enter the amount to be withdrawn");
		double amount = sc.nextDouble();
		double accountBalance = walletDao.Withdraw(amount, accountId);
		System.out.println(accountBalance + " is the current account balance.");
	}

	private static void AllTransactions() throws AccountNotFoundException {
		System.out.println("Enter the accountId");
		sc.nextLine();
		String accountId = sc.nextLine();

		List<Transactions> transactions = walletDao.transactions(accountId);
		if (transactions.isEmpty()) {
			System.out.println("No Transactions are done");
		} else {
			System.out.println("Transaction History of accountId:" + accountId);
			for (Transactions transaction : transactions) {
				System.out.println(transaction);
			}
		}
	}

	private static void Transfers() throws AccountNotFoundException {
		System.out.println("Enter your accountId");
		sc.nextLine();
		String senderId = sc.nextLine();
		System.out.println("Enter the reciever accountId");

		String recieverId = sc.nextLine();
		System.out.println("Enter the amount to be transferred");
		double amount = sc.nextDouble();
		double balance = walletDao.transfer(senderId, recieverId, amount);
		System.out.println("Your Current accountBalance is:" + balance);
	}

	private static void AllTransfers() throws AccountNotFoundException {
		System.out.println("Enter the accountId");
		sc.nextLine();
		String accountId = sc.nextLine();
		List<Transfers> allTransfers;
		allTransfers = walletDao.transfers(accountId);
		if (allTransfers.isEmpty()) {
			System.out.println("No Transfers made");
		} else {
			System.out.println("Transaction History of accountId:" + accountId);
			for (Transfers transfer : allTransfers) {
				System.out.println(transfer);
			}
		}
	}

}
