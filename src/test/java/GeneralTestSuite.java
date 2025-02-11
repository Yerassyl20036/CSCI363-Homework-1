import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    LibraryTest.class,
    BookTest.class,
    UserTest.class,
    TransactionTest.class
})
public class GeneralTestSuite {
    
}
