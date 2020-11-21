import com.epam.kotlinapp.chat.server.*
import com.epam.kotlinapp.crud.business.*
import com.epam.kotlinapp.crud.endpoints.*
import com.epam.kotlinapp.crud.dao.*
import com.epam.kotlinapp.crud.dao.nosql.*
import connection.nosql.*
import de.nielsfalk.ktor.swagger.*
import de.nielsfalk.ktor.swagger.version.v2.*
import de.nielsfalk.ktor.swagger.version.v3.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.*


//Server for testing
@KtorExperimentalLocationsAPI
fun Application.main() {

    val server = Server()

    install(DefaultHeaders)
    install(CallLogging)
    install(Locations)
    install(ContentNegotiation) {
        json()
    }
    install(SwaggerSupport) {
        swagger = Swagger().apply {}
        openApi = OpenApi().apply {}
    }
    install(WebSockets)
    //setting con to test bd
    setConn(ProductOperationImpl)
    setConn(ProductGroupOperationImp)
    setConn(UserOperationImpl)


    routing {
        this.userController(UserService,server)
        this.productController(ProductService,server)
        this.productGroupController(ProductGroupService,server)
        this.webSocket(server)
    }
}

private fun setConn(someImpl: ICommonOperations<*>) {
    val kClass = Class.forName(someImpl.javaClass.name).kotlin
    val member = kClass.memberProperties.filterIsInstance<KMutableProperty<*>>()
        .firstOrNull { it.name == "conn" }
    if (member != null) {
        member.isAccessible = true
        member.setter.call(ConnectionToTestNoSqlDb.entityStore)
    }
}
