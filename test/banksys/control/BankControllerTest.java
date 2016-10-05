package banksys.control;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import banksys.account.AbstractAccount;
import banksys.account.OrdinaryAccount;
import banksys.control.exception.BankTransactionException;
import banksys.persistence.AccountVector;
import banksys.persistence.IAccountRepository;
import banksys.persistence.exception.AccountCreationException;
import banksys.persistence.exception.AccountDeletionException;
import banksys.persistence.exception.AccountNotFoundException;
import banksys.persistence.exception.FlushException;

public class BankControllerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		IAccountRepository repository = new IAccountRepository() {
			
			@Override
			public AbstractAccount retrieve(String number) throws AccountNotFoundException {
				// TODO Auto-generated method stub
				return null;
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
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void create(AbstractAccount account) throws AccountCreationException {
				// TODO Auto-generated method stub
				
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddAccount() {
		IAccountRepository repository = new AccountVector();
		BankController controller = new BankController(repository);
		
		AbstractAccount account = new OrdinaryAccount("1234");
		try {
			controller.addAccount(account);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Number of accounts should be 1, is " + repository.mumberOfAccounts(), repository.mumberOfAccounts() == 1);
	}
	
	@Test
	public void testRemoveAccount() {
		IAccountRepository repository = new AccountVector();
		BankController controller = new BankController(repository);
		
		AbstractAccount account = new OrdinaryAccount("1234");
		try {
			controller.addAccount(account);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		try {
			controller.removeAccount("1234");
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Number of accounts should be 0, is " + repository.mumberOfAccounts(), repository.mumberOfAccounts() == 0);
	}

}
