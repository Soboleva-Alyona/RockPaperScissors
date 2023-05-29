import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.util.ResourceBundle

class RockPaperScissorsTest {


    private val userPlayer = mock<Player>()

    private val machinePlayer = mock<Player>()

    private val gameConfig = mock<Config>()

    private val resourceBundle = ResourceBundle.getBundle("game_ru_RU")

    init {
        given(gameConfig.parse(anyString())).willReturn(ConfigParsingCodes.SUCCESS)
    }

    private val rockPaperScissors = RockPaperScissors(userPlayer, machinePlayer, gameConfig, resourceBundle)

    @Test
    fun `user should win and exit after first move`() {
        // given
        given(gameConfig.getAvailableFigures()).willReturn(
            setOf("камень", "ножницы", "бумага")
        )

        given(userPlayer.makeMove()).willReturn("камень")
        given(machinePlayer.makeMove()).willReturn("ножницы")
        given(gameConfig.firstWinsSecond("камень", "ножницы")).willReturn(true)
        given(userPlayer.nextCommand()).willReturn("выход")

        // when
        val resultScore = rockPaperScissors.startTheGame()

        // then
        assertEquals(1 to 0, resultScore)
    }
}
