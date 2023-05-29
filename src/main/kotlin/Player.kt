interface Player {

    fun getPlayerName(): String

    fun setConfig(config: Config)

    fun makeMove(): String

    fun nextCommand(): String?

    fun setResponse(message: String)
}