package banksys.account;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import banksys.account.exception.NegativeAmountException;

public class SavingsAccountTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEarnInterest() {
		SavingsAccount account = new SavingsAccount("1234");
		try {
			account.credit(50);
		} catch (NegativeAmountException e) {
			fail(e.getMessage());
		}
		
		account.earnInterest();
		assertTrue("Balance should be " + (50 + (50 * 0.001)) + ", is " + account.getBalance(), account.getBalance() == 50 + (50 * 0.001));
	}

}
