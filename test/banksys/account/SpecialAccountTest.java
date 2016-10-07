package banksys.account;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import banksys.account.exception.NegativeAmountException;

public class SpecialAccountTest {
	
	private static final String ACCOUNT_NUMBER = "1234";
	private SpecialAccount account;

	@Before
	public void setUp() throws Exception {
		account = new SpecialAccount(ACCOUNT_NUMBER);
		account.credit(50);
	}

	@Test
	public void testEarnBonus() {
		account.earnBonus();
		double expectedBalance = 50 + (50 * 0.01);
		assertTrue("Balance should be " + expectedBalance, account.getBalance() == expectedBalance);
	}
	
	@Test
	public void testCredit() {
		try {
			account.credit(30);
		} catch (NegativeAmountException e) {
			fail(e.getMessage());
		}
		
		double expectedBalance = 80;
		double expectedBonus = 80 * 0.01;
		
		assertTrue("Balance should be " + expectedBalance, account.getBalance() == expectedBalance);
		assertTrue("Bonus should be " + expectedBonus, account.getBonus() == expectedBonus);
	}
	
	@Test
	public void testCreditNegativeAmount() {
		try {
			account.credit(-30);
		} catch (NegativeAmountException e) {
			return;
		}
		
		fail("Should throw NegativeAmountException");
	}

}
