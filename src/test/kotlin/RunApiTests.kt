import api.ProductApiTest
import api.ProductGroupApiTest
import api.UserApiTests
import chat.WebSocketTest
import com.epam.kotlinapp.crud.model.ProductGroup
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    value = [ProductApiTest::class,
        ProductGroupApiTest::class,
        UserApiTests::class,
        WebSocketTest::class]
)
class RunApiTests {

}