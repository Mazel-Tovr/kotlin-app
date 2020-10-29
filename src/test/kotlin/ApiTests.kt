import com.epam.kotlinapp.main
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    routing {
        get("/") {
            call.respondText("Hello from Ktor Testable sample application")
        }
    }
}


class ApiTests {


    @Test
    fun testRequests() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "/")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("Hello from Ktor Testable sample application", response.content)
        }
        with(handleRequest(HttpMethod.Get, "/index.html")) {
            assertFalse(requestHandled)
        }
    }
}