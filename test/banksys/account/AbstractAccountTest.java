package banksys.account;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import banksys.account.exception.InsufficientFundsException;
import banksys.account.exception.NegativeAmountException;

public class AbstractAccountTest {
	
	private static final String ACCOUNT_NUMBER = "1234";
	private AbstractAccount account;

	@Before
	public void setUp() throws Exception {
		account = accountMock();
		account.credit(50);
	}
	
	private AbstractAccount accountMock() {
		return new AbstractAccount(ACCOUNT_NUMBER) {
			@Override
			public void debit(double amount) throws NegativeAmountException, InsufficientFundsException {
			}
		};
	}

	@Test
	public void testCredit() {
		try {
			account.credit(30);
		} catch (NegativeAmountException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Balance should be 80", account.getBalance() == 80);
	}
	
	@Test
	public void testCreditNegativeAmount() {
		try {
			account.credit(-30);
		} catch (NegativeAmountException e) {
			return;
		}
		
		fail("Should throw an exception");
	}

}
