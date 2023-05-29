class UserPlayer(private val name: String) : Player {

    override fun getPlayerName(): String {
        return name
    }

    override fun setConfig(config: Config) {
        // do nothing
    }

    override fun makeMove(): String {
        return readln().trim()
    }

    override fun nextCommand(): String {
        return readln().trim()
    }

    override fun setResponse(message: String) {
        println(message)
    }

}