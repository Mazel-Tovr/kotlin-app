package integration

import ServerTestConfig
import com.epam.kotlinapp.crud.model.*
import com.google.gson.*
import com.google.gson.reflect.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import java.lang.reflect.*
import java.util.*
import kotlin.test.*

@KtorExperimentalLocationsAPI
class UserApiTests {

    private val url: String = "/users"
    private val idToDelete: Int = 1
    private val idToGet = 0
    private val gson = GsonBuilder().create()
    private val expectedUser: User = User(0, "Roma", "roma@mail.ru", "123")
    private val expectedAllUsersList: List<User> = listOf(expectedUser, User(1, "Sanya", "sanya@mail.ru", "123"))

    @Test
    fun getAllUserApiTest() = withTestApplication(Application::ServerTestConfig) {
        with(handleRequest(HttpMethod.Get, url)) {
            assertEquals(HttpStatusCode.OK, response.status())
            val groupListType: Type = object : TypeToken<ArrayList<User?>?>() {}.type
            assertEquals(expectedAllUsersList, gson.fromJson(response.content, groupListType))
        }
    }

    @Test
    fun getUserByIdApiTest() = withTestApplication(Application::ServerTestConfig) {
        with(handleRequest(HttpMethod.Get, "$url/$idToGet")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expectedUser, gson.fromJson(response.content, User::class.java))
        }
    }

    @Test
    fun createUserApiTest() = withTestApplication(Application::ServerTestConfig) {

        var userFromResponse: User
        with(handleRequest(HttpMethod.Post, url) {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")
            setBody(
                gson.toJson(
                    User(0, "User", "User", "User")
                )
            )
        }) {
            assertEquals(HttpStatusCode.Created, response.status())
            userFromResponse = gson.fromJson(response.content, User::class.java)
            val expectedNewUser = User(userFromResponse.id, "User", "User", "User")

            assertEquals(expectedNewUser, userFromResponse)
        }


    }

    @Test
    fun deleteUserApiTest() = withTestApplication(Application::ServerTestConfig) {

        with(handleRequest(HttpMethod.Delete, "$url/$idToDelete") {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")

        }) {

            assertEquals(HttpStatusCode.OK, response.status())
            return@withTestApplication
        }
    }

}


