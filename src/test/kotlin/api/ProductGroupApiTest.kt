package api

import com.epam.kotlinapp.crud.model.*
import com.google.gson.*
import com.google.gson.reflect.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.testing.*
import main
import java.lang.reflect.*
import java.util.*
import kotlin.test.*

@KtorExperimentalLocationsAPI
class ProductGroupApiTest {

    private val url: String = "/productgroups"
    private val idToDelete: Int = 1
    private val idToGet = 0
    private val gson = GsonBuilder().create();
    private val expectedProduct: ProductGroup = ProductGroup(0, "Super Duper Group")
    private val expectedAllProductList: List<ProductGroup> =
        listOf(expectedProduct, ProductGroup(1, "Super Duper Group v2"))

    @Test
    fun getAllProductGroupApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, url)) {
            assertEquals(HttpStatusCode.OK, response.status())
            val groupListType: Type = object : TypeToken<ArrayList<ProductGroup?>?>() {}.type
            assertEquals(expectedAllProductList, gson.fromJson(response.content, groupListType))
        }
    }

    @Test
    fun getProductGroupByIdApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/$idToGet")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expectedProduct, gson.fromJson(response.content, ProductGroup::class.java))
        }
    }

    @Test()
    fun createProductGroupApiTest() = withTestApplication(Application::main) {

        var productGroup: ProductGroup
        with(handleRequest(HttpMethod.Post, url) {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")
            setBody(
                gson.toJson(
                    ProductGroup(0, "Some Product Group")
                )
            )
        }) {
            assertEquals(HttpStatusCode.Created, response.status())
            productGroup = gson.fromJson(response.content, ProductGroup::class.java)
            val expectedProductFromResponse = ProductGroup(productGroup.id, "Some Product Group")
            assertEquals(expectedProductFromResponse, productGroup)
        }

    }

    @Test
    fun deleteUserApiTest() = withTestApplication(Application::main) {

        with(handleRequest(HttpMethod.Delete, "$url/$idToDelete") {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")

        }) {

            assertEquals(HttpStatusCode.OK, response.status())
            return@withTestApplication
        }
    }
}
