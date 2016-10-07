package banksys.persistence;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import banksys.account.AbstractAccount;
import banksys.account.exception.InsufficientFundsException;
import banksys.account.exception.NegativeAmountException;
import banksys.persistence.exception.AccountCreationException;
import banksys.persistence.exception.AccountDeletionException;
import banksys.persistence.exception.AccountNotFoundException;
import banksys.persistence.exception.FlushException;

public class AccountXStreamTest {
	
	private static final String ACCOUNT_NUMBER_A = "1234";
	private static final String ACCOUNT_NUMBER_B = "5678";
	private AccountXStream repository;
	private AbstractAccount accountA;
	private AbstractAccount accountB;

	@Before
	public void setUp() throws Exception {
		repository = new AccountXStream();
		accountA = accountMock(true);
		repository.create(accountA);
		accountB = accountMock(false);
	}
	
	private AbstractAccount accountMock(boolean isA) {
		String accountNumber = isA ? ACCOUNT_NUMBER_A : ACCOUNT_NUMBER_B;
		return new AbstractAccount(accountNumber) {
			@Override
			public void debit(double amount) throws NegativeAmountException, InsufficientFundsException {
			}
		};
	}

	@After
	public void tearDown() throws Exception {
		try {
			repository.delete(ACCOUNT_NUMBER_A);
			repository.delete(ACCOUNT_NUMBER_B);
		} catch (AccountDeletionException e) {
		} finally {
			repository.flush();
		}
	}
	
	@Test
	public void testDelete() {
		try {
			repository.delete(ACCOUNT_NUMBER_A);
			repository.flush();
		} catch (AccountDeletionException | FlushException e) {
			fail(e.getMessage());
		}
		
		repository = new AccountXStream();
		
		assertTrue("Number of accounts should be 0", repository.mumberOfAccounts() == 0);
	}
	
	@Test
	public void testDeleteAccountDeletionException() {
		try {
			repository.delete("0000");
		} catch (AccountDeletionException e) {
			return;
		}
		
		fail("Should throw AccountDeletionException");
	}
	
	@Test
	public void testCreate() {
		try {
			repository.create(accountB);
			repository.flush();
		} catch (AccountCreationException | FlushException e) {
			fail(e.getMessage());
		}
		
		repository = new AccountXStream();
		
		assertTrue("Number of accounts should be 2", repository.mumberOfAccounts() == 2);
	}
	
	@Test
	public void testCreateAccountCreationException() {
		try {
			repository.create(accountA);
		} catch (AccountCreationException e) {
			return;
		}
		
		fail("Should throw AccountCreationException");
	}
	
	@Test
	public void testNumberOfAccounts() {
		assertTrue("Number of accounts should be 1", repository.mumberOfAccounts() == 1);
	}
	
	@Test
	public void testRetrieve() {
		AbstractAccount retrievedAccount = null;
		try {
			retrievedAccount = repository.retrieve(ACCOUNT_NUMBER_A);
		} catch (AccountNotFoundException e) {
			fail(e.getMessage());
		}

		assertTrue("Returned wrong account", ACCOUNT_NUMBER_A == retrievedAccount.getNumber());
	}
	
	@Test
	public void testRetrieveAccountNotFoundException() {
		try {
			repository.retrieve("0000");
		} catch (AccountNotFoundException e) {
			return;
		}

		fail("Should throw AccountNotFoundException");
	}

}
