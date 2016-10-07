package banksys.account;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AbstractAccountTest.class, OrdinaryAccountTest.class, SavingsAccountTest.class,
		SpecialAccountTest.class, TaxAccountTest.class })
public class AccountTests {

}
