package banksys.persistence;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import banksys.account.AbstractAccount;
import banksys.account.OrdinaryAccount;
import banksys.persistence.exception.AccountCreationException;
import banksys.persistence.exception.AccountDeletionException;
import banksys.persistence.exception.AccountNotFoundException;
import banksys.persistence.exception.FlushException;

public class AccountXStreamTest {
	
	private AccountXStream repository;
	private AbstractAccount account;

	@Before
	public void setUp() throws Exception {
		repository = new AccountXStream();
		account = new OrdinaryAccount("1234");
	}

	@After
	public void tearDown() throws Exception {
		try {
			repository.delete(account.getNumber());
			repository.flush();
		} catch (AccountDeletionException | FlushException e) {
		}
	}

	@Test
	public void testCreate() {
		try {
			repository.create(account);
			repository.flush();
		} catch (AccountCreationException | FlushException e) {
			fail(e.getMessage());
		}
		
		repository = new AccountXStream();
		
		assertTrue("Number of accounts should be 1", repository.mumberOfAccounts() == 1);
	}
	
	@Test
	public void testDelete() {
		try {
			repository.create(account);
			repository.flush();
		} catch (AccountCreationException | FlushException e) {
			fail(e.getMessage());
		}
		
		try {
			repository.delete(account.getNumber());
			repository.flush();
		} catch (AccountDeletionException | FlushException e) {
			fail(e.getMessage());
		}
		
		repository = new AccountXStream();
		
		assertTrue("Number of accounts should be 0", repository.mumberOfAccounts() == 0);
	}
	
	@Test
	public void testRetrieve() {
		try {
			repository.create(account);
			repository.flush();
		} catch (AccountCreationException | FlushException e) {
			fail(e.getMessage());
		}
		
		repository = new AccountXStream();
		
		AbstractAccount retrievedAccount = null;
		try {
			retrievedAccount = repository.retrieve(account.getNumber());
		} catch (AccountNotFoundException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Account numbers should be equal", account.getNumber().equals(retrievedAccount.getNumber()));
	}
	
	@Test
	public void testNumberOfAccounts() {
		try {
			repository.create(account);
			repository.flush();
		} catch (AccountCreationException | FlushException e) {
			fail(e.getMessage());
		}
		
		repository = new AccountXStream();
		
		assertTrue("Number of accounts should be 1", repository.mumberOfAccounts() == 1);
	}

}
