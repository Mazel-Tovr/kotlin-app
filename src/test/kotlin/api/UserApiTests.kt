package api

import com.epam.kotlinapp.crud.model.User
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import main
import java.lang.reflect.Type
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

@KtorExperimentalLocationsAPI
class UserApiTests {

    private val url: String = "/user"
    private val gson = GsonBuilder().create()
    private val expectedUser: User = User(1, "Roma", "roma@mail.ru", "123")
    private val expectedAllUsersList: List<User> = listOf(expectedUser, User(2, "Sanya", "sanya@mail.ru", "123"))
    private val userIdToDelete: Int = 2


    @Test
    fun getAllUserApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/all")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val groupListType: Type = object : TypeToken<ArrayList<User?>?>() {}.type
            assertEquals(expectedAllUsersList, gson.fromJson(response.content, groupListType))
        }
    }

    @Test
    fun getUserByIdApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/1")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expectedUser, gson.fromJson(response.content, User::class.java))
        }
    }

    @Test
    fun createUserApiTest() = withTestApplication(Application::main) {

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
    fun deleteUserApiTest() = withTestApplication(Application::main) {

        with(handleRequest(HttpMethod.Delete, "$url/$userIdToDelete") {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")

        }) {

            assertEquals(HttpStatusCode.OK, response.status())
            return@withTestApplication
        }
    }

}


