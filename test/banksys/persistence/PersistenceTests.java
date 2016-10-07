package banksys.persistence;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountVectorTest.class, AccountXStreamTest.class })
public class PersistenceTests {

}
