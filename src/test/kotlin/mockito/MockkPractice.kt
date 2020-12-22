package mockito

import io.mockk.*
import java.io.*
import java.net.*
import kotlin.test.*

class MockkPractice {

    private lateinit var httpCon: HttpURLConnection

    @BeforeTest
    fun beforeTests() {
        httpCon = mockk()
        every { httpCon.connect() } returns Unit
        every { httpCon.responseCode } returns 200
        every { httpCon.responseMessage } returns "HTTP/1.0 200 OK"
        every { httpCon.requestMethod } returns "GET"
    }

    @Test
    fun `test getting text from html body`() {
        val expected = "This is response from req"
        every { httpCon.inputStream } returns ByteArrayInputStream(
            """
            |HTML CODE HTML CODE HTML CODE
            |<tag>This is response from req</tag> 
            |HTML CODE HTML CODE HTML CODE""".trimMargin()
                .toByteArray()
        )
        val parser = Parser(httpCon)
        assertEquals(expected, parser.parseRequest())
    }

    @Test
    fun `test getting text with out html tags`() {
        val expected = "This is response from req\nHello btw"
        every { httpCon.inputStream } returns ByteArrayInputStream(
            "This <i>is</i> <b>response</b> from req<br>Hello btw".toByteArray()
        )
        val parser = Parser(httpCon)
        assertEquals(expected, parser.parseRequest())
    }

    @Test
    fun `test getting from html body and with out html tags`() {
        val expected = "This is response from req\nHello btw"
        every { httpCon.inputStream } returns ByteArrayInputStream(
            """HTML CODE HTML CODE HTML CODE
                    |<tag>This is response from req<br>Hello btw</tag>
                    |HTML CODE HTML CODE HTML CODE""".trimMargin()
                .toByteArray()
        )
        val parser = Parser(httpCon)
        assertEquals(expected, parser.parseRequest())
    }

    @Test(expected = RuntimeException::class)
    fun `test page not found response`() {
        every { httpCon.responseCode } returns 404
        Parser(httpCon).parseRequest()
    }

}

class Parser(
    private val httpCon: HttpURLConnection,
    private val startTag: String = "<tag>",
    private val endTag: String = "</tag>"
) {

    fun parseRequest(): String {
        httpCon.connect()
        if (!httpCon.isResponsePresent()) throw RuntimeException("Response is not present")
        var textContent = String(httpCon.inputStream.readBytes())
        textContent = getContent(textContent, startTag, endTag)
        return removeHtmlTags(textContent)

    }

    private fun getContent(response: String, startTag: String, endTag: String): String {
        return response.substring(
            response.indexOf(startTag).let { if (it >= 0) it + startTag.length else 0 },
            response.indexOf(endTag).let { if (it >= 0) it else response.length }
        )
    }


    private fun removeHtmlTags(response: String): String {
        var newLine = response.replace("<i>|</i>|<b>|</b>|<p>".toRegex(), "")
        newLine = newLine.replace("<br>|</p>".toRegex(), "\n")
        return newLine
    }

    private fun HttpURLConnection.isResponsePresent() = responseMessage.isNotEmpty() && responseCode == 200

}
