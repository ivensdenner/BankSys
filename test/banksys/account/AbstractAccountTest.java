package banksys.account;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import banksys.account.exception.InsufficientFundsException;
import banksys.account.exception.NegativeAmountException;

public class AbstractAccountTest {
	
	private AbstractAccount account;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		account = new AbstractAccount("1234") {
			@Override
			public void debit(double amount) throws NegativeAmountException, InsufficientFundsException {
				System.out.println("debit amount: " + amount);
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCredit() {
		try {
			account.credit(50);
		} catch (NegativeAmountException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Balance should be 50, is " + account.getBalance(), account.getBalance() == 50);
	}

}
