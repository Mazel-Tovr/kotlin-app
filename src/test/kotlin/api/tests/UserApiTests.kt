package api.tests

import com.epam.kotlinapp.crud.business.ICommonServices
import com.epam.kotlinapp.crud.business.UserService
import com.epam.kotlinapp.crud.exceptions.UserNotFoundException
import com.epam.kotlinapp.crud.model.User
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.lang.reflect.Type
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserApiTests {
    private val url:String = "/user"
    private val gson = GsonBuilder().create();
    private val service: ICommonServices<User> = UserService


    @Test
    fun _1_getAllUserApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/all")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val groupListType: Type = object : TypeToken<ArrayList<User?>?>() {}.getType()
            assertEquals(service.getAll(), gson.fromJson(response.content, groupListType))
        }
    }

    @Test
    fun _2_getUserByIdApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/1")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(service.getEntity(1), gson.fromJson(response.content, User::class.java))
        }
    }

    @Test(UserNotFoundException::class)
    fun _3_createAndDeleteUserApiTest() = withTestApplication(Application::main) {

        var userFromResponse:User
        with(handleRequest(HttpMethod.Post, url) {
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
            assertEquals(service.getEntity(userFromResponse.id!!), userFromResponse)
        }

        with(handleRequest(HttpMethod.Delete, url.plus("/${userFromResponse.id}")) {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")
//            setBody(
//                gson.toJson(
//                    userFromResponse
//                )
//            )
        }) {

            assertEquals(HttpStatusCode.OK, response.status())
            service.getEntity(userFromResponse.id!!)
            return@withTestApplication
        }
    }


}