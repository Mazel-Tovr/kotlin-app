package api

import com.epam.kotlinapp.crud.business.ICommonServices
import com.epam.kotlinapp.crud.business.ProductService
import com.epam.kotlinapp.crud.exceptions.ProductNotFoundException
import com.epam.kotlinapp.crud.model.Product
import com.epam.kotlinapp.crud.model.User
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import main
import kotlin.test.Test
import kotlin.test.assertEquals
import java.lang.reflect.Type
import java.util.ArrayList

class ProductApiTest {
    private val url: String = "/product"
    private val gson = GsonBuilder().create();
    private val expectedProduct: Product = Product(1, "Телефон", 228, "Nokia 330", 1, 1)
    private val expectedAllProductList: List<Product> =
        listOf(expectedProduct, Product(2, "IТелефон", 1337, "Samsung", 2, 2))
    private val productIdToDelete: Int = 2


    @Test
    fun getAllProductApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/all")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val groupListType: Type = object : TypeToken<ArrayList<Product?>?>() {}.type
            assertEquals(expectedAllProductList, gson.fromJson(response.content, groupListType))
        }
    }

    @Test
    fun getProductByIdApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/1")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expectedProduct, gson.fromJson(response.content, Product::class.java))
        }
    }

    @Test
    fun createProductApiTest() = withTestApplication(Application::main) {

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
    fun deleteProductApiTest() = withTestApplication(Application::main) {

        with(handleRequest(HttpMethod.Delete, "$url/$productIdToDelete") {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")

        }) {

            assertEquals(HttpStatusCode.OK, response.status())
            return@withTestApplication
        }
    }
}