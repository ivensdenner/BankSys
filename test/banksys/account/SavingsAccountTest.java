package banksys.account;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SavingsAccountTest {
	
	private static final String ACCOUNT_NUMBER = "1234";
	private SavingsAccount account;

	@Before
	public void setUp() throws Exception {
		account = new SavingsAccount(ACCOUNT_NUMBER);
		account.credit(50);
	}

	@Test
	public void testEarnInterest() {
		account.earnInterest();
		double expectedBalance = 50 + (50 * 0.001);
		assertTrue("Balance should be " + expectedBalance, account.getBalance() == expectedBalance);
	}

}
