import org.slf4j.*

class Test {

    private val logger: Logger = LoggerFactory.getLogger(Test::class.java)

    @org.junit.Test
    fun `test logger dependency`() {
        logger.info("Hello from logger")
        assert(true)
    }

}
