package com.epam

import com.sun.net.httpserver.*
import java.io.*
import java.net.*
import kotlin.text.toByteArray

@Deprecated("There is some problem with kotlin main")
object LocalServer {

    private val httpServer: HttpServer = HttpServer.create(InetSocketAddress("localhost", 1234), 0)

    fun startServer() {
        httpServer.createContext("/api/agents/Petclinic/plugins/test2code/dispatch-action") { httpExchange ->
            val response = "OK"
            val ss = httpExchange.requestHeaders["drill-test-name"]?.firstOrNull() ?: ""
            println("SS polychaeca $ss")
            httpExchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
            val os = httpExchange.responseBody
            os.write(response.toByteArray())
            os.close()
        }
        httpServer.createContext("/api/login") { httpExchange ->
            if (httpExchange.requestMethod.equals("GET")) {
                val response = "OK"
                httpExchange.responseHeaders.add("authorization", "token")
                httpExchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
                val os = httpExchange.responseBody
                os.write(response.toByteArray())
                os.close()
            }
        }
        httpServer.createContext("/test") {
            val response = "This is the response"
            it.sendResponseHeaders(200, response.length.toLong())
            val os: OutputStream = it.getResponseBody()
            os.write(response.toByteArray())
            os.close()
        }
        httpServer.executor = null
        httpServer.start()
        println("Server host ${httpServer.address} ")
    }

    fun close() {
        println("I am closed")
        httpServer.stop(1)
    }

}

fun main(args: Array<String>) {
    println("Hello there")
    LocalServer.startServer()
    Runtime.getRuntime().addShutdownHook(Thread {
        LocalServer.close()
    })
}
