package wallet.exception;

@SuppressWarnings("serial")
public class InsufficientFundsException extends Exception {
	public InsufficientFundsException(String message) {
		super(message);
	}
}
