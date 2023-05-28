import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GameConfigTest {

    private val gameConfig = GameConfig()

    @Test
    fun `should successfully parse a good config`() {
        // when
        val code = gameConfig.parse("src/test/resources/good_config.txt")

        // then
        assertEquals(ConfigParsingCodes.SUCCESS, code)
    }

    @Test
    fun `should return correct move result for scissors and paper`() {
        // when
        gameConfig.parse("src/test/resources/good_config.txt")
        val figures = gameConfig.getAvailableFigures()
        val moveResult = gameConfig.firstWinsSecond("ножницы", "бумага")

        // then
        assertEquals(setOf("камень", "ножницы", "бумага"), figures)
        assertTrue(moveResult)

    }

    @Test
    fun `should fail during config parsing when there are duplicates in figures list`() {
        // when
        val code = gameConfig.parse("src/test/resources/config_with_duplicates.txt")

        // then
        assertEquals(ConfigParsingCodes.DUPLICATES, code)

    }

    @Test
    fun `should fail during config parsing when there conflicting rules`() {
        // when
        val code = gameConfig.parse("src/test/resources/config_with_conflicting_rules.txt")

        // then
        assertEquals(ConfigParsingCodes.CONFLICTING_RULES, code)

    }

    @Test
    fun `should fail during config parsing when there are rules with unlisted figure`() {
        // when
        val code = gameConfig.parse("src/test/resources/config_with_unlisted_figure_in_rule.txt")

        // then
        assertEquals(ConfigParsingCodes.INCORRECT_FORMAT_OF_RULE, code)
    }

}
