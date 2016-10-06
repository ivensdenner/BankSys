package banksys.control;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import banksys.account.AbstractAccount;
import banksys.account.OrdinaryAccount;
import banksys.account.exception.InsufficientFundsException;
import banksys.account.exception.NegativeAmountException;
import banksys.control.exception.BankTransactionException;
import banksys.persistence.IAccountRepository;
import banksys.persistence.exception.AccountCreationException;
import banksys.persistence.exception.AccountDeletionException;
import banksys.persistence.exception.AccountNotFoundException;
import banksys.persistence.exception.FlushException;

public class BankControllerTest {
	
	private IAccountRepository repository;
	private AbstractAccount account;
	private BankController controller;
	private boolean didCreateAccount;
	private boolean didRemoveAccount;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		repository = newRepositoryMock();
		account = newAccountMock();
		account.credit(50);
		controller = new BankController(repository);
		
		didCreateAccount = false;
		didRemoveAccount = false;
	}
	
	private IAccountRepository newRepositoryMock() {
		return new IAccountRepository() {
			
			@Override
			public AbstractAccount retrieve(String number) throws AccountNotFoundException {
				return account;
			}
			
			@Override
			public int mumberOfAccounts() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public AbstractAccount[] list() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void flush() throws FlushException {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void delete(String number) throws AccountDeletionException {
				didRemoveAccount = true;
			}
			
			@Override
			public void create(AbstractAccount account) throws AccountCreationException {
				didCreateAccount = true;
			}
		};
	}
	
	private AbstractAccount newAccountMock() {
		return new AbstractAccount("1234") {
			
			@Override
			public void debit(double amount) throws NegativeAmountException, InsufficientFundsException {
				if (amount < 0) {
					throw new NegativeAmountException(amount);
				}
				if (amount <= balance) {
					balance -= amount;
				} else {
					throw new InsufficientFundsException(number, amount);
				}
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddAccount() {
		try {
			controller.addAccount(account);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Account wasn't created in the repository", didCreateAccount);
	}
	
	@Test
	public void testRemoveAccount() {
		try {
			controller.addAccount(account);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		try {
			controller.removeAccount(account.getNumber());
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Account wasn't removed of the repository", didRemoveAccount);
	}
	
	@Test
	public void testDoCredit() {
		try {
			controller.doCredit(account.getNumber(), 30);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Account balance should be 80, is " + account.getBalance(), account.getBalance() == 80);
	}
	
	@Test
	public void testDoDebit() {
		try {
			controller.doDebit(account.getNumber(), 30);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Account balance should be 20, is " + account.getBalance(), account.getBalance() == 20);
	}

}
