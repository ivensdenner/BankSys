package banksys.control;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import banksys.account.AbstractAccount;
import banksys.account.SavingsAccount;
import banksys.account.SpecialAccount;
import banksys.account.exception.InsufficientFundsException;
import banksys.account.exception.NegativeAmountException;
import banksys.control.exception.BankTransactionException;
import banksys.control.exception.IncompatibleAccountException;
import banksys.persistence.IAccountRepository;
import banksys.persistence.exception.AccountCreationException;
import banksys.persistence.exception.AccountDeletionException;
import banksys.persistence.exception.AccountNotFoundException;
import banksys.persistence.exception.FlushException;

public class BankControllerTest {
	
	private static final String INVALID_ACCOUNT_NUMBER = "0000";
	private static final String ACCOUNT_NUMBER_A = "1234";
	private static final String ACCOUNT_NUMBER_B = "5678";
	private static final String SAVINGS_ACCOUNT_NUMBER = "9999";
	private static final String SPECIAL_ACCOUNT_NUMBER = "1010";
	
	private IAccountRepository repository;
	
	private AbstractAccount accountA;
	private AbstractAccount accountB;
	private SavingsAccount savingsAccount;
	private SpecialAccount specialAccount;
	
	private BankController controller;
	
	private boolean didCreateAccount;
	private boolean didRemoveAccount;

	@Before
	public void setUp() throws Exception {
		repository = newRepositoryMock();
		
		accountA = newAccountMock(true);
		accountA.credit(50);
		accountB = newAccountMock(false);
		savingsAccount = new SavingsAccount(SAVINGS_ACCOUNT_NUMBER);
		savingsAccount.credit(50);
		specialAccount = new SpecialAccount(SPECIAL_ACCOUNT_NUMBER);
		specialAccount.credit(50);
		
		controller = new BankController(repository);
		
		didCreateAccount = false;
		didRemoveAccount = false;
	}
	
	private IAccountRepository newRepositoryMock() {
		return new IAccountRepository() {
			@Override
			public AbstractAccount retrieve(String number) throws AccountNotFoundException {
				switch (number) {
				case ACCOUNT_NUMBER_A:
					return accountA;
				case ACCOUNT_NUMBER_B:
					return accountB;
				case SAVINGS_ACCOUNT_NUMBER:
					return savingsAccount;
				case SPECIAL_ACCOUNT_NUMBER:
					return specialAccount;
				default:
					throw new AccountNotFoundException("Invalid account number", number);
				}
			}
			
			@Override
			public int mumberOfAccounts() {
				return 4;
			}
			
			@Override
			public AbstractAccount[] list() {
				return new AbstractAccount[]{accountA, accountB, savingsAccount};
			}
			
			@Override
			public void flush() throws FlushException {
			}
			
			@Override
			public void delete(String number) throws AccountDeletionException {
				if (number != ACCOUNT_NUMBER_A) {
					throw new AccountDeletionException("Invalid account number", number);
				}
				didRemoveAccount = true;
			}
			
			@Override
			public void create(AbstractAccount account) throws AccountCreationException {
				if (account.getNumber() != ACCOUNT_NUMBER_A) {
					throw new AccountCreationException("Invalid account", account.getNumber());
				}
				didCreateAccount = true;
			}
		};
	}
	
	private AbstractAccount newAccountMock(boolean isA) {
		String accountNumber = isA ? ACCOUNT_NUMBER_A : ACCOUNT_NUMBER_B;
		return new AbstractAccount(accountNumber) {
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

	@Test
	public void testAddAccount() {
		try {
			controller.addAccount(accountA);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Didn't create account in the repository", didCreateAccount);
	}
	
	@Test
	public void testAddAccountBankTransactionException() {
		try {
			controller.addAccount(accountB);
		} catch (BankTransactionException e) {
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testRemoveAccount() {
		try {
			controller.removeAccount(ACCOUNT_NUMBER_A);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Didn't remove account in the repository", didRemoveAccount);
	}
	
	@Test
	public void testRemoveAccountBankTransactionException() {
		try {
			controller.removeAccount(INVALID_ACCOUNT_NUMBER);
		} catch (BankTransactionException e) {
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoCredit() {
		try {
			controller.doCredit(ACCOUNT_NUMBER_A, 30);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Account balance should be 80", accountA.getBalance() == 80);
	}
	
	@Test
	public void testDoCreditBankTransactionExceptionInvalidAccount() {
		try {
			controller.doCredit(INVALID_ACCOUNT_NUMBER, 30);
		} catch (BankTransactionException e) {
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoCreditBankTransactionExceptionNegativeAmount() {
		try {
			controller.doCredit(ACCOUNT_NUMBER_A, -30);
		} catch (BankTransactionException e) {
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoDebit() {
		try {
			controller.doDebit(ACCOUNT_NUMBER_A, 30);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Account balance should be 20", accountA.getBalance() == 20);
	}
	
	@Test
	public void testDoDebitBankTransactionExceptionInvalidAccount() {
		try {
			controller.doDebit(INVALID_ACCOUNT_NUMBER, 30);
		} catch (BankTransactionException e) {
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoDebitBankTransactionExceptionNegativeAmount() {
		try {
			controller.doDebit(ACCOUNT_NUMBER_A, -30);
		} catch (BankTransactionException e) {
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testGetBalance() {
		double balance = 0;
		try {
			balance = controller.getBalance(ACCOUNT_NUMBER_A);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Balance should be 50", balance == 50);
	}
	
	@Test
	public void testGetBalanceBankTransactionException() {
		try {
			controller.getBalance(INVALID_ACCOUNT_NUMBER);
		} catch (BankTransactionException e) {
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoTranfer() {
		try {
			controller.doTransfer(ACCOUNT_NUMBER_A, ACCOUNT_NUMBER_B, 30);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		assertTrue("Account A balance should be 20", accountA.getBalance() == 20);
		assertTrue("Account B balance should be 30", accountB.getBalance() == 30);
	}
	
	@Test
	public void testDoTranferBankTransactionExceptionInvalidAccountA() {
		try {
			controller.doTransfer(INVALID_ACCOUNT_NUMBER, ACCOUNT_NUMBER_B, 30);
		} catch (BankTransactionException e) {
			assertTrue("Account B balance should be 0", accountB.getBalance() == 0);
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoTranferBankTransactionExceptionInvalidAccountB() {
		try {
			controller.doTransfer(ACCOUNT_NUMBER_A, INVALID_ACCOUNT_NUMBER, 30);
		} catch (BankTransactionException e) {
			assertTrue("Account A balance should be 50", accountA.getBalance() == 50);
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoTranferBankTransactionExceptionNegativeAmount() {
		try {
			controller.doTransfer(ACCOUNT_NUMBER_A, ACCOUNT_NUMBER_B, -30);
		} catch (BankTransactionException e) {
			assertTrue("Account A balance should be 50", accountA.getBalance() == 50);
			assertTrue("Account B balance should be 0", accountB.getBalance() == 0);
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoTranferBankTransactionExceptionInsuficientFunds() {
		try {
			controller.doTransfer(ACCOUNT_NUMBER_A, ACCOUNT_NUMBER_B, 60);
		} catch (BankTransactionException e) {
			assertTrue("Account A balance should be 50", accountA.getBalance() == 50);
			assertTrue("Account B balance should be 0", accountB.getBalance() == 0);
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoEarnInterest() {
		try {
			controller.doEarnInterest(SAVINGS_ACCOUNT_NUMBER);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		double expectedBalance = 50 + (50 * 0.001);
		assertTrue("Savings account balance should be " + expectedBalance, savingsAccount.getBalance() == expectedBalance);
	}
	
	@Test
	public void testDoEarnInterestBankTransactionExceptionInvalidAccount() {
		try {
			controller.doEarnInterest(INVALID_ACCOUNT_NUMBER);
		} catch (IncompatibleAccountException e) {
			fail(e.getMessage());
		} catch (BankTransactionException e) {
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoEarnInterestIncompatibleAccountException() {
		try {
			controller.doEarnInterest(ACCOUNT_NUMBER_A);
		} catch (IncompatibleAccountException e) {
			return;
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		fail("Should throw IncompatibleAccountException");
	}
	
	@Test
	public void testDoEarnBonus() {
		try {
			controller.doEarnBonus(SPECIAL_ACCOUNT_NUMBER);
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		double expectedBalance = 50 + (50 * 0.01);
		assertTrue("Special Account balance should be " + expectedBalance, specialAccount.getBalance() == expectedBalance);
	}
	
	@Test
	public void testDoEarnBonusBankTransactionExceptionInvalidAccount() {
		try {
			controller.doEarnBonus(INVALID_ACCOUNT_NUMBER);
		} catch (IncompatibleAccountException e) {
			fail(e.getMessage());
		} catch (BankTransactionException e) {
			return;
		}
		
		fail("Should throw BankTransactionException");
	}
	
	@Test
	public void testDoEarnBonusIncompatibleAccountException() {
		try {
			controller.doEarnBonus(ACCOUNT_NUMBER_A);
		} catch (IncompatibleAccountException e) {
			return;
		} catch (BankTransactionException e) {
			fail(e.getMessage());
		}
		
		fail("Should throw IncompatibleAccountException");
	}

}
