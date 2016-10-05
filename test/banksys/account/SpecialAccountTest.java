package banksys.account;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import banksys.account.exception.NegativeAmountException;

public class SpecialAccountTest {

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
	public void testEarnBonus() {
		SpecialAccount account = new SpecialAccount("1234");
		try {
			account.credit(50);
		} catch (NegativeAmountException e) {
			fail(e.getMessage());
		}
		
		account.earnBonus();
		assertTrue("", account.getBalance() == (50 + (50 * 0.01)));
		
		account.earnBonus();
		assertTrue("", account.getBalance() == (50 + (50 * 0.01)));
	}
	
	@Test
	public void testCredit() {
		SpecialAccount account = new SpecialAccount("1234");
		try {
			account.credit(50);
		} catch (NegativeAmountException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Balance should be 50, is " + account.getBalance(), account.getBalance() == 50);
		assertTrue("Bonus should be " + (50 * 0.01) + ", is " + account.getBonus(), account.getBonus() == 50 * 0.01);
	}

}
