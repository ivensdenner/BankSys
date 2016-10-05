package banksys.account;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import banksys.account.exception.InsufficientFundsException;
import banksys.account.exception.NegativeAmountException;

public class OrdinaryAccountTest {

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
	public void testDebit() {
		OrdinaryAccount account = new OrdinaryAccount("1234");
		try {
			account.credit(50);
		} catch (NegativeAmountException e) {
			fail(e.getMessage());
		}
		
		try {
			account.debit(30);
		} catch (NegativeAmountException | InsufficientFundsException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Balance should be 20, is " + account.getBalance(), account.getBalance() == 20);
	}

}
