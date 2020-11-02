package chat

import de.nielsfalk.ktor.swagger.swaggerUi
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.server.testing.*
import main
import java.lang.Exception
import kotlin.test.Test
import kotlin.test.assertEquals

class WebSocketTest {

    private val userName = "Roma"
    private val anotherUser = "Sanya"

    @Test
    fun callbackFromServerTest() = withTestApplication(Application::main) {
        val expected: Array<String> = arrayOf(
            "<server> New user in the chat: $userName",
            "<server> Users online : 1",
            "<$userName> Hello world!"
        )
        var message: Array<String> = emptyArray()

        handleWebSocketConversation("/ws") { incoming, outgoing ->
            outgoing.send(Frame.Text(userName))
            for (i in 1..2) {
                when (val frame = incoming.receive()) {
                    is Frame.Text -> message += frame.readText()
                }
            }
            outgoing.send(Frame.Text("Hello world!"))
            when (val frame = incoming.receive()) {
                is Frame.Text -> message += frame.readText()
            }
        }
        for (i in expected.indices) {
            assertEquals(expected[i], message[i])
        }
    }

    @Test
    fun otherUserMessageCallbackTest() = withTestApplication(Application::main) {
        val expected: Array<String> = arrayOf(
            "<server> New user in the chat: $userName",
            "<server> Users online : 1",
            "<$userName> Hello world!",
            "<$userName> Hello world!",
            "<server> New user in the chat: $anotherUser",
            "<server> Users online : 2",
        )
        var message: Array<String> = emptyArray()
        try {
            handleWebSocketConversation("/ws") { incoming1, outgoing1 ->
                outgoing1.send(Frame.Text(userName))
                message += (incoming1.receive() as Frame.Text).readText()
                message += (incoming1.receive() as Frame.Text).readText()
                outgoing1.send(Frame.Text("Hello world!"))
                message += (incoming1.receive() as Frame.Text).readText()

                handleWebSocketConversation("/ws") { incoming2, outgoing2 ->

                    outgoing2.send(Frame.Text(anotherUser))
                    message += (incoming2.receive() as Frame.Text).readText()
                    message += (incoming2.receive() as Frame.Text).readText()
                    message += (incoming2.receive() as Frame.Text).readText()
                }
            }
        } catch (ignore: Exception) {
        }

        for (i in expected.indices) {
            assertEquals(expected[i], message[i])
        }
    }
}