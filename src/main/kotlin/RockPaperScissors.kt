import java.util.*

class RockPaperScissors(
    private val playerOne: Player, // user
    private val playerTwo: Player, // machine
    private val config: Config,
    private val resourceBundle: ResourceBundle,
    private val path: String? = null,
) {

    private var gameScore: Pair<Int, Int> // user - machine

    init {
        when (config.parse(getPath())) {
            ConfigParsingCodes.CONFLICTING_RULES -> {
                throw ConfigParsingException(resourceBundle.getString("exception.ConflictingRules"))
            }
            ConfigParsingCodes.INCORRECT_FORMAT_OF_RULE -> {
                throw ConfigParsingException(resourceBundle.getString("exception.IncorrectRuleFormat"))
            }
            ConfigParsingCodes.DUPLICATES -> {
                throw ConfigParsingException(resourceBundle.getString("exception.DuplicateRulesInConfig"))
            }
            ConfigParsingCodes.SUCCESS -> {
                // do nothing
            }
        }

        playerOne.setConfig(config)
        playerTwo.setConfig(config)

        gameScore = 0 to 0
    }

    fun startTheGame(): Pair<Int, Int> {
        newMove()
        return gameScore
    }

    private fun getPath(): String {
        if (path.isNullOrEmpty()) return DEFAULT_CONFIG_PATH
        return path
    }

    private fun newMove() {
        informBothPlayers(
            config.getAvailableFigures().joinToString(", ") + " " + resourceBundle.getString("game.OneTwoThree")
        )

        val playerOneInput = playerOne.makeMove()
        if (shouldExitOrSkipToNextMove(playerOneInput)) return

        val playerTwoInput = playerTwo.makeMove()
        if (shouldExitOrSkipToNextMove(playerTwoInput)) return

        if (!haveWinner(playerOneInput, playerTwoInput)) {
            doNextMoveWithMessage(resourceBundle.getString(DRAW_RESULT_CODE))
            return
        }

        informBothPlayers(
            resourceBundle.getString("game.Score") + " ${playerOne.getPlayerName()} ${gameScore.first} " +
                    "- ${gameScore.second} ${playerTwo.getPlayerName()}"
        )
        informBothPlayers(resourceBundle.getString(SUGGEST_TO_CONTINUE_MESSAGE_CODE))

        while (true) {
            when (playerOne.nextCommand()) {
                resourceBundle.getString(CONTINUE_COMMAND_CODE) -> {
                    when (playerTwo.nextCommand()) {
                        resourceBundle.getString(CONTINUE_COMMAND_CODE) -> {
                            newMove()
                            return
                        }
                        resourceBundle.getString(EXIT_COMMAND_CODE) -> {
                            return
                        }
                        else -> {
                            playerTwo.setResponse(resourceBundle.getString(CONTINUE_OR_EXIT_COMMAND_EXPECTED_CODE))
                        }
                    }
                }
                resourceBundle.getString(EXIT_COMMAND_CODE) -> {
                    return
                }
                else -> {
                    playerOne.setResponse(resourceBundle.getString(CONTINUE_OR_EXIT_COMMAND_EXPECTED_CODE))
                }
            }
        }

    }

    private fun informBothPlayers(message: String) {
        playerOne.setResponse(message)
        playerTwo.setResponse(message)
    }

    private fun shouldExitOrSkipToNextMove(playerInput: String): Boolean {
        if (playerInput == resourceBundle.getString(EXIT_COMMAND_CODE)) {
            return true
        }
        if (playerInput == resourceBundle.getString(CONTINUE_COMMAND_CODE)) {
            newMove()
            return true
        }
        if (!config.getAvailableFigures().contains(playerInput)) {
            doNextMoveWithMessage(unexpectedFiguresMessage())
            return true
        }
        return false
    }

    private fun haveWinner(playerOneInput: String, playerTwoInput: String): Boolean {
        if (playerOneInput == playerTwoInput) {
            return false
        }

        if (config.firstWinsSecond(playerOneInput, playerTwoInput)) {
            gameScore = (gameScore.first + 1) to gameScore.second
            playerOne.setResponse(resourceBundle.getString(WIN_RESULT_CODE))
            playerTwo.setResponse(resourceBundle.getString(FAIL_RESULT_CODE))
            return true
        } else if (config.firstWinsSecond(playerTwoInput, playerOneInput)) {
            gameScore = gameScore.first to (gameScore.second + 1)
            playerOne.setResponse(resourceBundle.getString(FAIL_RESULT_CODE))
            playerTwo.setResponse(resourceBundle.getString(WIN_RESULT_CODE))
            return true
        }

        return false
    }

    private fun doNextMoveWithMessage(m: String) {
        informBothPlayers(m)
        newMove()
        return
    }

    private fun unexpectedFiguresMessage() =
        resourceBundle.getString(UNEXPECTED_FIGURES_CODE) + " " + config.getAvailableFigures().joinToString(", ")

    companion object {
        private const val DEFAULT_CONFIG_PATH = "src/main/resources/config.txt"

        const val CONTINUE_COMMAND_CODE = "game.ContinueCommand"
        private const val EXIT_COMMAND_CODE = "game.ExitCommand"

        private const val DRAW_RESULT_CODE = "game.Draw"
        private const val WIN_RESULT_CODE = "game.Win"
        private const val FAIL_RESULT_CODE = "game.Fail"
        private const val SUGGEST_TO_CONTINUE_MESSAGE_CODE =
            "game.SuggestToContinueOrExit"
        private const val CONTINUE_OR_EXIT_COMMAND_EXPECTED_CODE =
            "game.ContinueOrExitCommandExpected"
        private const val UNEXPECTED_FIGURES_CODE =
            "game.UnexpectedFigures"

    }

}
