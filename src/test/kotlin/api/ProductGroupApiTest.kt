package api

import com.epam.kotlinapp.crud.exceptions.ProductGroupNotFoundException
import com.epam.kotlinapp.crud.model.Product
import com.epam.kotlinapp.crud.model.ProductGroup
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import main
import org.junit.FixMethodOrder
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.runners.MethodSorters
import java.lang.reflect.Type
import java.util.ArrayList


class ProductGroupApiTest {
    private val url: String = "/productgroup"
    private val gson = GsonBuilder().create();
    private val expectedProduct: ProductGroup = ProductGroup(1, "Super Duper Group")
    private val expectedAllProductList: List<ProductGroup> =
        listOf(expectedProduct, ProductGroup(2, "Super Duper Group v2"))
    private val productIdToDelete: Int = 2


    @Test
    fun getAllProductGroupApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/all")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val groupListType: Type = object : TypeToken<ArrayList<ProductGroup?>?>() {}.type
            assertEquals(expectedAllProductList, gson.fromJson(response.content, groupListType))
        }
    }

    @Test
    fun getProductGroupByIdApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/1")) {
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

        with(handleRequest(HttpMethod.Delete, "$url/$productIdToDelete") {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")

        }) {

            assertEquals(HttpStatusCode.OK, response.status())
            return@withTestApplication
        }
    }
}