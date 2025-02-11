import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    ParameterizedTestExample.class,
    RepeatableTestExample.class
})
public class AdvancedTestSuite {
    // Runs parameterized and repeatable tests
}
