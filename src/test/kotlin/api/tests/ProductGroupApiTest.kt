package api.tests

import com.epam.kotlinapp.crud.business.ICommonServices
import com.epam.kotlinapp.crud.business.ProductGroupService
import com.epam.kotlinapp.crud.model.ProductGroup
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.lang.reflect.Type
import java.util.ArrayList
import kotlin.test.assertEquals

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ProductGroupApiTest
{
    private val url:String = "/productgroup"
    private val gson = GsonBuilder().create();
    private val service: ICommonServices<ProductGroup> = ProductGroupService

    @Test
    fun _1_getAllUserApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/all")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val groupListType: Type = object : TypeToken<ArrayList<ProductGroup?>?>() {}.getType()
            assertEquals(service.getAll(), gson.fromJson(response.content, groupListType))
        }
    }

    @Test
    fun _2_getUserByIdApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/1")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(service.getEntity(1), gson.fromJson(response.content, ProductGroup::class.java))
        }
    }

    @Test
    fun _3_createAndDeleteUserApiTest() = withTestApplication(Application::main) {

        var productGroup: ProductGroup
        with(handleRequest(HttpMethod.Post, url) {
            addHeader("accept","application/json")
            addHeader("Content-Type" ,"application/json")
            setBody(
                gson.toJson(
                    ProductGroup(0,"Some Product Group")
                )
            )
        }) {
            assertEquals(HttpStatusCode.Created, response.status())
            productGroup = gson.fromJson(response.content, ProductGroup::class.java)
            assertEquals(service.getEntity(productGroup.id!!), productGroup)
        }

        with(handleRequest(HttpMethod.Delete, url) {
            addHeader("accept","application/json")
            addHeader("Content-Type" ,"application/json")
            setBody(
                gson.toJson(
                    productGroup
                )
            )
        }) {

            assertEquals(HttpStatusCode.OK, response.status())
            service.getEntity(productGroup.id!!)
            return@withTestApplication
        }
    }
}