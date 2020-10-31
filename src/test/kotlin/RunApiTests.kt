import api.tests.ProductApiTest
import api.tests.UserApiTests
import api.tests.db.ConnectionToTestDB
import com.epam.kotlinapp.crud.dao.ICommonOperations
import com.epam.kotlinapp.crud.dao.ProductGroupOperations
import com.epam.kotlinapp.crud.dao.ProductOperations
import com.epam.kotlinapp.crud.dao.UserOperations
import org.junit.runner.RunWith
import org.junit.runners.Suite
import java.util.*
import javax.swing.text.html.parser.Entity
import kotlin.test.BeforeTest

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [ProductApiTest::class,ProductApiTest::class,UserApiTests::class])
class RunApiTests {

    @BeforeTest
    fun setConnToTestBd()
    {
       setConn(ProductGroupOperations)
       setConn(UserOperations)
       setConn(ProductOperations)
    }


    private fun setConn(someImpl: Any)
    {
        val clazz = someImpl::javaClass.get()
        val field = clazz.getField("conn")
        field.set(someImpl, ConnectionToTestDB.conn)
    }

}