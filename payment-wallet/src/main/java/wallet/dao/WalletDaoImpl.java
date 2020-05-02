package wallet.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import wallet.exception.AccountNotFoundException;
import wallet.exception.InsufficientFundsException;
import wallet.model.Account;
import wallet.model.Transactions;
import wallet.model.Transfers;

public class WalletDaoImpl implements WalletDao {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	// public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	// this.jdbcTemplate = jdbcTemplate;
	// }

	public String CreateAccount(String accountName, double accountBalance)
			throws AccountNotFoundException, InsufficientFundsException {
		String sql1 = "select max(accountId) from account";
		List<String> list;
		list = jdbcTemplate.query(sql1, new ResultSetExtractor<List<String>>() {
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					String maxAccountId = rs.getString(1);
					maxAccountId = (maxAccountId != null) ? maxAccountId.substring(2) : "0";
					int accountId1 = 1 + Integer.parseInt(maxAccountId);
					String accountId = "WW" + String.format("%09d", accountId1);
					list.add(accountId);
				}
				return list;
			}
		});
		String accountId = list.get(0);
		String sql = "insert into account values(?,?,?)";
		jdbcTemplate.update(sql, accountName, accountId, accountBalance);
		return accountId;
	}

	public double Withdraw(double amount, String accountId)
			throws AccountNotFoundException, InsufficientFundsException {
		double current = 0;
		String sql = "select * from account where accountId=?";
		Account res = jdbcTemplate.queryForObject(sql, new Object[] { accountId },
				new BeanPropertyRowMapper(Account.class));
		if (res.getAccountBalance() > amount) {
			current = res.getAccountBalance() - amount;
		}
		String sql1 = "update account set accountBalance= ? where accountId= ?";
		jdbcTemplate.update(sql1, current, accountId);
		String transactionType = "wd";
		java.sql.Timestamp sqlTimeStamp = new java.sql.Timestamp(new java.util.Date().getTime());
		jdbcTemplate.update("insert into transactions values (?,?,?,?)", accountId, amount, transactionType,
				sqlTimeStamp);
		return current;
	}

	public double Deposit(double amount, String accountId) throws AccountNotFoundException {
		double current = 0;
		String sql = "select	* from account where accountId=?";
		Account res = jdbcTemplate.queryForObject(sql, new Object[] { accountId },
				new BeanPropertyRowMapper(Account.class));
		current = res.getAccountBalance() + amount;
		String sql1 = "update account set accountBalance= ? where accountId= ?";
		jdbcTemplate.update(sql1, current, accountId);
		String transactionType = "dp";

		java.sql.Timestamp sqlTimeStamp = new java.sql.Timestamp(new java.util.Date().getTime());
		jdbcTemplate.update("insert into transactions values (?,?,?,?)", accountId, amount, transactionType,
				sqlTimeStamp);
		return current;
	}

	public List<Transactions> transactions(String accountId) throws AccountNotFoundException {
		String sql = "Select * from transactions where accountId='" + accountId + "'";
		List<Transactions> transactions = jdbcTemplate.query(sql, new RowMapper<Transactions>() {

			public Transactions mapRow(ResultSet rs, int rn) throws SQLException {
				Transactions trans = new Transactions();
				trans.setAccountId(rs.getString(1));
				trans.setAmount(rs.getDouble(2));
				trans.setTransactionType(rs.getString(3));
				trans.setTimeStamp(rs.getTimestamp(4));
				return trans;
			}

		});
		return transactions;
	}

	public double transfer(String senderId, String recieverId, double amount) throws AccountNotFoundException {
		String sql = "select * from account where accountId=?";
		Account sender = jdbcTemplate.queryForObject(sql, new Object[] { senderId },
				new BeanPropertyRowMapper(Account.class));
		Account reciever = jdbcTemplate.queryForObject(sql, new Object[] { recieverId },
				new BeanPropertyRowMapper(Account.class));
		if ((sender.getAccountBalance() - amount) < 0) {
			System.out.println("Cannot transact");
			System.exit(0);
		}
		double senderCurrent = sender.getAccountBalance() - amount;
		double recieverCurrent = reciever.getAccountBalance() + amount;
		String sql2 = "Update account set accountBalance=? where accountId=?";
		jdbcTemplate.update(sql2, new Object[] { senderCurrent, senderId });
		jdbcTemplate.update(sql2, new Object[] { recieverCurrent, recieverId });

		jdbcTemplate.update("insert into transfers values(?,?,?,?)",
				new Object[] { senderId, recieverId, amount, new java.sql.Timestamp(new java.util.Date().getTime()) });
		return senderCurrent;
	}

	public List<Transfers> transfers(String accountId) throws AccountNotFoundException {
		List<Transfers> transferList = jdbcTemplate.query("Select * from transfers where senderId=?",
				new Object[] { accountId }, new RowMapper<Transfers>() {

					public Transfers mapRow(ResultSet rs, int rn) throws SQLException {
						Transfers transfer = new Transfers();
						transfer.setSenderId(rs.getString(1));
						transfer.setRecieverId(rs.getString(2));
						transfer.setAmount(rs.getDouble(3));
						transfer.setTimeStamp(rs.getTimestamp(4));
						return transfer;
					}

				});
		return transferList;
	}

}
