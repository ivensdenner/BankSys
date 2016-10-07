package banksys.account;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import banksys.account.exception.InsufficientFundsException;
import banksys.account.exception.NegativeAmountException;

public class TaxAccountTest {
	
	private static final String ACCOUNT_NUMBER = "1234";
	private TaxAccount account;

	@Before
	public void setUp() throws Exception {
		account = new TaxAccount(ACCOUNT_NUMBER);
		account.credit(50);
	}

	@Test
	public void testDebit() {
		try {
			account.debit(30);
		} catch (NegativeAmountException | InsufficientFundsException e) {
			fail(e.getMessage());
		}
		
		double expectedBalance = 50 - (30 + (30 * 0.001));
		assertTrue("Balance should be " + expectedBalance, account.getBalance() == expectedBalance);
	}
	
	@Test
	public void testDebitNegativeAmount() {
		try {
			account.debit(-30);
		} catch (InsufficientFundsException e) {
			fail(e.getMessage());
		} catch (NegativeAmountException e) {
			return;
		}
		
		fail("Should throw NegativeAmountException");
	}
	
	@Test
	public void testDebitInsufficientFundsException() {
		try {
			account.debit(80);
		} catch (InsufficientFundsException e) {
			return;
		} catch (NegativeAmountException e) {
			fail(e.getMessage());
		}
		
		fail("Should throw InsufficientFundsException");
	}

}
