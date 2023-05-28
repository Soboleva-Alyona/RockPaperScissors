import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

class RockPaperScissorsTest {


    private val userPlayer = mock<Player>()

    private val machinePlayer = mock<Player>()

    private val gameConfig = mock<Config>()

    init {
        given(gameConfig.parse("../resources/config.txt")).willReturn(ConfigParsingCodes.SUCCESS)
    }

    private val rockPaperScissors = RockPaperScissors(userPlayer, machinePlayer, gameConfig)

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
