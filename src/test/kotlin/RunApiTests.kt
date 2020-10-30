import api.tests.ProductApiTest
import api.tests.UserApiTests
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [ProductApiTest::class,ProductApiTest::class,UserApiTests::class])
class RunApiTests {

}