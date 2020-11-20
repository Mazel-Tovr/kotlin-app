import api.*
import chat.*
import org.junit.runner.*
import org.junit.runners.*
import kotlin.test.*

@Ignore
@RunWith(Suite::class)
@Suite.SuiteClasses(
    value = [ProductApiTest::class,
        ProductGroupApiTest::class,
        UserApiTests::class,
        WebSocketTest::class]
)
class RunApiTests {

}
