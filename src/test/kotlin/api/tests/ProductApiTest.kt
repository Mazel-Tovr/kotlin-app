package api.tests

import com.epam.kotlinapp.crud.business.ICommonServices
import com.epam.kotlinapp.crud.business.ProductService
import com.epam.kotlinapp.crud.dao.ProductOperations
import com.epam.kotlinapp.crud.exceptions.ProductNotFoundException
import com.epam.kotlinapp.crud.model.Product
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.FixMethodOrder
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.runners.MethodSorters
import java.lang.reflect.Type
import java.util.ArrayList
import kotlin.test.BeforeTest

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ProductApiTest
{
    private val url:String = "/product"
    private val gson = GsonBuilder().create();
    private val service:ICommonServices<Product> = ProductService

    @BeforeTest
    fun init()
    {
        RunApiTests.setConn(ProductOperations)
    }


    @Test
    fun _1_getAllProductApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/all")) {
            assertEquals(HttpStatusCode.OK, response.status())
            val groupListType: Type = object : TypeToken<ArrayList<Product?>?>() {}.getType()
            assertEquals(service.getAll(), gson.fromJson(response.content, groupListType))
        }
    }

    @Test
    fun _2_getProductByIdApiTest() = withTestApplication(Application::main) {
        with(handleRequest(HttpMethod.Get, "$url/1")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(service.getEntity(1), gson.fromJson(response.content, Product::class.java))
        }
    }

    @Test(ProductNotFoundException::class)
    fun _3_createAndDeleteProductApiTest() = withTestApplication(Application::main) {

        var product:Product
        with(handleRequest(HttpMethod.Post, url) {
            addHeader("accept","application/json")
            addHeader("Content-Type" ,"application/json")
            setBody(
                gson.toJson(
                    Product(0, "Product", 123, "Product",1,1)
                )
            )
        }) {
            assertEquals(HttpStatusCode.Created, response.status())
            product = gson.fromJson(response.content, Product::class.java)
            assertEquals(service.getEntity(product.id!!), product)
        }

        with(handleRequest(HttpMethod.Delete, url.plus("/${product.id}")) {
            addHeader("accept", "application/json")
            addHeader("Content-Type", "application/json")
//            setBody(
//                gson.toJson(
//                    product
//                )
//            )
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            service.getEntity(product.id!!)
            return@withTestApplication
        }
    }
}