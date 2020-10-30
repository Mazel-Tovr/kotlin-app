import com.epam.kotlinapp.crud.business.ProductGroupService
import com.epam.kotlinapp.crud.business.ProductService
import com.epam.kotlinapp.crud.business.UserService
import com.epam.kotlinapp.crud.controllers.productController
import com.epam.kotlinapp.crud.controllers.productGroupController
import com.epam.kotlinapp.crud.controllers.userController
import com.epam.kotlinapp.crud.model.User
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.nielsfalk.ktor.swagger.SwaggerSupport
import de.nielsfalk.ktor.swagger.version.v2.Swagger
import de.nielsfalk.ktor.swagger.version.v3.OpenApi
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.lang.reflect.Type
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals


//Server for testing
fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Locations)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(SwaggerSupport) {
        swagger = Swagger().apply {}
        openApi = OpenApi().apply {}
    }
    routing {
        this.userController(UserService)
        this.productController(ProductService)
        this.productGroupController(ProductGroupService)
    }
}
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserApiTests {


    private val gson = GsonBuilder().create();
    private var userFromResponse: User? =null

    @Test
    fun _1_getAllUserApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/user/all")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val groupListType: Type = object : TypeToken<ArrayList<User?>?>() {}.getType()
            assertEquals(UserService.getAll(), gson.fromJson(response.content, groupListType))
        }
    }

    @Test
    fun _2_getUserByIdApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/user/1")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(UserService.getEntity(1), gson.fromJson(response.content, User::class.java))
        }
    }

    @Test
    fun _3_createUserApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Post, "/user") {
            addHeader("accept","application/json")
            addHeader("Content-Type" ,"application/json")
            setBody(
                gson.toJson(
                    User(0, "User", "User", "User")
                )
            )
        }) {
            assertEquals(HttpStatusCode.Created, response.status())
            userFromResponse = gson.fromJson(response.content, User::class.java)
            assertEquals(UserService.getEntity(userFromResponse!!.id!!), userFromResponse)
        }
    }

    @Test
    fun _4_deleteUserApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Delete, "/user") {
            addHeader("accept","application/json")
            addHeader("Content-Type" ,"application/json")
            setBody(
                gson.toJson(
                    userFromResponse
                )
            )
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            UserService.getEntity(userFromResponse!!.id!!)
            return@withTestApplication
        }
    }
}