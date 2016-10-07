package banksys.account;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import banksys.account.exception.InsufficientFundsException;
import banksys.account.exception.NegativeAmountException;

public class OrdinaryAccountTest {
	
	private static final String ACCOUNT_NUMBER = "1234";
	private OrdinaryAccount account;
	
	@Before
	public void setUp() throws Exception {
		account = new OrdinaryAccount(ACCOUNT_NUMBER);
		account.credit(50);
	}

	@Test
	public void testDebit() {
		try {
			account.debit(30);
		} catch (NegativeAmountException | InsufficientFundsException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Balance should be 20", account.getBalance() == 20);
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
	public void testDebitInsufficientFunds() {
		try {
			account.debit(80);
		} catch (NegativeAmountException e) {
			fail(e.getMessage());
		} catch (InsufficientFundsException e) {
			return;
		}
		
		fail("Should throw InsufficientFundsException");
	}

}
