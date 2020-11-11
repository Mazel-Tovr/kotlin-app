import db.ConnectionToTestDB
import com.epam.kotlinapp.chat.server.Server
import com.epam.kotlinapp.chat.server.webSocket
import com.epam.kotlinapp.crud.business.ProductGroupService
import com.epam.kotlinapp.crud.business.ProductService
import com.epam.kotlinapp.crud.business.UserService
import com.epam.kotlinapp.crud.controllers.productController
import com.epam.kotlinapp.crud.controllers.productGroupController
import com.epam.kotlinapp.crud.controllers.userController
import com.epam.kotlinapp.crud.dao.ICommonOperations
import com.epam.kotlinapp.crud.dao.ProductGroupOperations
import com.epam.kotlinapp.crud.dao.ProductOperations
import com.epam.kotlinapp.crud.dao.UserOperations
import de.nielsfalk.ktor.swagger.SwaggerSupport
import de.nielsfalk.ktor.swagger.json
import de.nielsfalk.ktor.swagger.version.v2.Swagger
import de.nielsfalk.ktor.swagger.version.v3.OpenApi
import io.ktor.application.*
import io.ktor.client.features.json.serializer.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


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
    setConn(ProductOperations)
    setConn(ProductGroupOperations)
    setConn(UserOperations)


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
        member.setter.call(ConnectionToTestDB.conn)
    }
}