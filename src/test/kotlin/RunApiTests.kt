import api.tests.ProductApiTest
import api.tests.UserApiTests
import api.tests.db.ConnectionToTestDB
import org.junit.runner.RunWith
import org.junit.runners.Suite
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [ProductApiTest::class, ProductApiTest::class, UserApiTests::class])
class RunApiTests {

    companion object {

        fun setConn(someImpl: Any) {
            val kClass = Class.forName(someImpl.javaClass.name).kotlin
            val member = kClass.memberProperties.filterIsInstance<KMutableProperty<*>>()
                    .firstOrNull { it.name == "conn" }
            member?.isAccessible = true
            member?.setter?.call(ConnectionToTestDB.conn)

        }
    }
}