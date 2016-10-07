package banksys.persistence;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import banksys.account.AbstractAccount;
import banksys.account.exception.InsufficientFundsException;
import banksys.account.exception.NegativeAmountException;
import banksys.persistence.exception.AccountCreationException;
import banksys.persistence.exception.AccountDeletionException;
import banksys.persistence.exception.AccountNotFoundException;

public class AccountVectorTest {
	
	private static final String ACCOUNT_NUMBER_A = "1234";
	private static final String ACCOUNT_NUMBER_B = "5678";
	private AccountVector repository;
	private AbstractAccount accountA;
	private AbstractAccount accountB;

	@Before
	public void setUp() throws Exception {
		repository = new AccountVector();
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

	@Test
	public void testDelete() {
		try {
			repository.delete(ACCOUNT_NUMBER_A);
		} catch (AccountDeletionException e) {
			fail(e.getMessage());
		}
		
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
		} catch (AccountCreationException e) {
			fail(e.getMessage());
		}
		
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
	
	@Test
	public void testNumberOfAccounts() {
		assertTrue("Number of accounts should be 1", repository.mumberOfAccounts() == 1);
	}

}
