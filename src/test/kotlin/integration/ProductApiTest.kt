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
class ProductApiTest {

    private val url: String = "/products"
    private val idToDelete = 1
    private val idToGet = 0
    private val gson = GsonBuilder().create();
    private val expectedProduct: Product = Product(0, "Телефон", 228, "Nokia 330", 0, 0)
    private val expectedAllProductList: List<Product> =
        listOf(expectedProduct, Product(1, "IТелефон", 1337, "Samsung", 1, 1))

    @Test
    fun getAllProductApiTest() = withTestApplication(Application::ServerTestConfig) {
        with(handleRequest(HttpMethod.Get, url)) {
            assertEquals(HttpStatusCode.OK, response.status())
            val groupListType: Type = object : TypeToken<ArrayList<Product?>?>() {}.type
            assertEquals(expectedAllProductList, gson.fromJson(response.content, groupListType))
        }
    }

    @Test
    fun getProductByIdApiTest() = withTestApplication(Application::ServerTestConfig) {
        with(handleRequest(HttpMethod.Get, "$url/$idToGet")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expectedProduct, gson.fromJson(response.content, Product::class.java))
        }
    }

    @Test
    fun createProductApiTest() = withTestApplication(Application::ServerTestConfig) {

        with(handleRequest(HttpMethod.Post, url) {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")
            setBody(
                gson.toJson(
                    Product(0, "Product", 123, "Product", 1, 1)
                )
            )
        }) {
            assertEquals(HttpStatusCode.Created, response.status())
            val product: Product = gson.fromJson(response.content, Product::class.java)
            val expectedResponseProduct: Product =
                Product(product.id, "Product", 123, "Product", 1, 1)

            assertEquals(expectedResponseProduct, product)
        }

    }

    @Test
    fun deleteProductApiTest() = withTestApplication(Application::ServerTestConfig) {

        with(handleRequest(HttpMethod.Delete, "$url/$idToDelete") {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")

        }) {

            assertEquals(HttpStatusCode.OK, response.status())
            return@withTestApplication
        }
    }
}
