class RockPaperScissors(
    private val playerOne: Player, // user
    private val playerTwo: Player, // machine
    private val config: Config,
    private val path: String? = null
) {

    private var gameScore: Pair<Int, Int> // user - machine

    init {
        when(config.parse(getPath())) {
            ConfigParsingCodes.CONFLICTING_RULES -> {
                throw ConfigParsingException("Conflicting rules in config")
            }
            ConfigParsingCodes.INCORRECT_FORMAT_OF_RULE -> {
                throw ConfigParsingException("Incorrect format of rule in config")
            }
            ConfigParsingCodes.DUPLICATES -> {
                throw ConfigParsingException("Duplicates found in figures list in config")
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
        informBothPlayers(config.getAvailableFigures().joinToString(", ") + " раз-два-три")

        val playerOneInput = playerOne.makeMove()
        if (shouldExitOrSkipToNextMove(playerOneInput)) return

        val playerTwoInput = playerTwo.makeMove()
        if (shouldExitOrSkipToNextMove(playerTwoInput)) return

        if (!haveWinner(playerOneInput, playerTwoInput)) {
            doNextMoveWithMessage(DRAW_RESULT)
            return
        }

        informBothPlayers("Счёт: ${playerOne.getPlayerName()} ${gameScore.first} - ${gameScore.second} ${playerTwo.getPlayerName()}")
        informBothPlayers(SUGGEST_TO_CONTINUE_MESSAGE)

        while (true) {
            when (playerOne.nextCommand()) {
                CONTINUE_COMMAND -> {
                    when (playerTwo.nextCommand()) {
                        CONTINUE_COMMAND -> {
                            newMove()
                            return
                        }
                        EXIT_COMMAND -> {
                            return
                        }
                        else -> {
                            playerTwo.setResponse(CONTINUE_OR_EXIT_COMMAND_EXPECTED)
                        }
                    }
                }
                EXIT_COMMAND -> {
                    return
                }
                else -> {
                    playerOne.setResponse(CONTINUE_OR_EXIT_COMMAND_EXPECTED)
                }
            }
        }

    }

    private fun informBothPlayers(message: String) {
        playerOne.setResponse(message)
        playerTwo.setResponse(message)
    }

    private fun shouldExitOrSkipToNextMove(playerInput: String): Boolean {
        if (playerInput == EXIT_COMMAND) {
            return true
        }
        if (playerInput == CONTINUE_COMMAND) {
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
            playerOne.setResponse(WIN_RESULT)
            playerTwo.setResponse(FAIL_RESULT)
            return true
        } else if (config.firstWinsSecond(playerTwoInput, playerOneInput)) {
            gameScore = gameScore.first to (gameScore.second + 1)
            playerOne.setResponse(FAIL_RESULT)
            playerTwo.setResponse(WIN_RESULT)
            return true
        }

        return false
    }

    private fun doNextMoveWithMessage(m: String) {
        informBothPlayers(m)
        newMove()
        return
    }

    private fun unexpectedFiguresMessage() = "Unexpected figure in the input line. " +
            "Available figures are: ${config.getAvailableFigures().joinToString(", ")}." +
            "Try again"

    companion object {
        private const val DEFAULT_CONFIG_PATH = "src/main/resources/config.txt"

        const val CONTINUE_COMMAND = "ещё раз"
        private const val EXIT_COMMAND = "выход"

        private const val DRAW_RESULT = "ничья"
        private const val WIN_RESULT = "вы победили"
        private const val FAIL_RESULT = "вы проиграли"
        private const val SUGGEST_TO_CONTINUE_MESSAGE =
            "To continue playing type: $CONTINUE_COMMAND, to exit: $EXIT_COMMAND"
        private const val CONTINUE_OR_EXIT_COMMAND_EXPECTED =
            "Continue or exit command expected. Please type: $CONTINUE_COMMAND or $EXIT_COMMAND"

    }

}
