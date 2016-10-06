package banksys.persistence;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import banksys.account.AbstractAccount;
import banksys.account.exception.InsufficientFundsException;
import banksys.account.exception.NegativeAmountException;
import banksys.persistence.exception.AccountCreationException;
import banksys.persistence.exception.AccountDeletionException;
import banksys.persistence.exception.AccountNotFoundException;

public class AccountVectorTest {
	
	private AccountVector repository;
	private AbstractAccount account;

	@Before
	public void setUp() throws Exception {
		repository = new AccountVector();
		account = accountMock();
	}
	
	private AbstractAccount accountMock() {
		return new AbstractAccount("1234") {
			@Override
			public void debit(double amount) throws NegativeAmountException, InsufficientFundsException {
				// TODO Auto-generated method stub
			}
		};
	}

	@Test
	public void testDelete() {
		try {
			repository.create(account);
		} catch (AccountCreationException e) {
			fail(e.getMessage());
		}
		
		try {
			repository.delete(account.getNumber());
		} catch (AccountDeletionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Number of accounts should be 0", repository.mumberOfAccounts() == 0);
	}
	
	@Test
	public void testCreate() {
		try {
			repository.create(account);
		} catch (AccountCreationException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Number of accounts should be 1", repository.mumberOfAccounts() == 1);
	}
	
	@Test
	public void testRetrieve() {
		try {
			repository.create(account);
		} catch (AccountCreationException e) {
			fail(e.getMessage());
		}
		
		AbstractAccount retrievedAccount = null;
		
		try {
			retrievedAccount = repository.retrieve(account.getNumber());
		} catch (AccountNotFoundException e) {
			fail(e.getMessage());
		}

		assertSame("Returned a wrong account", account, retrievedAccount);
	}
	
	@Test
	public void testNumberOfAccounts() {
		try {
			repository.create(account);
		} catch (AccountCreationException e) {
			fail(e.getMessage());
		}

		assertTrue("Number of accounts should be 1", repository.mumberOfAccounts() == 1);
	}

}
