import integration.*
import io.ktor.locations.*
import org.junit.runner.*
import org.junit.runners.*
import kotlin.test.*

@KtorExperimentalLocationsAPI
@Ignore
@RunWith(Suite::class)
@Suite.SuiteClasses(
    value = [ProductApiTest::class,
        ProductGroupApiTest::class,
        UserApiTests::class]
)
class RunApiTests {

}
